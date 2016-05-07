package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import dndblocks.DnDContent;
import dndblocks.DnDContentText;
import dndblocks.DnDGrid;
import dndblocks.DnDStructure;

public class DnDBlocksTest {

    public static void main(String[] args) {

	Shell shell = new Shell();
	shell.setSize(550, 300);
	shell.setText("DnDGrid test");
	shell.setLayout(new FillLayout(SWT.VERTICAL));

	DnDGrid dndGrid = new DnDGrid(shell, 0, 15, 4, 18);

	new DnDContentText(dndGrid.getCell(12, 1), "12345");
	new DnDContentText(dndGrid.getCell(13, 1), "Q");
	new DnDContentText(dndGrid.getCell(10, 1), "W");
	DnDContent x = new DnDContentText(dndGrid.getCell(11, 1), "H");
	DnDContent xx = new DnDContentText(dndGrid.getCell(11, 1), "X");
	DnDContent w = new DnDContentText(dndGrid.getCell(12, 1), "I");
	DnDContent n11 = new DnDContentText(dndGrid.getCell(13, 1), "!");

	DnDStructure myStructure = new DnDStructure();
	myStructure.add(w);
	myStructure.add(n11);
	myStructure.add(x);
	myStructure.add(xx);

	DnDGrid dndGrid2 = new DnDGrid(shell, 0, 15, 4, 26);

	new DnDContentText(dndGrid2.getCell(12, 1), "12345");
	new DnDContentText(dndGrid2.getCell(13, 1), "Q");
	new DnDContentText(dndGrid2.getCell(10, 1), "W");
	DnDContent x2 = new DnDContentText(dndGrid2.getCell(11, 1), "H");
	DnDContent xx2 = new DnDContentText(dndGrid2.getCell(11, 1), "X");
	DnDContent w2 = new DnDContentText(dndGrid2.getCell(12, 1), "I");
	DnDContent n112 = new DnDContentText(dndGrid2.getCell(13, 1), "!");

	DnDStructure myStructure2 = new DnDStructure();
	myStructure2.add(w2);
	myStructure2.add(n112);
	myStructure2.add(x2);
	myStructure2.add(xx2);

	DnDStructure.structureFromText("Article", 10, dndGrid2, 0, 0);

	shell.open();
	while (!shell.isDisposed()) {
	    if (!shell.getDisplay().readAndDispatch()) {
		shell.getDisplay().sleep();
	    }
	}
    }

}
