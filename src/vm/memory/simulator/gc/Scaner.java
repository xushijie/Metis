package vm.memory.simulator.gc;

public class Scaner implements IGCAction {

	@Override
	public void run() {
		Candidates.get().scaner();
	}

}
