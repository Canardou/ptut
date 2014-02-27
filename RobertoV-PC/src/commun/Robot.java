package commun;
import java.io.*;

import communication.*;
import lejos.pc.comm.*;

public class Robot
{
	/**
	 * Concentre tout l'interface de communication avec les robots
	 * comme le deplacement, les coordonnees et l'arret 
	 * Le Superviseur se sert de cette interface pour communiquer de maniere totalement transparente
	 */
	private BluetoothCommunication com;
	private String nom;
	private char id;

	

	public Robot(String nom, String addr)
	{
		this.nom = nom;
		this.com = new BluetoothCommunication(this.nom, addr);
	}
	
	public void open() throws IOException
	{
		this.com.openBluetoothConnection();
	}
	
	public void close() throws IOException
	{
		this.com.closeBluetoothConnection();
	}
	
	public String getName(){
		return this.nom;
	}
	
	public char getID()
	{
		return this.id;
	}
	
	public void onNewMessage(Message msg)
	{
		System.out.println(msg);

	}
	
	public void envoie(int msg) throws IOException
	{
		this.com.sendIntToRobot(msg);
	}
	
	public int recoie() throws IOException
	{
		return this.com.receiveIntFromRobot();
	}
	
}