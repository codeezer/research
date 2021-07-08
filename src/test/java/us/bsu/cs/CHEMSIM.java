package us.bsu.cs;

import java.util.Random;

public class CHEMSIM {
	static int RunIntakePump1, RunIntakePump2, RunIntakePump3, RunOuttakePump, RunMixer, EmergencyFlush;
	static boolean RunProcess, EmergencyStop;
	
	public CHEMSIM() {
		
	}
	
	public static void printComponentsStatus(String title) {
		System.out.println("-----" + title + "-----");
		System.out.println("RunProcess: " + RunProcess);
		System.out.println("EmergencyStop: " + EmergencyStop);
		System.out.println("RunIntakePump1: " + RunIntakePump1);
		System.out.println("RunIntakePump2: " + RunIntakePump2);
		System.out.println("RunIntakePump3: " + RunIntakePump3);
		System.out.println("RunOuttakePump: " + RunOuttakePump);
		System.out.println("RunMixer: " + RunMixer);
		System.out.println("EmergencyFlush: " + EmergencyFlush);
	}
	
	public static void injectMixerAttack() {
		RunMixer = 1;
	}
	
	public void simulateControlLogic() {
		Random random = new Random();
		
		RunIntakePump1 = 0; RunIntakePump2 = 0; RunIntakePump3 = 0;
		RunOuttakePump = 0; RunMixer = 0; EmergencyFlush = 0;
		
		RunProcess = random.nextBoolean();
		EmergencyStop = random.nextBoolean();
		int N = 1700;
		int attackTime = random.nextInt(N);
		
		int emergencyFlag = 0;
		int runFlag = 0;
		
		for (int i = 0; i < N; i++) {
			RunProcess = random.nextBoolean();
			EmergencyStop = random.nextBoolean();
			
			
			if (i==attackTime) {
				System.out.println("Attack injected at time: " + attackTime);
				injectMixerAttack();
			}
			
			if (EmergencyStop) {
				RunIntakePump1 = 0; RunIntakePump2 = 0; RunIntakePump3 = 0;
				RunOuttakePump = 0; RunMixer = 0; EmergencyFlush = 0;
				EmergencyFlush = 1; runFlag = 0;
				emergencyFlag++;
				if (emergencyFlag>=5) {
					EmergencyFlush = 0;
				}
			}
			else if (RunProcess) {
				EmergencyFlush = 0; emergencyFlag = 0;
				
				if (runFlag<=1) {
					RunIntakePump1 = 1;
				} else if (runFlag<=3) {
					RunIntakePump1 = 0;
					RunIntakePump2 = 1;
					RunIntakePump3 = 1;
					RunMixer = 1;
				} else if (runFlag<=5) {
					RunIntakePump2 = 0;
				} else if (runFlag<=7) {
					RunIntakePump3 = 0;
				} else if (runFlag<=11) {
					RunMixer = 0;
					RunOuttakePump = 1;
				} else if (runFlag<=16) {
					RunOuttakePump = 0;
					if (runFlag==16) {
						runFlag = -1;
					}
				}
				runFlag++;
			}
			printComponentsStatus(""+i);
			
			if (RunMixer == 1 && EmergencyFlush == 1) {
				System.out.println("Violation 1!");
				printComponentsStatus("");
			}
			if (EmergencyStop == true && RunMixer == 1) {
				System.out.println("Violation 2!");
				printComponentsStatus("");
			}
			if (RunMixer == 1 && RunIntakePump1 == 1) {
				System.out.println("Violation 3!");
				printComponentsStatus("");
			}
		}
	}
}