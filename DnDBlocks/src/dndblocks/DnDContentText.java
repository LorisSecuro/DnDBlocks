package dndblocks;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class DnDContentText extends DnDContent {

    private String text;

    public DnDContentText(DnDCell parentCell, String text) {
	super(parentCell);
	setContent(text);
    }

    @Override
    public void paint(GC gc) {
	if (text == null) {
	    return;
	}

	Point textExtent = gc.textExtent(text);

	Rectangle clipping = gc.getClipping();
	double posX = (double) (clipping.width) / 2 - (double) (textExtent.x)
		/ 2;
	double posY = (double) (clipping.height) / 2 - (double) (textExtent.y)
		/ 2;
	gc.drawText(text, (int) posX, (int) posY);
    }

    @Override
    public Object getContent() {
	return text;
    }

    public void setContent(String text) {
	this.text = text;
    }
}
