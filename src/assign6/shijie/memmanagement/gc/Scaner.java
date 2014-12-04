package assign6.shijie.memmanagement.gc;

public class Scaner implements IGCAction {

	@Override
	public void run() {
		Candidates.get().scaner();
	}

}
