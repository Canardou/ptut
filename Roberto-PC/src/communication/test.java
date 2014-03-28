package communication;


import java.io.IOException;
import java.util.Scanner;

public class test {
	
	public static Emission E1;
	public static Emission E2;
	
	
	
	public static void main(String[] args) throws IOException{
		//E1=new Emission();
		//E2=new Emission();
	
		EntiteeBT PC= new EntiteeBT("marion", "00:0C:78:33:EB:0C");
		EntiteeBT robot1= new EntiteeBT("Robot 1",(byte)1,"00:16:53:0A:6A:65");
		
		EntiteeBT robot2= new EntiteeBT("Robot I",(byte)1,"00:16:53:06:DE:F2");
		
		Trame2 trameEnvoyee1= new Trame2((byte)9,(byte)12,(byte)12,(byte)12,true,false,false,(byte)3);
		Trame2 trameEnvoyee2= new Trame2((byte)9,(byte)22,(byte)22,(byte)22,true,false,false,(byte)3);
			
		while(true){
			System.out.println("entrée robot :");
			Scanner sc = new Scanner(System.in);
			int str =sc.nextInt();
							
			if (str==1){
				BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
				bluetoothPC1.initialisationCommunication();
				System.out.println("robot 1 connecté");
				//if(PC.getInput().read()==0){
					
				bluetoothPC1.sendTrameToNXT (trameEnvoyee1);
					
				//}
			}
					
			else if (str==2){
				BluetoothCommPC2 bluetoothPC2= new BluetoothCommPC2(PC, robot2);
				bluetoothPC2.initialisationCommunication();
				System.out.println("robot 2 connecté");		
				
				//if(PC.getInput().read()==0){
				bluetoothPC2.sendTrameToNXT (trameEnvoyee2);
					
				//}
					
				
			}
		}
				

		
		
	}
	
	
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


