package vm.memory.simulator;

public class SubInst extends BaseInst{
	
	public SubInst(String line, int pc){
		super(line, pc);
	}

	@Override
	protected void execute() {
		MyThread thread = ThreadPool.getThread(_threadId);
		Node obj = Heap.getHeap().getObject(_objectId);
		thread.removeRootSet(obj);
	}

}
