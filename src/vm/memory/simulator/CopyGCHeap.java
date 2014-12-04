package vm.memory.simulator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CopyGCHeap extends Heap {

	List<Node> _backSpace = new LinkedList<Node>();
	
	protected CopyGCHeap(){
		super();
		_freeList = new LinkedList<Node>();
		_freeList.add(new HeapNode(0, _size/2));
		_backSpace.add(new HeapNode(_size/2, _size/2));
		
	}
	
	protected boolean isIncreaseHeapSize(){
		return 1.0* _occupiedSize*2/_size > _t;
	}
	
	private void increaseHeap(){
		int increasedSize =  (int) (_size * _p)/2;
		
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
		_backSpace.get(0).increaseLength(increasedSize);
		_size = _size+increasedSize*2;
	}
	
	
	public void gc(Set<ObjectNode> nodes) {
		
		Iterator<Integer> iter = _workingNodes.keySet().iterator();
		_occupiedSize = 0 ;
		int start = _backSpace.get(0).getStartAddress();
		int backStart =  (start==0 ? _size/2: 0);
		//1, Copying
		while(iter.hasNext()){
			Integer key = iter.next();
			Node node =  _workingNodes.get(key);
			if(!nodes.contains(node)){
				iter.remove();

			}else{
				/*1, Copy node */
				node.setAddress(start);
				start+=node.getLength();
				_occupiedSize += node.getLength();
				
			}
		}
		
		//2, Free all _freeList
		_freeList.clear();
		_freeList.add(new HeapNode(start, _size/2 - _occupiedSize));
		
//		//2, Flip
//		List<Node> temp = _freeList;
//		_freeList = _backSpace;
//		_backSpace= temp;

	}

}
