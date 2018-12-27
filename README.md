# Fork from easytable

This is a (very) small project that builds upon easytable, but borrow some API fashion from iText.

## Example
In order to produce a whole PDF document with a table that looks like this one:

![easytable table](doc/demo2.png)

        final PDFont FONT = BoxDocument.loadFontFromResource("simfang.ttf");
    	
        BoxDocument boxDocument = new BoxDocument(FONT);
        
        boxDocument.add(new BoxParagraph("万 岁 人 寿 保 险 股 份 有 限 公 司", HorizontalAlignment.CENTER, FONT, 12, Color.BLUE));

        boxDocument.add(new BoxParagraph("理 赔 拒 赔 通 知 书", HorizontalAlignment.CENTER, FONT, 12, Color.BLUE));

        boxDocument.add(" ");
        boxDocument.add(" ");
        boxDocument.add(" ");
        
        boxDocument.add("尊敬的张三 先生/女士：");

        boxDocument.add("您好！感谢您对我公司的支持与信任。");

        boxDocument.add("您本次提交的关于被保人张三于2018-12-24所发生事故的索赔资料，经本公司认真审核，现已理赔结案，给付详情如下所示：");

        boxDocument.add("赔案号：1234567890");

        BoxTable boxTable = new BoxTable(7, 480, FONT);
        
        boxTable.addCell("保单号");
        boxTable.addCell("给付险种");
        boxTable.addCell("合同处理结论");
        boxTable.addCell("给付金额(元)");
        boxTable.addCell("红利(元)");
        boxTable.addCell("生存金(元)");
        boxTable.addCell("退还保费(元)");

        boxTable.addCell("860000001");
        boxTable.addCell("太子太保");
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
        
        boxDocument.add(boxTable);

        boxDocument.add("领取人信息");

        BoxTable boxTable2 = new BoxTable(6, 480, FONT);
        boxTable2.addCell("领款人");
        boxTable2.addCell("与被保险人关系");
        boxTable2.addCell("收益金额（元）");
        boxTable2.addCell("支付方式");
        boxTable2.addCell("银行帐号");
        boxTable2.addCell("银行账户名");
        
        boxTable2.addCell("李四");
        boxTable2.addCell("情敌");
        boxTable2.addCell("￥1000");
        boxTable2.addCell("银行转帐");
        boxTable2.addCell("12121212121212");
        boxTable2.addCell("李四");
        
        boxTable2.flush();

        boxDocument.add(boxTable2);

        boxDocument.add("若您有任何疑问，请与当地客户服务部联系，或致电全国客户服务热线400-XXXXXXXX垂询。");

        boxDocument.add("谨祝 安康！");
        
        boxDocument.add(new BoxParagraph("万岁人寿保险股份有限公司（青岛分公司）", HorizontalAlignment.RIGHT, FONT, 10));
        
        boxDocument.add(new BoxParagraph("(盖章)", HorizontalAlignment.RIGHT, FONT, 10));
        
        boxDocument.add(new BoxParagraph("2050-12-27 10:52", HorizontalAlignment.RIGHT, FONT, 10));

        boxDocument.save(OUTPUT_FILE_NAME);

## Q&A
