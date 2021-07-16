package us.bsu.cs;

import java.util.Random;

import org.mvel2.sh.command.basic.Exit;

public class CHEMSIM {
	int RunIntakePump1, RunIntakePump2, RunIntakePump3, RunOuttakePump, RunMixer, RunEmergencyFlush;
	boolean RunProcess, EmergencyStop;
	String[] variablesName = {"RunIntakePump1", "RunIntakePump2", "RunIntakePump3", "RunOuttakePump", "RunMixer", "RunEmergencyFlush"};
	String[] componentsName = {"IntakePump1", "IntakePump2", "IntakePump3", "OuttakePump", "MixerO", "EmergencyFlush"};
	int[] componentsStatus = {0, 0, 0, 0, 0, 0};
	
	
	public CHEMSIM() {
		setComponentsStatus();
	}

	public void setComponentsStatus() {
		 componentsStatus[0] = RunIntakePump1;
		 componentsStatus[1] = RunIntakePump2;
		 componentsStatus[2] = RunIntakePump3;
		 componentsStatus[3] = RunOuttakePump;
		 componentsStatus[4] = RunMixer;
		 componentsStatus[5] = RunEmergencyFlush;
	}
	
	
	public void printComponentsStatus(String title) {
		System.out.println("-----" + title + "-----");
		for (int i = 0; i < variablesName.length; i++) {
			System.out.println(variablesName[i] + ": " + componentsStatus[i]);
		}
		System.out.println("----------------------");
	}
	
	public void updateComponentsStatusToOntology(OWLAPI owlAPI) {
		for (int i = 0; i < componentsName.length; i++) {
			// System.out.println(componentsName[i] + ": " + "isRunning" + "=" + (componentsStatus[i]==1));
			if (owlAPI.loadDataPropertyValue(componentsName[i], "isRunning").size()==1) {
				owlAPI.removeDataProperty(componentsName[i], "isRunning", Boolean.parseBoolean(owlAPI.loadDataPropertyValue(componentsName[i], "isRunning").get(0).getLiteral()));
			}
			owlAPI.dataPropertyAssertion(componentsName[i], "isRunning", componentsStatus[i]==1);
		}
	}
	
	
	public void injectMixerAttack() {
		RunMixer = 1;
	}
	
	public void simulateControlLogic(OWLAPI owlAPI) {
		Random random = new Random();
		
		RunIntakePump1 = 0; RunIntakePump2 = 0; RunIntakePump3 = 0;
		RunOuttakePump = 0; RunMixer = 0; RunEmergencyFlush = 0;
		
		RunProcess = random.nextBoolean();
		EmergencyStop = random.nextBoolean();
		
		int N = 50;
		// int attackTime = random.nextInt(N);
		int attackTime = 0;
		
		int emergencyFlag = 0;
		int runFlag = 0;
		
		for (int i = 0; i < N; i++) {
			// RunProcess = random.nextBoolean();
			// EmergencyStop = random.nextBoolean();
			if (i==attackTime) {
				System.out.println("Attack injected at time: " + attackTime);
				injectMixerAttack();
			}
			
			if (EmergencyStop) {
				RunIntakePump1 = 0; RunIntakePump2 = 0; RunIntakePump3 = 0;
				RunOuttakePump = 0; RunMixer = 0; RunEmergencyFlush = 0;
				RunEmergencyFlush = 1; 
				runFlag = 0;
				
				emergencyFlag++;
				if (emergencyFlag>=5) {
					RunEmergencyFlush = 0;
				}
			}
			else if (RunProcess) {
				RunEmergencyFlush = 0; emergencyFlag = 0;
				
				if (runFlag==0) {
					RunIntakePump1 = 1;
				} else if (runFlag==2) {
					RunIntakePump1 = 0;
					RunIntakePump2 = 1;
					RunIntakePump3 = 1;
					RunMixer = 1;
				} else if (runFlag==6) {
					RunIntakePump2 = 0;
				} else if (runFlag==8) {
					RunIntakePump3 = 0;
				} else if (runFlag==12) {
					RunMixer = 0;
					RunOuttakePump = 1;
				} else if (runFlag==17) {
					RunOuttakePump = 0;
					runFlag = -1;
				}
				runFlag++;
			}
			
			// if (RunMixer == 1 && RunEmergencyFlush == 1) {
			//	System.out.println("Violation 1!");
			//	printComponentsStatus("");
			//}
			//if (EmergencyStop == true && RunMixer == 1) {
			//	System.out.println("Violation 2!");
			//	printComponentsStatus("");
			//}
			setComponentsStatus();
			updateComponentsStatusToOntology(owlAPI);
			try {
				owlAPI.startInferring();
			} catch (Exception e) {
				// TODO: handle exception
				System.exit(0);
			}
			
			// Check if R1 has violation
			printComponentsStatus("");
			if (owlAPI.loadDataPropertyValue("R1", "isViolated").size()>0) {
				System.out.println("At time " + runFlag + ": R1" + " isViolated?" + "=");
				owlAPI.printDataPropertyValue("R1", "isViolated");
			}
			
			/*
			if (RunMixer == 1 && RunIntakePump1 == 1) {
				System.out.println("Violation 3!");
				printComponentsStatus("");
				System.out.println("IntakePump1: ");
				owlAPI.printDataPropertyValue("IntakePump1", "isRunning");
				System.out.println("MixerO: ");
				owlAPI.printDataPropertyValue("MixerO", "isRunning");
			}*/
		}
	}
}