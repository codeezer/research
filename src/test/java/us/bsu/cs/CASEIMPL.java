package us.bsu.cs;

public class CASEIMPL {
	public static String CHEM_ONT_FILE = "/home/ezer/Documents/research/implementation/files/chemicalProcess.owl";
	public static String TEMP_ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	// public static String ONT_FILE = "/Users/ezer/Desktop/research/implementation/research/files/temporal.owl";
	
	public static void chemicalProcess() {
		OWLAPI owlAPI = new OWLAPI(CHEM_ONT_FILE);
		owlAPI.startReasoner();
		owlAPI.isConsistent();
		
		// owlAPI.addIndividual("Variable", "Test");
		// owlAPI.isConsistent();
		System.out.println("Mixer:");
		System.out.println("--should run for ");
		owlAPI.printDataPropertyValue("MixerO", "shouldRunFor");
		System.out.println("--should run together with");
		owlAPI.printObjectPropertyValue("MixerO", "shouldRunTogether");
		System.out.println("--should run before");
		owlAPI.printObjectPropertyValue("MixerO", "shouldRunBefore");
		System.out.println("--should run after");
		owlAPI.printObjectPropertyValue("MixerO", "shouldRunAfter");
	}
	
	public static void main(String[] args) {
		// chemicalProcess();
		OWLAPI owlAPI = new OWLAPI(TEMP_ONT_FILE);
		owlAPI.startReasoner();
		owlAPI.isConsistent();
		CHEMSIM chemSim = new CHEMSIM();
		chemSim.simulateControlLogic();
	}
}