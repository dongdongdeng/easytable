package iPDFBox;

import org.vandeseer.easytable.structure.Table;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class BoxCell {

    private String mText = "";

    private int mColumnSpan = 1;

    public BoxCell(String text) {
        this.mText = text;
    }

    public void setColspan(int colspan) {
        this.mColumnSpan = colspan;
    }

    public int getColspan() {
        return this.mColumnSpan;
    }

    public String getText() {
        return this.mText;
    }
}
