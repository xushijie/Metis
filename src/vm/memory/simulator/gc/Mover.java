package vm.memory.simulator.gc;

import java.util.LinkedHashSet;
import java.util.Set;

import vm.memory.simulator.Heap;
import vm.memory.simulator.ObjectNode;

public class Mover implements IGCAction {

	Set<ObjectNode> _set = new LinkedHashSet<ObjectNode>();
	
	public Mover(Set<ObjectNode> set){
		_set = set;
	}
	
	@Override
	public void run() {
		Heap.getHeap().gc(_set);
	}

}
