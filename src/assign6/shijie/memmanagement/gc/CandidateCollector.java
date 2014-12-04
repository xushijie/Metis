package assign6.shijie.memmanagement.gc;

public class CandidateCollector implements IGCAction {

	@Override
	public void run() {
		Candidates.get().collect();
	}

}
