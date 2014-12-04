package assign6.shijie.memmanagement;

public class PlusInst  extends BaseInst{

	
	public PlusInst(String line, int pc){
		/*+ T3 O6*/
		super(line, pc);
	}	
	
	@Override
	protected void execute() {
		MyThread thread = ThreadPool.getThread(_threadId);
		Node obj = Heap.getHeap().getObject(_objectId);
		thread.addObj2Set(obj);
	}

}
