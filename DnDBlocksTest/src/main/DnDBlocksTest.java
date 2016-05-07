package main;

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
	shell.setLayout(new FillLayout());

	DnDGrid dndGrid = new DnDGrid(shell, 0, 15, 4);

	new DnDContentText(dndGrid.getCell(12, 1), "12345");
	new DnDContentText(dndGrid.getCell(13, 1), "Q");
	new DnDContentText(dndGrid.getCell(10, 1), "W");
	DnDContent x = new DnDContentText(dndGrid.getCell(11, 1), "H");
	DnDContent x2 = new DnDContentText(dndGrid.getCell(11, 1), "X");
	DnDContent w = new DnDContentText(dndGrid.getCell(12, 1), "I");
	DnDContent n11 = new DnDContentText(dndGrid.getCell(13, 1), "!");

	DnDStructure myStructure = new DnDStructure();
	myStructure.add(w);
	myStructure.add(n11);
	myStructure.add(x);
	myStructure.add(x2);

	shell.open();
	while (!shell.isDisposed()) {
	    if (!shell.getDisplay().readAndDispatch()) {
		shell.getDisplay().sleep();
	    }
	}
    }

}
