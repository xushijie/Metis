package assign6.shijie.memmanagement;

import assign6.shijie.memmanagement.smartallocation.SmartAgent;

public class JmpInst extends BaseInst {

	int count =0;
	
	public JmpInst(String line, int pc) {
		super(line, pc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute() {
		try {
			if(SmartAgent.getAgent().isProfilePhase()){
				_loader.gotoAddress(_pc);
			}
			
			if(count++>=1){
				SmartAgent.getAgent().activate();
			}
			
			System.out.println("This is "+ (SmartAgent.getAgent().isProfilePhase()?"Profile":"Activation"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
