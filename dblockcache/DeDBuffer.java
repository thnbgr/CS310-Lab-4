package dblockcache;

public class DeDBuffer extends DBuffer {

	/*
	 * The Dbuffer maintains status information about the block, e.g., whether
	 * or not device I/O is in progress on the block. Other Dbuffer methods
	 * allow a thread to wait on a Dbuffer for pending I/O to complete.
	 */

	/* Have some sort of data structure to store metadata */

	private int myBlockID;
	private byte[] myBuffer;
	private boolean isDirty;
	private boolean isBusy;

	public DeDBuffer(int i) {
		myBlockID = i;
		isDirty = false;
		isBusy = false;
	}

	@Override
	public void startFetch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPush() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean waitValid() {
		// TODO Auto-generated method stub
		// calls virtualDisk fetch method, which will notify the following wait
		// wait
		return false;
	}

	@Override
	public boolean checkClean() {
		return !isDirty;
	}

	@Override
	public boolean waitClean() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int read(byte[] buffer, int startOffset, int count) {
		// check valid
		// check clean
		// check busy
		this.isBusy = false;
		// read data
		this.isBusy = true;
		return 0;
	}

	@Override
	public int write(byte[] buffer, int startOffset, int count) {
		// check valid
		// check clean
		// check busy
		this.isBusy = false;
		// write data
		this.isDirty = true;
		this.isBusy = true;
		return 0;
	}

	@Override
	/* called when an IO is completed by VirtualDisk (?) */
	public void ioComplete() {

	}

	@Override
	public int getBlockID() {
		return myBlockID;
	}
	
	public byte[] getBlockContents() {
		return null;
	}

	@Override
	/* 
	 * Simply returns a buffer to read into or write from
	 */
	public byte[] getBuffer() {
		return myBuffer;
	}

}
