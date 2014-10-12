package assign4.shijie.memmanagement;

public interface IHeapManagement {
	public int allocate(int numBytes);
	public boolean free(int address, int size);
	public void stats();
}
