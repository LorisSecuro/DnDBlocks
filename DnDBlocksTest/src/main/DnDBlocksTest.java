package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import dndblocks.DnDContent;
import dndblocks.DnDContentText;
import dndblocks.DnDGrid;
import dndblocks.DnDStructure;

public class DnDBlocksTest {

    static DnDStructure myStructure;
    static DnDStructure cross;
    static DnDStructure cube;
    static DnDStructure text1;
    static DnDStructure text2;

    static Button allowOutOfGrid;

    public static void main(String[] args) {

	Shell shell = new Shell();
	shell.setSize(600, 400);
	shell.setText("DnDGrid test");
	shell.setLayout(new FillLayout(SWT.VERTICAL));

	DnDGrid dndGrid = new DnDGrid(shell, 0, 30, 6, 18);

	new DnDContentText(dndGrid.getCell(12, 1), "12345");
	new DnDContentText(dndGrid.getCell(13, 1), "X");
	new DnDContentText(dndGrid.getCell(10, 1), "Y");
	DnDContent c1 = new DnDContentText(dndGrid.getCell(11, 1), "A");
	DnDContent c2 = new DnDContentText(dndGrid.getCell(11, 1), "A1");
	DnDContent c3 = new DnDContentText(dndGrid.getCell(12, 1), "B");
	DnDContent c4 = new DnDContentText(dndGrid.getCell(13, 1), "C");

	myStructure = new DnDStructure();
	myStructure.add(c1);
	myStructure.add(c2);
	myStructure.add(c3);
	myStructure.add(c4);

	DnDContent y1 = new DnDContentText(dndGrid.getCell(20, 1), "N");
	DnDContent y2 = new DnDContentText(dndGrid.getCell(20, 3), "+");
	DnDContent y3 = new DnDContentText(dndGrid.getCell(20, 5), "S");
	DnDContent y4 = new DnDContentText(dndGrid.getCell(18, 3), "W");
	DnDContent y5 = new DnDContentText(dndGrid.getCell(22, 3), "E");

	cross = new DnDStructure();
	cross.add(y1);
	cross.add(y2);
	cross.add(y3);
	cross.add(y4);
	cross.add(y5);

	DnDGrid dndGrid2 = new DnDGrid(shell, 0, 15, 4, 26);

	DnDContent x1 = new DnDContentText(dndGrid2.getCell(11, 1), "C");
	DnDContent x2 = new DnDContentText(dndGrid2.getCell(12, 1), "U");
	DnDContent x3 = new DnDContentText(dndGrid2.getCell(11, 2), "B");
	DnDContent x4 = new DnDContentText(dndGrid2.getCell(12, 2), "E");

	cube = new DnDStructure();
	cube.add(x1);
	cube.add(x2);
	cube.add(x3);
	cube.add(x4);

	text1 = DnDStructure.structureFromText("Test", 10, dndGrid2, 0, 0);
	text2 = DnDStructure.structureFromText("Test2", dndGrid2, -2, 1);

	allowOutOfGrid = new Button(shell, SWT.CHECK);
	allowOutOfGrid.setText("Allow out of grid");
	allowOutOfGrid.setSelection(true);
	allowOutOfGrid.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent event) {
		myStructure.setAllowOutOfGrid(allowOutOfGrid.getSelection());
		cross.setAllowOutOfGrid(allowOutOfGrid.getSelection());
		cube.setAllowOutOfGrid(allowOutOfGrid.getSelection());
		text1.setAllowOutOfGrid(allowOutOfGrid.getSelection());
		text2.setAllowOutOfGrid(allowOutOfGrid.getSelection());
	    }

	});

	shell.open();
	while (!shell.isDisposed()) {
	    if (!shell.getDisplay().readAndDispatch()) {
		shell.getDisplay().sleep();
	    }
	}
    }

}
