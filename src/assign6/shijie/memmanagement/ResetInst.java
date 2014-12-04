package assign6.shijie.memmanagement;

public class ResetInst extends BaseInst {
	
	public ResetInst(String line, int pc){
		super(line, pc);
	}

	@Override
	protected void execute() {
		ObjectNode source = (ObjectNode)Heap.getHeap().getObject(_sourceId);
		ObjectNode target = (ObjectNode)Heap.getHeap().getObject(_objectId);
		source.setReference(_refIndex, target);
		
	}
}