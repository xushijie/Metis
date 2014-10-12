package assign4.shijie.memmanagement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import assign4.shijie.memmanagement.Action;
import assign4.shijie.memmanagement.IInstruction;

public abstract class BaseInst implements IInstruction {

	private String _instruction;
	//
	protected int _threadId;
	protected int _objectId;
	protected int _payLoad;
	protected int _pointers;
	
	
	protected Action _action;
	
	
	//Below are for instruction RESET
	protected int _refIndex;
	protected int _sourceId;
	// protected int _targetId; reused objectid
	
	
	private static Map<String, Action> constmap = new LinkedHashMap<String, Action>();
	
	static {
		constmap.put("a", Action.ALLOCATE);
		constmap.put("r", Action.RESET);
		constmap.put("+", Action.PLUS);
		constmap.put("-", Action.SUBTRACT);
	}
	
	
	public BaseInst(String line){
		_instruction = line;
		StringTokenizer tokens = new StringTokenizer(line.trim());
		while(tokens.hasMoreElements()){
			String str = tokens.nextToken();
			if(str.startsWith(IInstruction.THREAD)){
				_threadId = Integer.parseInt(str.substring(1));
			}else if(str.startsWith(IInstruction.OBJECT)){
				_objectId = Integer.parseInt(str.substring(1)); 
			}else if(str.startsWith(IInstruction.PAYLOAD)){
				_payLoad = Integer.parseInt(str.substring(1)); 
			}else if(str.startsWith(IInstruction.POINTS)){
				_pointers = Integer.parseInt(str.substring(1)); 
			}else if(str.startsWith(IInstruction.INDEXID)){
				_refIndex = Integer.parseInt(str.substring(1));
			}else if(str.startsWith(IInstruction.SOURCEID)){
				_sourceId = Integer.parseInt(str.substring(1));
			}else if(constmap.keySet().contains(str.trim().toLowerCase())){
				_action =  constmap.get(str.toLowerCase());
			}
			
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		execute();
	}

	protected abstract void execute();
	
	@Override
	public Action getAction() {
		return _action;
	}
	
	@Override 
	public String toString(){
		return _instruction;
	}

}
