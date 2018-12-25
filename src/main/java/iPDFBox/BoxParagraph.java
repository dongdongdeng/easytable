package iPDFBox;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;

public class BoxParagraph {
	private String mText = "";
	private PDFont mFont;
	private int mFontSize;
	private HorizontalAlignment mAlignment;
	
	public BoxParagraph(String text, HorizontalAlignment align, PDFont font, int fontSize) {
		this.mText = text;
		this.mAlignment = align;
		this.mFont = font;
		this.mFontSize = fontSize;
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
	
	public HorizontalAlignment getAlign() {
		return this.mAlignment;
	}
}
