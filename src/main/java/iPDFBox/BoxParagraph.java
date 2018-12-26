package iPDFBox;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;

public class BoxParagraph {
	private String mText = "";
	private PDFont mFont;
	private Color mColor = Color.BLACK;
	private int mFontSize;
	private HorizontalAlignment mAlignment;
	
	public List<BoxParagraph> mParagraphList = new ArrayList<>();
	
	public BoxParagraph(String text, HorizontalAlignment align, PDFont font, int fontSize) {
		this.mText = text;
		this.mAlignment = align;
		this.mFont = font;
		this.mFontSize = fontSize;
	}
	
	public BoxParagraph(String text, HorizontalAlignment align, PDFont font, int fontSize, Color color) {
		this.mText = text;
		this.mAlignment = align;
		this.mFont = font;
		this.mFontSize = fontSize;
		this.mColor = color;
	}
	
	public String getText() {
		return this.mText;
	}
	
	public PDFont getFont() {
		return this.mFont;
	}
	
	public int getFontSize() {
		return this.mFontSize;
	}
	
	public Color getColor() {
		return this.mColor;
	}
	
	public HorizontalAlignment getAlign() {
		return this.mAlignment;
	}
	
	public void addParagraph(String text, HorizontalAlignment align, PDFont font, int fontSize) {
		BoxParagraph boxParagraph = new BoxParagraph(text, align, font, fontSize);
		this.mParagraphList.add(boxParagraph);
	}
	
	public void addParagraph(BoxParagraph boxParagraph) {
		this.mParagraphList.add(boxParagraph);
	}
	
	public List<BoxParagraph> getParagraphList() {
		return this.mParagraphList;
	}
}
