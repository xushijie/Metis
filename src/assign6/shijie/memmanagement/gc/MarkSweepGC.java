package assign6.shijie.memmanagement.gc;

public class MarkSweepGC extends BaseGCAction{
	
	public MarkSweepGC(){
		_actions.add(new Marker(GCController.getWorkings()));
		_actions.add(new Sweeper(GCController.getWorkings()));
		_actions.add(new PostRunner(GCController.getWorkings()));
	}

}
