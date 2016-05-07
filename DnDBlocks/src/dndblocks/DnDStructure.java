package dndblocks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Point;

public class DnDStructure {

    // LinkedHashMap because i want to preserve the order they are inserted, so if there are contents
    // in the same cell they get returned in order
    private Map<DnDContent, Point> distancesFromFirst = new LinkedHashMap<DnDContent, Point>();
    private List<DnDContent> contents = new ArrayList<DnDContent>();
    private boolean allowOutOfGrid = true;

    public DnDStructure() {

    }

    public void add(DnDContent content) {

	Point distanceFromFirst = new Point(0, 0);

	if (!contents.isEmpty()) {
	    DnDCell cellOfNewContent = content.getCell();
	    DnDCell cellOfFirst = contents.get(0).getCell();
	    distanceFromFirst = DnDStructure.getDistance(cellOfFirst,
		    cellOfNewContent);
	}

	add(content, distanceFromFirst);
    }

    public void add(DnDContent content, Point distanceFromFirst) {

	if (!contents.isEmpty()) {
	    distancesFromFirst.put(content, distanceFromFirst);
	}

	contents.add(content);
	content.setStructure(this);
    }

    public static DnDStructure structureFromText(String contents, int size,
	    DnDGrid grid, int xPosition, int yPosition) {
	DnDStructure structure = new DnDStructure();
	for (int i = 0; i < size; i++) {
	    String stringContent = "";
	    if (i < contents.length()) {
		stringContent = contents.substring(i, i + 1);
	    }
	    DnDCell targetCell = grid.getCell(xPosition, yPosition);
	    DnDContent content = new DnDContentText(targetCell, stringContent);
	    structure.add(content, new Point(i, 0));
	    xPosition++;
	}
	return structure;
    }

    public static DnDStructure structureFromText(String contents, DnDGrid grid,
	    int xPosition, int yPosition) {
	return structureFromText(contents, contents.length(), grid, xPosition,
		yPosition);
    }

    public boolean getAllowOutOfGrid() {
	return allowOutOfGrid;
    }

    public void setAllowOutOfGrid(boolean allowOutOfGrid) {
	this.allowOutOfGrid = allowOutOfGrid;
    }

    public List<DnDContent> getContents() {
	return contents;
    }

    public List<DnDCell> getStructureCellsShiftedBy(DnDContent shiftedContent,
	    DnDCell shifter) {
	DnDCell shifted = shiftedContent.getCell();
	Point shift = DnDStructure.getDistance(shifted, shifter);

	List<DnDCell> structureCellsShifted = new ArrayList<DnDCell>();
	Point positionOfFirst = getPositionOfFirst(shiftedContent);
	DnDCell shiftedCell = shifter.getGrid().getCell(
		positionOfFirst.x + shift.x, positionOfFirst.y + shift.y);

	if (shiftedCell == null) {
	    if (!allowOutOfGrid) {
		return null;
	    }
	} else {
	    structureCellsShifted.add(shiftedCell);
	}
	for (Point distance : distancesFromFirst.values()) {
	    shiftedCell = shifter.getGrid().getCell(
		    positionOfFirst.x + distance.x + shift.x,
		    positionOfFirst.y + distance.y + shift.y);
	    if (shiftedCell == null) {
		if (!allowOutOfGrid) {
		    return null;
		}
	    } else {
		structureCellsShifted.add(shiftedCell);
	    }
	}

	return structureCellsShifted;
    }

    public Map<DnDCell, List<DnDContent>> getStructureCellsShiftedAndCorrelatedContent(
	    DnDContent shiftedContent, DnDCell shifter) {
	DnDCell shifted = shiftedContent.getCell();

	Point shift = DnDStructure.getDistance(shifted, shifter);

	Map<DnDCell, List<DnDContent>> structureCellsShiftedAndCorrelatedContent = new LinkedHashMap<DnDCell, List<DnDContent>>();
	Point positionOfFirst = getPositionOfFirst(shiftedContent);
	DnDCell shiftedCell = shifter.getGrid().getCell(
		positionOfFirst.x + shift.x, positionOfFirst.y + shift.y);
	if (shiftedCell == null) {
	    if (!allowOutOfGrid) {
		return null;
	    }
	}
	List<DnDContent> contentsInKey = structureCellsShiftedAndCorrelatedContent
		.get(shiftedCell);
	if (contentsInKey == null) {
	    contentsInKey = new ArrayList<DnDContent>();
	}
	contentsInKey.add(contents.get(0));
	structureCellsShiftedAndCorrelatedContent.put(shiftedCell,
		contentsInKey);

	for (Map.Entry<DnDContent, Point> contentWithDistanceFromFirst : distancesFromFirst
		.entrySet()) {
	    shiftedCell = shifter.getGrid().getCell(
		    positionOfFirst.x
			    + contentWithDistanceFromFirst.getValue().x
			    + shift.x,
		    positionOfFirst.y
			    + contentWithDistanceFromFirst.getValue().y
			    + shift.y);
	    if (shiftedCell == null) {
		if (!allowOutOfGrid) {
		    return null;
		}
	    }
	    contentsInKey = structureCellsShiftedAndCorrelatedContent
		    .get(shiftedCell);
	    if (contentsInKey == null) {
		contentsInKey = new ArrayList<DnDContent>();
	    }
	    contentsInKey.add(contentWithDistanceFromFirst.getKey());
	    structureCellsShiftedAndCorrelatedContent.put(shiftedCell,
		    contentsInKey);
	}

	return structureCellsShiftedAndCorrelatedContent;
    }

    private static Point getDistance(DnDCell from, DnDCell to) {
	DnDGrid fromGrid = from.getGrid();
	Point fromPosition = fromGrid.getPosition(from);

	DnDGrid toGrid = to.getGrid();
	Point toPosition = toGrid.getPosition(to);

	return new Point(toPosition.x - fromPosition.x, toPosition.y
		- fromPosition.y);
    }

    // we don't know if first is in a cell, so the method require a reference cell that surely is in a cell
    private Point getPositionOfFirst(DnDContent reference) {
	DnDCell referenceCell = reference.getCell();
	DnDGrid referenceGrid = referenceCell.getGrid();
	Point referencePosition = referenceGrid.getPosition(referenceCell);
	// if the reference is the first
	if (reference == contents.get(0)) {
	    return referencePosition;
	}
	Point distanceFromFirst = distancesFromFirst.get(reference);
	Point positionOfFirst = new Point(referencePosition.x
		- distanceFromFirst.x, referencePosition.y
		- distanceFromFirst.y);
	return positionOfFirst;
    }
}
