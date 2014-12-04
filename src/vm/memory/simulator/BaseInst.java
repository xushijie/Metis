package vm.memory.simulator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import vm.memory.simulator.Action;
import vm.memory.simulator.IInstruction;
import vm.memory.simulator.VirtualMachine.InstructionLoader;
import vm.memory.simulator.smartallocation.PC;

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
	
	
	protected int _pc;
	protected InstructionLoader _loader = null;
	
	private static Map<String, Action> constmap = new LinkedHashMap<String, Action>();
	
	static {
		constmap.put("a", Action.ALLOCATE);
		constmap.put("r", Action.RESET);
		constmap.put("+", Action.PLUS);
		constmap.put("-", Action.SUBTRACT);
		constmap.put("j", Action.JMP);
	}
	
	
	private BaseInst(String line){
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
			}else if(_action == Action.JMP){
					_pc = Integer.parseInt(str);
			}
		}
	}
	
	public BaseInst(String line, int pc){
		
		this(line);
		if(pc==-1){
			//If it is not JMP instruction, then the _pc to the next instruction address.
			_pc = pc;
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
	public int getPC(){
		return _pc;
	}
	
	@Override 
	public String toString(){
		return _instruction;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof BaseInst){
			BaseInst inst = (BaseInst) obj;
			return this._instruction.toLowerCase().equals(inst._instruction.toLowerCase()) && this._pc == inst._pc;
		}
		return false;
	}
	
	public PC getProfilePC(){
		return new PC(this);
	}

	public void setLoader(InstructionLoader loader) {
		_loader = loader;
	}
}
