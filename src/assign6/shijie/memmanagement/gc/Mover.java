package assign6.shijie.memmanagement.gc;

import java.util.LinkedHashSet;
import java.util.Set;

import assign6.shijie.memmanagement.Heap;
import assign6.shijie.memmanagement.ObjectNode;

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
