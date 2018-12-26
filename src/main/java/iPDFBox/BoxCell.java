package iPDFBox;

import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Table;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class BoxCell {

    private String mText = "";
    
    private BoxParagraph mParagraph = null;
    
    private int mColumnSpan = 1;
    
    private HorizontalAlignment mAlignment = HorizontalAlignment.LEFT;

    public BoxCell(String text) {
        this.mText = text;
    }

    public BoxCell(BoxParagraph boxParagraph) {
    	this.mParagraph = boxParagraph;
    }
    
    public void setColspan(int colspan) {
        this.mColumnSpan = colspan;
    }

    public int getColspan() {
        return this.mColumnSpan;
    }
    
    public void setHorizontalAlignment(HorizontalAlignment alignment) {
    	this.mAlignment = alignment;
    }
    
    public HorizontalAlignment getHorizontalAlignment() {
    	return this.mAlignment;
    }

    public String getText() {
        return this.mText;
    }
    
    public BoxParagraph getParagraph() {
    	return this.mParagraph;
    }
}
