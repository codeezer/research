package us.bsu.cs;

import java.util.Timer;

public class CHEMIMPL {
	public static String ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	static int RunIntakePump1, RunIntakePump2, RunIntakePump3, RunOuttakePump, RunMixer, EmergencyFlush;
	static boolean RunProcess, EmergencyStop;
	
	public static void printComponentsStatus(String title) {
		System.out.println("-----" + title + "-----");
		System.out.println("RunIntakePump1: " + RunIntakePump1);
		System.out.println("RunIntakePump2: " + RunIntakePump2);
		System.out.println("RunIntakePump3: " + RunIntakePump3);
		System.out.println("RunOuttakePump: " + RunOuttakePump);
		System.out.println("RunMixer: " + RunMixer);
		System.out.println("EmergencyFlush: " + EmergencyFlush);
	}
	
	public static void simulateControlLogic() {
		RunIntakePump1 = 0; RunIntakePump2 = 0; RunIntakePump3 = 0;
		RunOuttakePump = 0; RunMixer = 0; EmergencyFlush = 0;
		
		RunProcess = true;
		EmergencyStop = false;
		
		int emergencyFlag = 0;
		int runFlag = 0;
		
		for (int i = 0; i < 17; i++) {
			
//			if (RunMixer == 1) {
//				System.out.println("HERE");
//			}
			
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
				emergencyFlag = 0;
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
					if (runFlag == 16) {
						runFlag = -1;
					}
				}
				runFlag++;
			}
		}
	}
	
	public static void main(String[] args) {
		simulateControlLogic();
		
	}
}