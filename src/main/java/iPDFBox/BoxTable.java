package iPDFBox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;

public class BoxTable {

    private Table.TableBuilder mTable;

    private int mNumColumns = 0;

    private List<BoxCell> mCellList = new ArrayList<>();

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

    public BoxTable(int numColumns) {
        PDFont pdFont = loadFont("D:\\simfang.ttf");
        mNumColumns = numColumns;

        mTable = Table.builder();

        for(int i=0; i<mNumColumns; i++) {
            mTable.addColumnOfWidth(70);
        }

        mTable.fontSize(8).font(pdFont).borderColor(Color.LIGHT_GRAY);

    }

    public void addCell(String text) {
        BoxCell boxCell = new BoxCell(text);
        mCellList.add(boxCell);
    }

    public void addCell(BoxCell boxCell) {
        if (1 == boxCell.getColspan()) {
            mCellList.add(boxCell);
        } else {
            mCellList.add(boxCell);
            for (int i=1; i<boxCell.getColspan(); i++) {
                mCellList.add(new BoxCell("_@_"));
            }
        }
    }

    public void flush( ) throws Exception {
        Color BLUE_DARK = new Color(76, 129, 190);

        for (int i=0; i<mCellList.size(); i++) {

            if (0 == (i+1) % mNumColumns) {
                Row.RowBuilder rowBuilder = Row.builder();

                for (int j=mNumColumns - 1; j>=0; j--) {
                    if (mCellList.get(i-j).getText().equals("_@_")) {
                        // 空白（被前面占位）
                    } else {
                        rowBuilder.add(CellText.builder().text(mCellList.get(i - j).getText()).span(mCellList.get(i - j).getColspan()).borderWidth(1).build());
                    }

                }
                PDFont pdFont = loadFont("D:\\simfang.ttf");
                Row row = rowBuilder.backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(pdFont).fontSize(9)
                        .horizontalAlignment(CENTER)
                        .build();

                mTable.addRow(row);
            }
        }

    }

    public Table.TableBuilder getTableBuilder() {
        return mTable;
    }
}
