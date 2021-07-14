package us.bsu.cs;

// import java.util.Calendar;

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
		// owlAPI.startReasoner();
		// owlAPI.printIfConsistent();
		// System.out.println("Before");
		// owlAPI.printDataPropertyValue("R1", "isValid");
		// owlAPI.printDataPropertyValue("R2", "isValid");
		// String S1 = "temporal:Pump(temporal:IntakePump1) ^ temporal:shouldRunFor(temporal:IntakePump1, ?vp) ^ temporal:ValidPeriod(?vp) ^ temporal:hasStartTime(?vp, ?st) ^ temporal:hasFinishTime(?vp, ?ft) ^ temporal:duration(2, ?st, ?ft, temporal:Seconds) -> temporal:isValid(temporal:R1, true)";
		// String S1 = "isValid(R1, false) -> isValid(R2, true)";
		// owlAPI.addSWRLRule("S1", S1);
		// owlAPI.startInferring();
		// String S1 = "temporal:Pump(temporal:IntakePump1) ^ temporal:shouldRunFor(temporal:IntakePump1, ?vp) ^ temporal:ValidPeriod(?vp) ^ temporal:hasStartTime(?vp, ?st) ^ temporal:hasFinishTime(?vp, ?ft) ^ temporal:duration(2, ?st, ?ft, temporal:Seconds) -> temporal:isValid(temporal:R1, true)";
		// String S2 = "temporal:Component(temporal:MixerO) ^ temporal:shouldRunFor(temporal:MixerO, ?vp) ^ temporal:ValidPeriod(?vp) ^ temporal:hasStartTime(?vp, ?st) ^ temporal:hasFinishTime(?vp, ?ft) ^ temporal:duration(2, ?st, ?ft, temporal:Seconds) -> temporal:isValid(temporal:R2, true)";
		// String S3 = "temporal:Pump(temporal:IntakePump1) ^ temporal:shouldRunFor(temporal:IntakePump1, ?vpi) ^ temporal:ValidPeriod(?vpi) ^ temporal:hasStartTime(?vpi, ?sti) ^ temporal:Mixer(temporal:MixerO) ^ temporal:shouldRunFor(temporal:MixerO, ?vp) ^ temporal:ValidPeriod(?vp) ^ temporal:hasStartTime(?vp, ?st) ^ temporal:before(?sti, ?st) -> temporal:isValid(temporal:R3, true)";
		// String S4 = "temporal:Pump(temporal:IntakePump1) ^ temporal:shouldRunFor(temporal:IntakePump1, ?vpi) ^ temporal:ValidPeriod(?vpi) ^ temporal:hasStartTime(?vpi, ?sti) ^ temporal:Mixer(temporal:MixerO) ^ temporal:shouldRunFor(temporal:MixerO, ?vp) ^ temporal:ValidPeriod(?vp) ^ temporal:hasStartTime(?vp, ?st) ^ temporal:after(?sti, ?st) -> temporal:isValid(temporal:R4, true)";
		// Calendar cal = Calendar.getInstance();
		// owlAPI.dataPropertyAssertion("VPIP1", "hasStartTime", cal);
		CHEMSIM chemSim = new CHEMSIM();
		chemSim.simulateControlLogic(owlAPI);
		
		owlAPI.saveOntology();
	}
}