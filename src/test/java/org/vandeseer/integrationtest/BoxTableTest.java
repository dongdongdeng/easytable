package org.vandeseer.integrationtest;

import iPDFBox.BoxCell;
import iPDFBox.BoxDocument;
import iPDFBox.BoxTable;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;

public class BoxTableTest {

    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static String OUTPUT_FILE_NAME = "boxTable.pdf";

    @Test
    public void tableExample() throws Exception {

        BoxTable boxTable = new BoxTable(7);
        boxTable.addCell("保单号");
        boxTable.addCell("给付险种");
        boxTable.addCell("合同处理结论");
        boxTable.addCell("给付金额(元)");
        boxTable.addCell("红利(元)");
        boxTable.addCell("生存金(元)");
        boxTable.addCell("退还保费(元)");

        boxTable.addCell("860000001");
        boxTable.addCell("康E保");
        boxTable.addCell("同意赔付");
        boxTable.addCell("￥1000.00");
        boxTable.addCell("￥100");
        boxTable.addCell("￥20");
        boxTable.addCell("￥300");

        BoxCell boxCell = new BoxCell("合计");
        boxCell.setColspan(3);
        boxTable.addCell(boxCell);

        boxTable.addCell("0");
        boxTable.addCell("0");
        boxTable.addCell("0");
        boxTable.addCell("0");

        boxTable.flush();

        BoxTable boxTable2 = new BoxTable(7);
        boxTable2.addCell("保单号");
        boxTable2.addCell("给付险种");
        boxTable2.addCell("合同处理结论");
        boxTable2.addCell("给付金额(元)");
        boxTable2.addCell("红利(元)");
        boxTable2.addCell("生存金(元)");
        boxTable2.addCell("退还保费(元)");

        boxTable2.addCell("860000001");
        boxTable2.addCell("康E保");
        boxTable2.addCell("同意赔付");
        boxTable2.addCell("￥1000.00");
        boxTable2.addCell("￥100");
        boxTable2.addCell("￥20");
        boxTable2.addCell("￥300");

        BoxCell boxCell2 = new BoxCell("合计");
        boxCell2.setColspan(3);
        boxTable2.addCell(boxCell2);

        boxTable2.addCell("0");
        boxTable2.addCell("0");
        boxTable2.addCell("0");
        boxTable2.addCell("0");

        boxTable2.flush();

        BoxDocument boxDocument = new BoxDocument(OUTPUT_FILE_NAME);

        boxDocument.add("民 生 人 寿 保 险 股 份 有 限 公 司");

        boxDocument.add("理 赔 给 付 通 知 书");

        boxDocument.add("尊敬的张三 先生/女士：");

        boxDocument.add("您好！感谢您对我公司的支持与信任。");

        boxDocument.add("您本次提交的关于被保人张三于2018-12-24所发生事故的索赔资料，经本公司认真审核，现已理赔结案，给付详情如下所示：");

        boxDocument.add("赔案号：1234567890");

        boxDocument.add(boxTable);

        boxDocument.add("领取人信息");

        boxDocument.add(boxTable2);

        boxDocument.add("若您有任何疑问，请与当地客户服务部联系，或致电全国客户服务热线95596垂询。");

        boxDocument.add("谨祝 安康！");

        boxDocument.save();

    }

}
