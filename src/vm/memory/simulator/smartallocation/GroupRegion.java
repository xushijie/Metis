package vm.memory.simulator.smartallocation;

import vm.memory.simulator.Heap;
import vm.memory.simulator.HeapNode;
import vm.memory.simulator.Node;
import vm.memory.simulator.ObjectNode;
import vm.memory.simulator.ThreadPool;

public class GroupRegion extends Heap {
	
	private int  _regionStart;
	private int  _regionSize;
	
	public GroupRegion(int start, int size){
		super(start, size, true);
		_isRegion = true;
		_regionStart = start;
		_regionSize = size;
		_freeList.add(new HeapNode(start, size));
	}
	
	/**
	 *  Allocate memory inside of one GroupRegion. 
	 *  Notice I disable GC triggering here..
	 *  We might have better solution for how to allocate here with some policy.
	 */
	@Override
	public int allocate(int numBytes, int payout, int referencesCount, int id, int threadId){
		int address = allocate(numBytes);
		if(address ==-1){
			//None of GC is required...
//			//Here cache _occuripedSize since GC might modify this value. 
//			int gcBefore = _occupiedSize;
//			int size = this._workingNodes.size();
//			
//			GCController.getGCController().run(_gcKind);
//			System.out.println("Inst number: "+ AlloInst._count+" Instruction: "+ AlloInst._inst
//					           +" heap size: " + _size + " live_Object_size_before GC: " + gcBefore 
//						       +" live_object_size_after_gc: " + (_occupiedSize));
//			/* I need to confirm whether the critical understanding..*/
//			if(isIncreaseHeapSize()){
//				increaseHeap();
//			}
//			
//			address = allocate(numBytes);
//			if(address == -1 ) return -1;
			return -1;
		}
		
		Node node = new ObjectNode(ThreadPool.getThread(threadId), payout,referencesCount,
					id, address);
		_workingNodes.put(node.getId(), node);
		_occupiedSize+=node.getLength();
		
		return address;
	}
	
	@Override
	public int getSize(){
		return _regionSize;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("start:"+_regionStart+" len:"+_regionSize);
		return builder.toString();
	}
}
