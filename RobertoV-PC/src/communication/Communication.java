package communication;

import java.io.IOException;

import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public interface Communication {
	
	public NXTInfo[] RecupInfo(String name) throws NXTCommException;
	public void openBluetoothConnection() throws IOException;

	public void closeBluetoothConnection();

	public boolean isRobotReady();

	public void sendToRobot(byte[] command) throws IOException,
			IllegalArgumentException;

	public boolean hasConnection();

	public int receiveByteFromRobot() throws IOException;
}
