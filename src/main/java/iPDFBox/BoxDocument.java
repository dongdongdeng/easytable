package iPDFBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoxDocument {
    private static final String TARGET_FOLDER = "target";

    private static final float DOCUMENT_PADDING = 50f;

    private static final int DEFAULT_FONT_SIZE = 10;

    private PDFont mDefaultFont = null;
    
    private List<BoxTable> mTableList = new ArrayList<>();

    private List<Object> mObjectList = new ArrayList<>();

    public BoxDocument(String fontFileLocation) {
    	try {
            PDDocument documentMock = new PDDocument();
            this.mDefaultFont = PDType0Font.load(documentMock, new File(fontFileLocation));
        }
        catch (IOException e) {
            throw new RuntimeException("IO exception");
        }
    }

    public void add(String text) {
        mObjectList.add(text);
    }
    
    public void add(BoxParagraph paragraph) {
        mObjectList.add(paragraph);
    }

    public void add(BoxTable table) {
        mObjectList.add(table);
    }

    public static PDFont loadFont(String fontFileLocation) {
    	PDFont font = null;
        try {
            PDDocument documentMock = new PDDocument();
            font = PDType0Font.load(documentMock, new File(fontFileLocation));
        }
        catch (IOException e) {
            throw new RuntimeException("IO exception");
        }
        return font;
    }
    
    /**
     * 增加一段文本
     * @param contentStream
     * @param width
     * @param sx
     * @param sy
     * @param text
     * @param justify
     * @throws IOException
     */
    private static int addParagraph(PDPageContentStream contentStream, float width, float sx,
                                     float sy, String text, boolean justify, PDFont font, int fontSize) throws IOException {
        List<String> lines = new ArrayList<>();
        parseLinesRecursive(text, width, lines, font, fontSize);

        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(sx, sy);
        for (String line: lines) {
            float charSpacing = 0;
            if (justify){
                if (line.length() > 1) {
                    float size = fontSize * font.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            contentStream.setCharacterSpacing(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -1.5f * fontSize);
        }
        return lines.size();
    }

    /**
     * 递归分析文本，并按宽度分割成N行
     * @param text
     * @param width
     * @param lines
     * @return
     * @throws IOException
     */
    private static List<String> parseLinesRecursive(String text, float width, List<String> lines, PDFont font, int fontSize) throws IOException {
        String tmpText = text;
        for (int i=0; i<text.length(); i++) {
            tmpText = text.substring(0, text.length() - i);

            float realWidth = fontSize * font.getStringWidth(tmpText) / 1000;

            if (realWidth > width) {
                continue;
            } else {
                lines.add(tmpText);

                if (0 != i) {
                    parseLinesRecursive(text.substring(text.length() - i), width, lines, font, fontSize);
                }

                break;
            }
        }

        return lines;
    }

    public void save(String outputFileName) {
    try {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float lastY = page.getMediaBox().getHeight() - DOCUMENT_PADDING;

            for (int i=0; i<mObjectList.size(); i++) {
                if (mObjectList.get(i) instanceof BoxTable) {
                	BoxTable boxTable = (BoxTable)mObjectList.get(i); 
                    Table table = boxTable.getTableBuilder().build();
                    
                    lastY = lastY - boxTable.getTopPadding();
                    
                    TableDrawer.builder()
                            .contentStream(contentStream)
                            .table(table)
                            .startX((page.getMediaBox().getWidth() - table.getWidth()) / 2)
                            .startY(lastY)
                            .build()
                            .draw(Color.LIGHT_GRAY, 1, 1, 0, 1);
                           
                    lastY = lastY - table.getHeight()- boxTable.getBottomPadding();

                } else if (mObjectList.get(i) instanceof BoxParagraph) {
                	BoxParagraph paragraph = (BoxParagraph)mObjectList.get(i);
                	
                	float textWidth = PdfUtil.getStringWidth(paragraph.getText(), paragraph.getFont(), paragraph.getFontSize());
                	
                	float pageWidth = page.getMediaBox().getWidth();

                	float startX = DOCUMENT_PADDING;
                	if (paragraph.getAlign().equals(HorizontalAlignment.CENTER)) {
                		startX = (pageWidth-textWidth) / 2;
                	} else if (paragraph.getAlign().equals(HorizontalAlignment.RIGHT)) {
                		startX = pageWidth - textWidth - DOCUMENT_PADDING; 
                	}
                	
                	contentStream.setFont(paragraph.getFont(), paragraph.getFontSize());
                    //Begin the Content stream
                    contentStream.beginText();

                    //Setting the position for the line
                    contentStream.newLineAtOffset(startX, lastY);

                    //Adding text in the form of string
                    contentStream.showText(paragraph.getText());

                    //Ending the content stream
                    contentStream.endText();

                    lastY = lastY - 1.5f * paragraph.getFontSize();
            		                	
                } else {
                    // text
                	String text = (String) mObjectList.get(i);
                	
                	float textWidth = PdfUtil.getStringWidth(text, this.mDefaultFont, DEFAULT_FONT_SIZE);
                	
                	float pageWidth = page.getMediaBox().getWidth();
	
                	// 宽度不够，需要换行
                	if (textWidth + DOCUMENT_PADDING * 2 > pageWidth) {
                		contentStream.beginText();
                		
                		int line = addParagraph(contentStream, pageWidth - DOCUMENT_PADDING * 2, 
                				DOCUMENT_PADDING, lastY, text, true,
                				this.mDefaultFont, DEFAULT_FONT_SIZE);
                		
                		contentStream.endText();
                		
                		lastY = lastY - 1.5f * this.DEFAULT_FONT_SIZE * line;
                	} else {
                	
	                    contentStream.setFont(this.mDefaultFont, DEFAULT_FONT_SIZE);
	                    //Begin the Content stream
	                    contentStream.beginText();
	                    
	                    contentStream.setNonStrokingColor(Color.BLUE);
	
	                    //Setting the position for the line
	                    contentStream.newLineAtOffset(DOCUMENT_PADDING, lastY);
	
	                    //Adding text in the form of string
	                    contentStream.showText(text);
	
	                    //Ending the content stream
	                    contentStream.endText();
	
	                    lastY = lastY - 1.5f * this.DEFAULT_FONT_SIZE;
                	}
                }
            }
        }

        document.save(TARGET_FOLDER + "/" + outputFileName);
        
        document.close();
    }catch (Exception e) {
        e.printStackTrace();
    }
    }
}
