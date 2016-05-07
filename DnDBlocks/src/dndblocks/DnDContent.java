package dndblocks;

import org.eclipse.swt.graphics.GC;

public abstract class DnDContent {
    private DnDCell parentCell;
    private DnDStructure parentStructure = null;

    abstract void paint(GC gc);

    abstract Object getContent();

    protected DnDContent(DnDCell parentCell) {
	this.parentCell = parentCell;
	if (parentCell != null) {
	    this.parentCell.addContent(this);
	}
    }

    public DnDCell getCell() {
	return parentCell;
    }

    public DnDStructure getStructure() {
	return parentStructure;
    }

    protected void setStructure(DnDStructure structure) {
	parentStructure = structure;
    }

    protected void setCell(DnDCell cell) {
	parentCell = cell;
    }
}
