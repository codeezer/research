package us.bsu.cs;

public class CASEIMPL {
	public static String ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	
	public static void main(String[] args) {
		OWLAPI owlAPI = new OWLAPI(ONT_FILE);
		owlAPI.startReasoner();
		owlAPI.isConsistent();
		
		CHEMSIM chemSim = new CHEMSIM();
		chemSim.simulateControlLogic();
	}
}