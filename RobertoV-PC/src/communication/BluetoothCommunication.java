package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.*;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * Provides communication between PC and Robot
 * 
 * @author Sarun Gulyanon
 * @author Richard Kenyon
 */
public class BluetoothCommunication implements Communication {

	private InputStream in;
	private OutputStream out;
	private NXTComm nxtComm;
	private NXTInfo nxtInfo;
	private boolean isRobotReady = false;
	private boolean isConnected = false;

	/**
	 * @param deviceName
	 *            The name of the bluetooth device
	 * @param deviceMACAddress
	 *            The MAC address of the bluetooth device
	 * @throws NXTCommException 
	 */

	
	public BluetoothCommunication(String deviceName, String deviceMACAddress) {
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, deviceName,
				deviceMACAddress);
	}
	
	public NXTInfo[] RecupInfo(String name) throws NXTCommException{
		return nxtComm.search(name);
	}

	/**
	 * Returns true if the server is connected to the robot, returns false
	 * otherwise
	 * 
	 * @return a boolean indicating whether the server is connected to the robot
	 *         or not
	 */
	public boolean hasConnection() {
		return isConnected;
	}

	/**
	 * Receive a byte from the robot
	 * 
	 * @return An integer containing the byte we received from the robot
	 * 
	 * @throws IOException
	 *             when fail to receive a byte from robot
	 */
	public int receiveByteFromRobot() throws IOException {
		return in.read();
	}

	/**
	 * Not currently used, but might be needed in the future
	 * 
	 * @return a command (stored as a byte array)
	 */
	public byte[] receiveBytesFromRobot() {
		try {
			byte[] bytes = new byte[Constants.COMMAND_SIZE];
			if (in.read(bytes) == Constants.COMMAND_SIZE) {
				return bytes;
			}
		} catch (IOException e) {
			System.err.println("Error receiving from robot: " + e.toString());
		}
		return null;
	}

	/**
	 * Returns whether the robot is ready to receive data or not. Always check
	 * that the robot is ready before sending any commands.
	 */
	public boolean isRobotReady() {
		return isRobotReady;
	}

	/**
	 * Send fixed-sized byte command to robot. Array of bytes consist of opcode
	 * and its parameter
	 * 
	 * @param command
	 *            - opcode concatenate to parameter
	 * 
	 * @throws IOException
	 *             when fail to send command to robot
	 */
	public void sendToRobot(byte[] command) throws IOException,
			IllegalArgumentException {
		if (command.length != Constants.COMMAND_SIZE) {
			throw new IllegalArgumentException("Command has wrong length"
					+ "(Expected " + Constants.COMMAND_SIZE + "byte command)");
		}

		out.write(command);
		out.flush();
	}
	
	public void sendIntToRobot(int command) throws IOException
	
	{
		out.write(command);
		out.flush();
	}


	/**
	 * Opens a new bluetooth connection and connects the input and output
	 * streams to this new bluetooth connection
	 * 
	 * @throws IOException
	 *             when fail to open bluetooth connection
	 */
	public void openBluetoothConnection() throws IOException {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.err.println("Could not create connection " + e.toString());
		}
		System.out.println("Attempting to connect to robot...");

		try {
			nxtComm.open(nxtInfo);
			in = nxtComm.getInputStream();
			out = nxtComm.getOutputStream();

			while ((receiveByteFromRobot()) != Constants.ROBOT_READY) {
				; // wait for ready signal
			}

			isRobotReady = true;
			System.out.println("Robot is ready!");
			isConnected = true;
		} catch (NXTCommException e) {
			throw new IOException("Failed to connect " + e.toString());
		}
	}

	/**
	 * Closes the bluetooth connection and closes the input and output streams
	 */
	public void closeBluetoothConnection() {
		try {
			isConnected = false;
			in.close();
			out.close();
			nxtComm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
