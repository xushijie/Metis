package vm.memory.simulator;

import vm.memory.simulator.smartallocation.SmartAgent;

public class JmpInst extends BaseInst {

	int count =0;
	
	public JmpInst(String line, int pc) {
		super(line, pc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute() {
		try {
			if(SmartAgent.getAgent().isProfilePhase() && count <1){
				_loader.gotoAddress(_goto);
			}
			
			System.out.println("This is "+ (SmartAgent.getAgent().isProfilePhase()?"Profile":"Activation"));	
			count++;
			if( count ==1){
				SmartAgent.getAgent().activate();
				Heap.getHeap().clear();
			}
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
