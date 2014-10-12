package assign4.shijie.memmanagement.gc;

import java.util.Set;

import assign4.shijie.memmanagement.ObjectNode;

public class PostRunner implements IGCAction {

	Set<ObjectNode> _workings;
	
	public PostRunner(Set<ObjectNode> workings){
		_workings = workings;
	}
	
	@Override
	public void run() {
		_workings.clear();
	}

}
