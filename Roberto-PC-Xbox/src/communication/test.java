package communication;


import java.io.IOException;

import com.centralnexus.*;
import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;

import java.util.Scanner;

public class test {
	
	public static Emission E1;
	public static Emission E2;
	
	
	public static void main(String[] args) throws IOException{
		//E1=new Emission();
		//E2=new Emission();
		
		//JoystickListener l = null ;
		Joystick manette = Joystick.createInstance(0);
		//manette.addJoystickListener(l);
		
		
		EntiteeBT PC= new EntiteeBT("kiwor-0", "00:15:83:0C:BF:EB");
		EntiteeBT robot1= new EntiteeBT("Robot J",(byte)1,"00:16:53:06:F5:30");
		
		EntiteeBT robot2= new EntiteeBT("Robot I",(byte)1,"00:16:53:06:DE:F2");
		
		Trame2 trameEnvoyee1= new Trame2((byte)3,(byte)1,(byte)1);
		//Trame2 trameEnvoyee2= new Trame2((byte)9,(byte)22,(byte)22,(byte)22,true,false,false,(byte)3);
			
		
			System.out.println("entrée robot :");
			Scanner sc = new Scanner(System.in);
			int str =sc.nextInt();
			int strordre =0;
			int strordreold =0;
			System.out.println("nombre boutons :"+manette.getNumButtons());
			System.out.println("nombre axes :"+manette.getNumAxes());
						
			if (str==1){
				//System.out.println("entrée ordre :");
				//int strordre =sc.nextInt();
				BluetoothCommPC2 bluetoothPC1= new BluetoothCommPC2(PC, robot1);
				bluetoothPC1.initialisationCommunication();
				
				while(true){
				strordre=0;
				manette.poll();
				if(manette.isButtonDown(manette.BUTTON2)){
					strordre=3;
				}
				else if(manette.isButtonDown(manette.BUTTON3)){
					strordre=2;
				}
				else if(manette.isButtonDown(manette.BUTTON4)){
					strordre=1;
				}
				
				if(strordre!=strordreold){
				//System.out.println("ordre: "+strordre);
				strordreold = strordre;
				trameEnvoyee1= new Trame2((byte)3,(byte)1,(byte)strordre);
				
				//System.out.println("robot 1 connecté");
				//System.out.println("j'ai reçu 0, j'envoie une trame" );
				//trameEnvoyee1.printTrame();
				bluetoothPC1.sendTrameToNXT (trameEnvoyee1);
				//System.out.println("c'est fait" );
				}
				//strordreold=0;
					
			
					
			  /*  if (str==2){
				BluetoothCommPC2 bluetoothPC2= new BluetoothCommPC2(PC, robot2);
				bluetoothPC2.initialisationCommunication();
				System.out.println("robot 2 connecté");		
				
				//if(PC.getInput().read()==0){
				//bluetoothPC2.sendTrameToNXT (trameEnvoyee2);
					
				//}*/
					
				
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


