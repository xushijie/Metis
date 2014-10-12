package assign4.shijie.memmanagement;

public class AlloInst extends BaseInst{

	public static int _count = 0;
	public static String _inst = "";
	
	public AlloInst(String line){
		super(line);
	}

	@Override
	public void execute() {
		Heap heap = Heap.getHeap();
		int size = _payLoad + 4 * _pointers;
		
		// =======================================
		_count ++; 
		_inst = toString();
		// =======================================	
		
		if(heap.allocate(size, _payLoad, _pointers, _objectId, _threadId) ==-1){
			System.err.print("Out of Memory: "+ this.toString()+"   "+heap.toString());
			System.exit(-1);
		}
		
		
		
//		if(_count ==100){
//			heap.stats();
//		}
//		
	}
	
	

}
