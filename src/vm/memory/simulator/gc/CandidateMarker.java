package vm.memory.simulator.gc;

public class CandidateMarker implements IGCAction {

	@Override
	public void run() {
		Candidates.get().marker();
	}

}
