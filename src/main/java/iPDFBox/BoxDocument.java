package iPDFBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoxDocument {
    private static final String TARGET_FOLDER = "target";

    private static final float DOCUMENT_PADDING = 50f;

    private String mOutputFileName;

    private List<BoxTable> mTableList = new ArrayList<>();

    private List<Object> mObjectList = new ArrayList<>();

    public BoxDocument(String outputFileName) {
        this.mOutputFileName = outputFileName;
    }

    public void add(String text) {
        mObjectList.add(text);
    }

    public void add(BoxTable table) {
        mObjectList.add(table);
    }

    public void save() {
        this.save(null);
    }

    private static PDFont loadFont(String location) {
        PDFont font;
        try {
            PDDocument documentMock = new PDDocument();
            //InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(location);
            //Encoding encoding = Encoding.getInstance(COSName.WIN_ANSI_ENCODING);
            font = PDType0Font.load(documentMock, new File(location));
            //font = PDTrueTypeFont.load(documentMock, systemResourceAsStream, encoding);
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

                } else {
                    // text
                    PDFont pdFont = loadFont("D:\\simfang.ttf");
                    contentStream.setFont(pdFont, 10);
                    //Begin the Content stream
                    contentStream.beginText();

                    //Setting the position for the line
                    contentStream.newLineAtOffset(DOCUMENT_PADDING, lastY);

                    //Adding text in the form of string
                    contentStream.showText((String) mObjectList.get(i));

                    //Ending the content stream
                    contentStream.endText();

                    lastY = lastY - 1.5f*10;
                }
            }
        }

        if (null == outputFileName) {
            document.save(TARGET_FOLDER + "/" + this.mOutputFileName);
        } else {
            document.save(TARGET_FOLDER + "/" + outputFileName);
        }

        document.close();
    }catch (Exception e) {
        e.printStackTrace();
    }
    }
}
