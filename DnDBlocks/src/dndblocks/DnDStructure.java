package dndblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Point;

public class DnDStructure {

    private Map<DnDContent, Point> distancesFromFirst = new HashMap<DnDContent, Point>();
    private List<DnDContent> contents = new ArrayList<DnDContent>();

    public DnDStructure() {

    }

    public void add(DnDContent content) {

	if (!contents.isEmpty()) {
	    DnDCell cellOfNewContent = content.getCell();
	    DnDCell cellOfFirst = contents.get(0).getCell();
	    Point distanceFromFirst = DnDStructure.getDistance(cellOfFirst,
		    cellOfNewContent);

	    distancesFromFirst.put(content, distanceFromFirst);
	}

	contents.add(content);
	content.setStructure(this);
    }

    public List<DnDContent> getContents() {
	return contents;
    }

    public List<DnDCell> getStructureCellsShiftedBy(DnDCell shifted,
	    DnDCell shifter) {
	Point shift = DnDStructure.getDistance(shifted, shifter);

	List<DnDCell> structureCellsShifted = new ArrayList<DnDCell>();
	Point positionOfFirst = getPositionOfFirst();
	DnDCell shiftedCell = shifter.getGrid().getCell(
		positionOfFirst.x + shift.x, positionOfFirst.y + shift.y);
	if (shiftedCell == null) {
	    return null;
	}
	structureCellsShifted.add(shiftedCell);
	for (Point distance : distancesFromFirst.values()) {
	    shiftedCell = shifter.getGrid().getCell(
		    positionOfFirst.x + distance.x + shift.x,
		    positionOfFirst.y + distance.y + shift.y);
	    if (shiftedCell == null) {
		return null;
	    }
	    structureCellsShifted.add(shiftedCell);
	}

	return structureCellsShifted;
    }

    public Map<DnDCell, DnDContent> getStructureCellsShiftedAndCorrelatedContent(
	    DnDCell shifted, DnDCell shifter) {
	Point shift = DnDStructure.getDistance(shifted, shifter);

	Map<DnDCell, DnDContent> structureCellsShiftedAndCorrelatedContent = new HashMap<DnDCell, DnDContent>();
	Point positionOfFirst = getPositionOfFirst();
	DnDCell shiftedCell = shifter.getGrid().getCell(
		positionOfFirst.x + shift.x, positionOfFirst.y + shift.y);
	if (shiftedCell == null) {
	    return null;
	}
	structureCellsShiftedAndCorrelatedContent.put(shiftedCell,
		contents.get(0));

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
		return null;
	    }
	    structureCellsShiftedAndCorrelatedContent.put(shiftedCell,
		    contentWithDistanceFromFirst.getKey());
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

    private Point getPositionOfFirst() {
	DnDContent first = contents.get(0);
	DnDCell cellOfFirst = first.getCell();
	DnDGrid gridOfFirst = cellOfFirst.getGrid();
	return gridOfFirst.getPosition(cellOfFirst);
    }

}
