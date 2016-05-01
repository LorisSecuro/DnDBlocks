package dndblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class DnDCell extends Canvas {

    enum State {
	EMPTY, FILLED, CONTESTED
    };

    private State state = State.EMPTY;
    private State provisionalState = null;
    private List<DnDContent> contents = new ArrayList<DnDContent>();
    private DnDGrid parentGrid;

    private static DnDCell dragger = null;

    protected DnDCell(Composite parent, int style) {
	super(parent, style);

	parentGrid = (DnDGrid) parent;

	setupDnD();

	addPaintListener(new PaintListener() {
	    @Override
	    public void paintControl(PaintEvent e) {
		// this is where we paint
		DnDCell.this.paintControl(e);
	    }
	});
    }

    public DnDGrid getGrid() {
	return parentGrid;
    }

    public void removeContent(DnDContent content) {
	if (contents.size() <= 0) {
	    return;
	}
	contents.remove(content);
	calculateState();
	redraw();
    }

    public void removeContentOnTop() {
	if (contents.size() <= 0) {
	    return;
	}
	contents.remove(contents.size() - 1);
	calculateState();
	redraw();
    }

    public DnDContent getContentOnTop() {
	if (contents.size() <= 0) {
	    return null;
	}
	return contents.get(contents.size() - 1);
    }

    // protected because i don't want classes in external packages to call this directly, they have to call new DnDContent(...) 
    protected void addContent(DnDContent content) {
	if (content == null) {
	    return;
	}
	contents.add(content);
	calculateState();
	redraw();
    }

    private void dragEnterOperations() {
	if (state == State.EMPTY) {
	    provisionalState = State.FILLED;
	} else {
	    provisionalState = State.CONTESTED;
	}

	redraw();
    }

    private void dragLeaveOperations() {
	provisionalState = null;

	redraw();
    }

    private void dropOperations(DnDContent contentToDrop) {
	// remove from old cell
	contentToDrop.getCell().removeContent(contentToDrop);

	contentToDrop.setCell(DnDCell.this);
	addContent(contentToDrop);
    }

    private void setupDnD() {

	DropTarget dropTarget = new DropTarget(this, DND.DROP_MOVE);
	dropTarget
		.setTransfer(new Transfer[] { DnDDummyTransfer.getInstance() });
	dropTarget.addDropListener(new DropTargetListener() {

	    @Override
	    public void dragEnter(DropTargetEvent dropTargetEvent) {

		// if the source of drag is myself i don't want anything to happen
		if (dragger == DnDCell.this) {
		    return;
		}

		DnDStructure structure = dragger.getContentOnTop()
			.getStructure();
		if (structure != null) {
		    List<DnDCell> structureCellsShifted = structure
			    .getStructureCellsShiftedBy(dragger, DnDCell.this);
		    // if it was not able to get the cells to shift, I stop
		    if (structureCellsShifted == null) {
			return;
		    }
		    for (DnDCell shiftedCell : structureCellsShifted) {
			shiftedCell.dragEnterOperations();
		    }
		} else {
		    dragEnterOperations();
		}

	    }

	    @Override
	    public void dragLeave(DropTargetEvent dropTargetEvent) {

		// if the source of drag is myself i don't want anything to happen
		if (dragger == DnDCell.this) {
		    return;
		}

		DnDStructure structure = dragger.getContentOnTop()
			.getStructure();
		if (structure != null) {
		    List<DnDCell> structureCellsShifted = structure
			    .getStructureCellsShiftedBy(dragger, DnDCell.this);
		    // if it was not able to get the cells to shift, I stop
		    if (structureCellsShifted == null) {
			return;
		    }
		    for (DnDCell shiftedCell : structureCellsShifted) {
			shiftedCell.dragLeaveOperations();
		    }
		} else {
		    dragLeaveOperations();
		}
	    }

	    @Override
	    public void dragOperationChanged(DropTargetEvent dropTargetEvent) {
	    }

	    @Override
	    public void dragOver(DropTargetEvent dropTargetEvent) {
	    }

	    @Override
	    public void drop(DropTargetEvent dropTargetEvent) {

		DnDStructure structure = dragger.getContentOnTop()
			.getStructure();
		if (structure != null) {
		    Map<DnDCell, DnDContent> structureCellsShiftedAndCorrelatedContent = structure
			    .getStructureCellsShiftedAndCorrelatedContent(
				    dragger, DnDCell.this);

		    for (Map.Entry<DnDCell, DnDContent> shiftedCellAndContent : structureCellsShiftedAndCorrelatedContent
			    .entrySet()) {
			DnDCell dropCell = shiftedCellAndContent.getKey();
			DnDContent dropContent = shiftedCellAndContent
				.getValue();
			dropCell.getGrid().getPosition(dropCell);
			dropCell.dropOperations(dropContent);
		    }
		} else {
		    dropOperations(dragger.getContentOnTop());
		}
	    }

	    @Override
	    public void dropAccept(DropTargetEvent dropTargetEvent) {

		// if the source of drag is myself I cancel the operation
		if (dragger == DnDCell.this) {
		    dropTargetEvent.detail = DND.DROP_NONE;
		}

		DnDStructure structure = dragger.getContentOnTop()
			.getStructure();
		if (structure != null) {
		    List<DnDCell> structureCellsShifted = structure
			    .getStructureCellsShiftedBy(dragger, DnDCell.this);
		    // if it was not able to get the cells to shift, I stop
		    if (structureCellsShifted == null) {
			dropTargetEvent.detail = DND.DROP_NONE;
		    }
		}
	    }
	});

	DragSource dragSource = new DragSource(this, DND.DROP_MOVE);
	dragSource
		.setTransfer(new Transfer[] { DnDDummyTransfer.getInstance() });

	dragSource.addDragListener(new DragSourceListener() {
	    @Override
	    public void dragStart(DragSourceEvent dragSourceEvent) {

		// if no content then there is nothing to drag
		if (contents.size() == 0) {
		    dragSourceEvent.doit = false;
		    return;
		}

		// i save the drag source so I can use it during the drop operations
		dragger = DnDCell.this;

		// the image cause redraw problems while dragging a structure, see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=393868
		/*
		// getting control widget - Composite in this case
		Composite composite = (Composite) ((DragSource) dragSourceEvent
			.getSource()).getControl();
		// Getting dimensions of this widget
		Point compositeSize = dragger.getSize();
		// creating new GC
		GC gc = new GC(dragger);
		// Creating new Image
		Image image = new Image(Display.getCurrent(), compositeSize.x,
			compositeSize.y);
		// Rendering widget to image
		gc.copyArea(image, 0, 0);
		// Setting widget to DnD image
		dragSourceEvent.image = image;
		gc.dispose();
		*/
	    }

	    @Override
	    public void dragFinished(DragSourceEvent dragSourceEvent) {
		dragger = null;
	    }

	    @Override
	    public void dragSetData(DragSourceEvent dragSourceEvent) {
	    }
	});
    }

    private void calculateState() {
	if (contents.size() > 1) {
	    state = State.CONTESTED;
	} else if (contents.size() == 1) {
	    state = State.FILLED;
	} else {
	    state = State.EMPTY;
	}
    }

    private void paintControl(PaintEvent e) {

	Color backgroundColor = null;

	State stateTocheck;
	if (provisionalState != null) {
	    stateTocheck = provisionalState;
	} else {
	    stateTocheck = state;
	}

	switch (stateTocheck) {
	case EMPTY:
	    backgroundColor = Display.getCurrent().getSystemColor(
		    SWT.COLOR_WHITE);
	    break;
	case FILLED:
	    backgroundColor = Display.getCurrent().getSystemColor(
		    SWT.COLOR_GRAY);
	    break;
	case CONTESTED:
	    backgroundColor = Display.getCurrent()
		    .getSystemColor(SWT.COLOR_RED);
	    break;
	}
	e.gc.setBackground(backgroundColor);
	e.gc.fillRectangle(0, 0, getSize().x, getSize().y);

	if (contents.size() > 0) {
	    getContentOnTop().paint(e.gc);
	}
    }

}
