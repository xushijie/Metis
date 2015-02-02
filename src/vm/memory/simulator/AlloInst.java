package vm.memory.simulator;

import vm.memory.simulator.smartallocation.SmartAgent;

public class AlloInst extends BaseInst{

	public static int _count = 0;
	public static String _inst = "";
	
	public AlloInst(String line, int pc){
		super(line, pc);
	}

	@Override
	public void execute() {
		Heap heap = Heap.getHeap();
		int size =  _pointers;
		//int size = _payLoad + 4 * _pointers;
		
		// =======================================
		_count ++; 
		_inst = toString();
		// =======================================	
		if(SmartAgent.getAgent().isProfilePhase()){
			if(heap.allocate(size, _payLoad, _pointers, _objectId, _threadId, this) ==-1){
				System.err.print("Out of Memory: "+ this.toString()+"   "+heap.toString());
				System.exit(-1);
			}
		}else{
			if(heap.allocate(this)==-1){
				System.err.print("Out of Memory: "+ this.toString()+"   "+heap.toString());
				System.exit(-1);
			}
		}
		
	}
}
