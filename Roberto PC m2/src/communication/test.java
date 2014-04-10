package communication;


import java.io.IOException;
import java.util.Scanner;

import env.Case;
//import robot.Param;

public class test {
	
	public static Emission E1;
	public static Emission E2;
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException{
		
	
		EntiteeBT PC= new EntiteeBT("marion","00:0C:78:33:EB:0C");
		//EntiteeBT PC= new EntiteeBT("kiwor-0", "00:15:83:0C:BF:EB");
		//EntiteeBT PC= new EntiteeBT("Thomas", "26:0A:64:62:8D:29");
		
		EntiteeBT robot1= new EntiteeBT("Robot H",(byte)1,"00:16:53:06:DA:CF");
		//EntiteeBT robot1= new EntiteeBT("Robot J",(byte)1,"00:16:53:06:F5:30");
		//EntiteeBT robot3= new EntiteeBT("Robot I",(byte)1,"00:16:53:06:DE:F2");
		

		
		System.out.println("entrée robot :");
		Scanner sc = new Scanner(System.in);
		int str =sc.nextInt();
		
		while(true){			
			if (str==1){
				
					//Choix emission ou reception
					System.out.println("Voulez vous emettre(1) ou recevoir(2) :");
					int strTypeComm =sc.nextInt();
					
					
					//EMISSION
					
					if (strTypeComm==1){
					
					System.out.println("Type Trame (1=position initiale robot ; 2= infos(demandeCalibration...); 3= ordre :");
					int strTrame =sc.nextInt();
					
					if (strTrame==1){
						/*BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
						bluetoothPC1.connexion();
						System.out.println("robot 1 connecté");
						System.out.println("j'ai recu 0, j'envoie une trame" );
						Case caseFirst= new Case(1,2);
						Trame2 sendFirstPosition = new Trame2((byte)1,caseFirst,Param.XP);
						sendFirstPosition.printTrame();
						bluetoothPC1.send (sendFirstPosition);
						System.out.println("c'est fait" );*/
						
					
						
					}
					
					else if (strTrame==2){
						BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
						bluetoothPC1.connexion();
						System.out.println("robot 1 connecté");
						System.out.println("j'ai recu 0, j'envoie une trame" );
						
						//sendInfos(ID, message, 0); Calibration
						//message = 8 -> demande Calibration
						//message = 4 -> calibration OK
						//message =2 -> start mission
						// message =1 -> mission termin�e           � faire, nouvelle classe avec ces parametres
						Trame2 sendInfos= new Trame2((byte)1,(byte)8,(byte)0);
						sendInfos.printTrame();
						bluetoothPC1.send(sendInfos);
						System.out.println("c'est fait" );
						
					}
					
					else if (strTrame==3){
						System.out.println("entrée ordre :");
						int strordre =sc.nextInt();
						Trame2 sendOrdre= new Trame2((byte)1,(byte)strordre);
						BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
						bluetoothPC1.connexion();
						System.out.println("robot 1 connecté");
						System.out.println("j'ai reçu 0, j'envoie une trame" );
	
						bluetoothPC1.send (sendOrdre);
						System.out.println("c'est fait" );
						
						}
				
						
					}
					
					//RECEPTION
					
					else if (strTypeComm==2){
						System.out.println("Type Trame (1= infos(demandeCalibration...); 2= liste case exploree :");
						int strTrame =sc.nextInt();
						
						if (strTrame==1){
							BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(robot1, PC);
							bluetoothPC1.connexion();
							System.out.println("robot 1 connecté");
							
							Trame2 receiveInfos;
							receiveInfos=bluetoothPC1.receive ();
							System.out.println("trame recue :");
							receiveInfos.printTrame();
							
							
							
						}
						
						else if (strTrame==2){
							BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(robot1, PC);
							bluetoothPC1.connexion();
							System.out.println("robot 1 connecté");
							System.out.println("j'ai recu 0, j'envoie une trame" );
							
							Trame2 receiveListCase;
							receiveListCase=bluetoothPC1.receive ();
							System.out.println("trame recue :");
							receiveListCase.printTrame();
							
							
						}
						
					}
			}
					
					

				
					
			else if (str==2){
				BluetoothCommPC2 bluetoothPC2= new BluetoothCommPC2(PC, robot1);
				bluetoothPC2.initialisationCommunication();
				System.out.println("robot 2 connecté");		
				
				//if(PC.getInput().read()==0){
				//bluetoothPC2.sendTrameToNXT (trameEnvoyee2);
					
				//}
					
				
			}
		}
				

		
		
	}
	
	
}


