package dfs;

import java.util.ArrayList;
import java.util.List;

import common.DFileID;

public class DeDFS extends DFS {

	ArrayList<DFileID> myDFileIDs;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		myDFileIDs = new ArrayList<DFileID>();
	}

	@Override
	public DFileID createDFile() {
		// Write inode info in inode block
		return null;
	}

	@Override
	public void destroyDFile(DFileID dFID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int sizeDFile(DFileID dFID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DFileID> listAllDFiles() {
		return myDFileIDs;
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}

}
