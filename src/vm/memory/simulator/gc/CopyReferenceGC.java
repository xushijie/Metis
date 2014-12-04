package vm.memory.simulator.gc;

public class CopyReferenceGC extends BaseGCAction {

	public CopyReferenceGC(){
		_actions.add(new Marker(GCController.getWorkings()));
		_actions.add(new Mover(GCController.getWorkings()));
		_actions.add(new PostRunner(GCController.getWorkings()));
		
	}
}
