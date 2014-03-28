package communication;
//import java.io.*;
import lejos.nxt.*;
import lejos.util.TextMenu;


public class Menu{
	
	public static void main(String[] args) throws Exception{
		String[] select = new String[3];
		select[0]="Emetteur";
		select[1]="Recepteur";
		select[2]="Sortir";
		EntiteeBT r1 = new EntiteeBT("Robot1",(byte)1,"00:16:53:0A:6A:65") ;
		EntiteeBT lui = new EntiteeBT("marion","00:0C:78:33:EB:0C");
		EntiteeBT r2 = new EntiteeBT("RobotI",(byte)2,"00:16:53:06:DE:F2");
		Trame2 message1= new Trame2((byte)9,(byte)1,(byte)0,(byte)0,false,true,true,(byte)2);
		Trame2 message2= new Trame2((byte)9,(byte)2,(byte)1,(byte)1,false,false,true,(byte)2);
		
		ComBluetooth com1 = new ComBluetooth(r1);
		ComBluetooth com2 = new ComBluetooth(r1);
		
		LCD LCD=new LCD();
		TextMenu monMenu = new TextMenu(select);
		monMenu.setTitle("Wow very menu");
		if (monMenu.select()==0){
			lejos.nxt.LCD.clear();
			//ComBluetooth com1 = new ComBluetooth(r1);
			com1.initialisation();
			Thread.sleep(4000);
			com1.envoyer(message2);
			//com1.moi.trame = new Trame2((byte)3,(byte)0,(byte)0);
			
			//while(true){
				//System.out.println("coucou");
				//Thread.sleep(4000);
				//System.out.println("coucou2");
				//com1.moi.trame.printTrame();
				//Thread.sleep(4000);
				//com1.envoie = new Envoie(lui,message1);
				
			//}
			//ComBluetooth com2 = new ComBluetooth(r2,lui);
		//}
		if (monMenu.select()==1){
			lejos.nxt.LCD.clear();
			//ComBluetooth com = new ComBluetooth(lui,moi);
			//Trame2 message2 = com.ecouter();
			//ComBluetooth com1 = new ComBluetooth(r1);
			System.out.println("test");
			Thread.sleep(4000);
			com1.initialisation();
			message1= com1.ecouter();
			message1.printTrame();
			com1.fermer();
			}
		if (monMenu.select()==2){
			
			monMenu.quit();
			lejos.nxt.LCD.clear();
			}
		}
	}

}
		