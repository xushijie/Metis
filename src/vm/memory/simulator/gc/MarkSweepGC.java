package vm.memory.simulator.gc;

public class MarkSweepGC extends BaseGCAction{
	
	static{
	}
	public MarkSweepGC(){
		_actions.add(new Marker(GCController.getWorkings()));
		_actions.add(new Sweeper(GCController.getWorkings()));
		_actions.add(new PostRunner(GCController.getWorkings()));
	}

}
