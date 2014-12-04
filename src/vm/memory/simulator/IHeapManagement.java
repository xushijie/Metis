package vm.memory.simulator;

public interface IHeapManagement {
	public int allocate(int numBytes);
	public boolean free(int address, int size);
	public void stats();
}
