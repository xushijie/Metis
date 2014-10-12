package assign4.shijie.memmanagement;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import assign4.shijie.memmanagement.gc.GCController;

public class Heap implements IHeapManagement{

	static int _size;
	
	static int _gcReduced;
	static int _occupiedSize;
	static double _p;  // increase heap by fraction p
	static double _t;  //leave more than the fraction t with live object, increase heap size by p.
	
	
	List<Node> _freeList = new LinkedList<Node>();
	Map<Integer, Node> _workingNodes = new LinkedHashMap<Integer, Node>();
	
	
	private static  Heap _instance = null;
	
	private Heap(int size){
		_size = size;
		_freeList.add(new HeapNode(0,size));
	}

	private Heap(){
		_size = 10000;
		_freeList.add(new HeapNode(0,_size));
	}
	
	
	/**
	 *  Increase Current heap size _size*p. 
	 *  Add it to the tail of freelist and merge if necessary
	 */
	private void increaseHeap(){
		int increasedSize =  (int) (_size * _p);
		
		Node lastNode = null;
		if(_freeList.size() !=0) lastNode = _freeList.get(_freeList.size()-1);
		else {
			_freeList.add(new HeapNode(_size, increasedSize));	
			return;
		}
		
		if(lastNode.getStartAddress() + lastNode.getLength() == _size){
			
			lastNode.increaseLength(increasedSize);
		}else{
			_freeList.add(new HeapNode(_size, increasedSize));
		}
		_size = _size+increasedSize;
	}
	
	
	
	
	public static synchronized Heap getHeap(){
		if(_instance == null ){
			_instance = new Heap(_size);
		}
		return _instance;
	}

	public static void init(int size, double p, double t) {
		_size = size;		
		_p = p;
		_t = t;
		
	}
	
	/////////////////////////////////////////////
	
	public int allocate(int numBytes, int payout, int referencesCount, int id, int threadId){
		int address = allocate(numBytes);
		if(address ==-1){
			//Here cache _occuripedSize since GC might modify this value. 
			int gcBefore = _occupiedSize;
			int size = this._workingNodes.size();
			
			GCController.getGCController().run();
			System.out.println("Inst number: "+ AlloInst._count+" Instruction: "+ AlloInst._inst
					           +" heap size: " + _size + " live_Object_size_before GC: " + gcBefore 
						       +" live_object_size_after_gc: " + (_occupiedSize));
			/* I need to confirm whether the critical understanding..*/
			if(	1.0* _occupiedSize/_size > _t){
				increaseHeap();
			}
			
			address = allocate(numBytes);
			if(address == -1 ) return -1;
		}
		
		Node node = new ObjectNode(ThreadPool.getThread(threadId), payout,referencesCount,
					id, address);
		_workingNodes.put(node.getId(), node);
		_occupiedSize+=node.getLength();
		
		return address;
		
	}
	
	public boolean free(int id){
		Node node = _workingNodes.remove(id);
		_occupiedSize -= node.getLength();
		
		return free(node.getStartAddress(), node.getLength());
	}
	
	/**
	 * Return true if (address, size) can be merged into existing freeList 
	 */
	@Override
	public boolean free(int address, int size){
		int i=0;
		for(i=0; i< _freeList.size(); i++){
			Node node = _freeList.get(i);
			
			//Pre-Merge case
			if(node.getStartAddress()+node.getLength() == address){
				node.increaseLength(size);
				if(i!=_freeList.size()-1 && node.getStartAddress()+node.getLength() == _freeList.get(i+1).getStartAddress()){
					node.increaseLength(_freeList.get(i+1).getLength());
					_freeList.remove(i+1);
				}
				return true;
			}else if(address+size==node.getStartAddress()){
				//After-merge case
				node.resetAddress(size);
				return true;
			}
			
			if(node.getStartAddress()+node.getLength()< address ) {
				continue;	
			}
			
			if(node.getStartAddress()> address+size) break;
		}
		
		if(i==_freeList.size()){
			_freeList.add(new HeapNode(address, size));
			
		}else{
			_freeList.add(i, new HeapNode(address, size));
		}
	
		return false;
	}

	
	@Override
	public int allocate(int numBytes) {
		Iterator<Node> iter = _freeList.iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			if(node.getLength()>numBytes){
				int address = node.getStartAddress();
				node.allocate(numBytes);
				return address;
			}else if(node.getLength() == numBytes){
				int address = node.getStartAddress();
				iter.remove();
				return address;
			}
			
		}
		return -1;
	}

	@Override
	public void stats() {
		int used = 0;
		for(Node node: _workingNodes.values()){
			used += node.getLength();
		}
		
		double percentage = 100.0*used/_size;
		System.out.println("Used heap pencentage "+percentage+"%");
		System.out.println("Number in the free list: "+ _freeList.size());
		System.out.println("Average size in free list: "+ (1.0*(_size-used)/_freeList.size()));
		
	}

	public Node getObject(int objectId) {
		return _workingNodes.get(objectId);
	}

	public void gc(Set<ObjectNode> nodes) {
		
		Iterator<Integer> iter = _workingNodes.keySet().iterator();
		while(iter.hasNext()){
			Integer key = iter.next();
			Node node =  _workingNodes.get(key);
			if(!nodes.contains(node)){
				//System.out.println("GC object id: "+ node.getId()+ " size: "+ node.getLength());
				iter.remove();
				_occupiedSize -= node.getLength();
				free(node.getStartAddress(), node.getLength());
			}
		}
	}
}
