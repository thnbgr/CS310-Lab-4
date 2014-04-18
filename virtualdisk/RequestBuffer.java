package virtualdisk;

import common.Constants.DiskOperationType;

import dblockcache.DBuffer;

public class RequestBuffer {
	
	DBuffer mybuff;
	DiskOperationType myOperation;

	public RequestBuffer(DBuffer buf, DiskOperationType operation) {
		// TODO Auto-generated constructor stub
		
		mybuff = buf;
		myOperation = operation;
	}
	
}
