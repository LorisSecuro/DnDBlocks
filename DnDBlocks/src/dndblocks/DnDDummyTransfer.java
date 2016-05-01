package dndblocks;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class DnDDummyTransfer extends ByteArrayTransfer {

    private static final String MYTYPENAME = "my_type_name";
    private static final int MYTYPEID = registerType(MYTYPENAME);

    private static DnDDummyTransfer _instance = new DnDDummyTransfer();

    private DnDDummyTransfer() {
    }

    public static DnDDummyTransfer getInstance() {
	return _instance;
    }

    @Override
    public void javaToNative(Object object, TransferData transferData) {
	return;
    }

    @Override
    public Object nativeToJava(TransferData transferData) {
	return null;
    }

    @Override
    protected int[] getTypeIds() {
	return new int[] { MYTYPEID };
    }

    @Override
    protected String[] getTypeNames() {
	return new String[] { MYTYPENAME };
    }

}
