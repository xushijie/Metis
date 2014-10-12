package assign4.shijie.memmanagement;

public class SubInst extends BaseInst{
	
	public SubInst(String line){
		super(line);
	}

	@Override
	protected void execute() {
		MyThread thread = ThreadPool.getThread(_threadId);
		Node obj = Heap.getHeap().getObject(_objectId);
		thread.removeRootSet(obj);
	}

}
