package common;

/*
 * This class contains the global constants used in DFS
 */

public class Constants {

	/* The below constants indicate that we have approximately 268 MB of
	 * disk space with 67 MB of memory cache; a block can hold upto 32 inodes and
	 * the maximum file size is constrained to be 500 blocks. These are compile
	 * time constants and can be changed during evaluation.  Your implementation
	 * should be free of any hard-coded constants.  
	 */

	public static final int NUM_OF_BLOCKS = 262144; // 2^18
	public static final int BLOCK_SIZE = 2048; // 2kB (originally 1024)
	public static final int INODE_SIZE = 2004; //500*4Bytes + 4Bytes
	public static final int NUM_OF_CACHE_BLOCKS = 65536; // 2^16
	public static final int MAX_NUM_BLOCKS_PER_FILE = 500; // Constraint on the max file size
	public static final int MAX_FILE_SIZE = BLOCK_SIZE*MAX_NUM_BLOCKS_PER_FILE; // Constraint on the max file size
	public static final int MAX_DFILES = 512; // For recylcing DFileIDs
	
	// 1026048bytes for total size of inodes
	// 1002 blocks

	/* DStore Operation types */
	public enum DiskOperationType {
		READ, WRITE
	};

	/* Virtual disk file/store name */
	public static final String vdiskName = "DSTORE.dat";
}
