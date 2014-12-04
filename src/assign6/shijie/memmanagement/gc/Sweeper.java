package assign6.shijie.memmanagement.gc;

import java.util.Set;

import assign6.shijie.memmanagement.Heap;
import assign6.shijie.memmanagement.ObjectNode;

public class Sweeper implements IGCAction {

	Set<ObjectNode> _nodes;
	
	public Sweeper(Set<ObjectNode> workingset){
		_nodes = workingset;
	}
	
	@Override
	public void run() {
		Heap heap = Heap.getHeap();
		heap.gc(_nodes);
	}

}
