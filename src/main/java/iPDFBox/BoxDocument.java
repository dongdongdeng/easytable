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

    public void save(String outputFileName) {
    try {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float lastY = page.getMediaBox().getHeight() - DOCUMENT_PADDING;

            for (int i=0; i<mObjectList.size(); i++) {
                if (mObjectList.get(i) instanceof BoxTable) {
                    Table table = ((BoxTable)mObjectList.get(i)).getTableBuilder().build();

                    TableDrawer.builder()
                            .contentStream(contentStream)
                            .table(table)
                            .startX(DOCUMENT_PADDING)
                            .startY(lastY)
                            .build()
                            .draw();

                    lastY = lastY - table.getHeight()- 20;

                } else if (mObjectList.get(i) instanceof BoxParagraph) {
                	BoxParagraph paragraph = (BoxParagraph)mObjectList.get(i);
                	
                	float textWidth = PdfUtil.getStringWidth(paragraph.getText(), paragraph.getFont(), paragraph.getFontSize());
                	
                	float pageWidth = page.getMediaBox().getWidth();

                	float startX = DOCUMENT_PADDING;
                	if (paragraph.getAlign().equals(HorizontalAlignment.CENTER)) {
                		startX = (pageWidth-textWidth) / 2;
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
                    contentStream.setFont(this.mDefaultFont, DEFAULT_FONT_SIZE);
                    //Begin the Content stream
                    contentStream.beginText();

                    //Setting the position for the line
                    contentStream.newLineAtOffset(DOCUMENT_PADDING, lastY);

                    //Adding text in the form of string
                    contentStream.showText((String) mObjectList.get(i));

                    //Ending the content stream
                    contentStream.endText();

                    lastY = lastY - 1.5f * this.DEFAULT_FONT_SIZE;
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
