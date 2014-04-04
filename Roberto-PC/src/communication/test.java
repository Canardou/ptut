package communication;


import java.io.IOException;
import java.util.Scanner;

public class test {
	
	public static Emission E1;
	public static Emission E2;
	
	
	
	public static void main(String[] args) throws IOException{
		//E1=new Emission();
		//E2=new Emission();
	
		//EntiteeBT PC= new EntiteeBT("marion","00:0C:78:33:EB:0C");
		//EntiteeBT PC= new EntiteeBT("kiwor-0", "00:15:83:0C:BF:EB");
		EntiteeBT PC= new EntiteeBT("Thomas", "26:0A:64:62:8D:29");
		
		EntiteeBT robot1= new EntiteeBT("Robot H",(byte)1,"00:16:53:06:DA:CF");
		EntiteeBT robot2= new EntiteeBT("Robot J",(byte)1,"00:16:53:06:F5:30");
		EntiteeBT robot3= new EntiteeBT("Robot I",(byte)1,"00:16:53:06:DE:F2");
		
		Trame2 trameEnvoyee1= new Trame2((byte)3,(byte)1,(byte)1);
		//Trame2 trameEnvoyee2= new Trame2((byte)9,(byte)22,(byte)22,(byte)22,true,false,false,(byte)3);
			
		
			System.out.println("entrée robot :");
			Scanner sc = new Scanner(System.in);
			int str =sc.nextInt();
			while(true){			
			if (str==1){
				System.out.println("entrée ordre :");
				int strordre =sc.nextInt();
				trameEnvoyee1= new Trame2((byte)3,(byte)1,(byte)strordre);
				BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
				bluetoothPC1.initialisationCommunication();
				System.out.println("robot 1 connecté");
				System.out.println("j'ai reçu 0, j'envoie une trame" );
				trameEnvoyee1.printTrame();
				bluetoothPC1.sendTrameToNXT (trameEnvoyee1);
				System.out.println("c'est fait" );

					
			}
					
			else if (str==2){
				BluetoothCommPC2 bluetoothPC2= new BluetoothCommPC2(PC, robot2);
				bluetoothPC2.initialisationCommunication();
				System.out.println("robot 2 connecté");		
				
				//if(PC.getInput().read()==0){
				//bluetoothPC2.sendTrameToNXT (trameEnvoyee2);
					
				//}
					
				
			}
		}
				

		
		
	}
	/*
	
	public static void main(String[] args) throws Exception{
		
		R=new Reception();
		EntiteeBT PC= new EntiteeBT("marion", "00:0C:78:33:EB:0C");
		EntiteeBT robot1= new EntiteeBT("Robot 1",(byte)1,"00:16:53:0A:6A:65");
		
		BluetoothCommPC2 bluetoothPC= new BluetoothCommPC2(robot1, PC);
		
		bluetoothPC.initialisationCommunication();
		Trame2 trameRecue= bluetoothPC.receiveTrameNXT() ;
		trameRecue.printTrame();
		
		
		
	}*/
	
}


