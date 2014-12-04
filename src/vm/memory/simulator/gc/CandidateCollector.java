package vm.memory.simulator.gc;

public class CandidateCollector implements IGCAction {

	@Override
	public void run() {
		Candidates.get().collect();
	}

}
