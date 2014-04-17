package virtualdisk;

import java.io.FileNotFoundException;
import java.io.IOException;

import common.Constants.DiskOperationType;
import dblockcache.DBuffer;

public class DeVirtualDisk extends VirtualDisk {

	public DeVirtualDisk(String volName, boolean format)
			throws FileNotFoundException, IOException {
		super(volName, format);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startRequest(DBuffer buf, DiskOperationType operation)
			throws IllegalArgumentException, IOException {
		if (operation == DiskOperationType.READ) {
			int r = readBlock(buf);
		} else if (operation == DiskOperationType.WRITE) {
			writeBlock(buf);
		}
		
		// Somewhere this happens
		buf.ioComplete();
	}

}
