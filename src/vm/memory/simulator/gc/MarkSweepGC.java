package vm.memory.simulator.gc;

import vm.memory.simulator.smartallocation.SmartAgent;

public class MarkSweepGC extends BaseGCAction{
	
	static {
		SmartAgent.init(GCController.getWorkings());
	}
	
	public MarkSweepGC(){
		_actions.add(new Marker(GCController.getWorkings()));
		_actions.add(SmartAgent.getAgent());
		_actions.add(new Sweeper(GCController.getWorkings()));
		_actions.add(new PostRunner(GCController.getWorkings()));
	}

}
