package us.bsu.cs;

public class CASEIMPL {
	// public static String ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	public static String ONT_FILE = "/Users/ezer/Desktop/research/implementation/research/files/temporal.owl";
	
	public static void main(String[] args) {
		// OWLAPI owlAPI = new OWLAPI(ONT_FILE);
		// owlAPI.startReasoner();
		// owlAPI.isConsistent();
		
		// owlAPI.addIndividual("Variable", "Test");
		// owlAPI.isConsistent();
		// owlAPI.printDataPropertyValue("MixerO", "shouldRunFor");
		// owlAPI.printClasses();
		CHEMSIM chemSim = new CHEMSIM();
		chemSim.simulateControlLogic();
	}
}