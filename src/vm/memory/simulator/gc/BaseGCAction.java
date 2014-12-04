package vm.memory.simulator.gc;

import java.util.LinkedList;
import java.util.List;

import vm.memory.simulator.gc.GCController.IGC;

public abstract class BaseGCAction implements IGC {
	protected List<IGCAction> _actions = new LinkedList<IGCAction>();
	
	@Override
	public void run() {
		for(IGCAction action : _actions){
			action.run();
		}
	}

}
