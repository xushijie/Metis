package assign6.shijie.memmanagement.gc;

public class CandidateMarker implements IGCAction {

	@Override
	public void run() {
		Candidates.get().marker();
	}

}
