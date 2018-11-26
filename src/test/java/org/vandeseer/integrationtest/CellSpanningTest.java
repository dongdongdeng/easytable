package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellCustom;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

public class CellSpanningTest {

    @Test
    public void createSampleDocumentWithCellSpanning() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(CellCustom.builder().heightSupplier(() -> 35f).onDrawConsumer(context -> {
                    
                    try {
                        context.contentStream.moveTo(context.x, context.y);
                        context.contentStream.drawLine(context.x, context.y, context.x + 10, context.y + 10);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }

                }).span(2).build())
                .add(CellText.builder().text("Booz").build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Hey").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Ho!").build())
                .add(CellText.builder().text("Fu.").backgroundColor(Color.ORANGE).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Baz").span(2).backgroundColor(Color.CYAN).horizontalAlignment(CENTER).borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "cellSpanning.pdf");
    }

}
