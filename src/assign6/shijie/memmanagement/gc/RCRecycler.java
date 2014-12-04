package assign6.shijie.memmanagement.gc;

public class RCRecycler extends BaseGCAction{

	public RCRecycler(){
		_actions.add(new CandidateMarker());
		_actions.add(new Scaner());
		_actions.add(new CandidateCollector());
	}

}
