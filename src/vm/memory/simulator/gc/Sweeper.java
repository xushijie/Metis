package vm.memory.simulator.gc;

import java.util.Set;

import vm.memory.simulator.Heap;
import vm.memory.simulator.ObjectNode;

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
