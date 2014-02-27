package communication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import commun.Message;
import commun.Robot;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class BluetoothManager
{
	/**
	 * Gere les communication bluetooth cote ordinateur
	 */
	private DataInputStream dis;
	private DataOutputStream dos;
	private Robot robot;
	private NXTComm nxtComm;
	private boolean stop;
 
	public BluetoothManager(String distName, String distAddr, Robot robot)
	{
		
		
		boolean result = false;
		this.stop=false;
		this.robot = robot;

		
		try{
			this.nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			//this.nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		}catch(Exception e){
		    System.out.println("Pas de bluetooth");
		    System.exit(1);
		}
		
		System.out.println("Connecting...");

		try{
			result = this.nxtComm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, distName, distAddr),NXTComm.PACKET);
			//result = this.nxtComm.open(this.nxtComm.search("Robot 9")[0]);
		}catch(Exception e){
			System.out.println("Err Connection");
		    System.exit(1);
		}
	  
		if (result == false){
			System.out.println("Err Connection");
		    System.exit(1);
		}
	    
		System.out.println("Connected");
	
	    this.dis = new DataInputStream(this.nxtComm.getInputStream());
	    this.dos = new DataOutputStream(this.nxtComm.getOutputStream());
	}
/*	
	public Message receiveMessage()
	{
		long n;
		
		try{
			n = this.dis.readLong();
	        Message msg= new Message(n);
	        return msg;
		}
		catch(IOException e)
		{
			System.out.println("Erreur de lecture");
			return new Message(-1);
		}
	}
	
	public void sendMessage(Message msg)
	{
		try{
			 long n = msg.ToLong();
	         dos.writeLong(n);
	         dos.flush(); 
			}
		catch (IOException ioe) {
			System.out.println("Write Exception");
		}
		
	}
	
	public void run()
	{
		while(!this.stop)
		{
			Message msg = this.receiveMessage();
			try{
				this.setPriority(MAX_PRIORITY);
				this.robot.onNewMessage(msg);
				this.setPriority(NORM_PRIORITY);
			}catch(NullPointerException e){
				System.out.println("Owner is null");
			}
		}
	}
*/
	public void sendMessage(int msg)
	{
		try{
			 int n = msg;
	         dos.writeInt(n);
	         dos.flush(); 
			}
		catch (IOException ioe) {
			System.out.println("Write Exception");
		}
	}
	
	public void close(){
		try {
			this.stop=true;
		    this.dis.close();
		    this.dos.close();
		    this.nxtComm.close();
		} catch (IOException ioe) {
		    System.out.println("IOException closing connection:");
		    System.out.println(ioe.getMessage());
		}
	}
}