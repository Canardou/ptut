package communication;

/**
 * Command Storage with concurrency support for CommunicationServer
 * 
 * @author Sarun Gulyanon
 * 
 */
public class Command {
	private static final int TIMEOUT = 300000;

	byte[] cmd;

	public Command() {
		cmd = null;
	}

	public synchronized void set(byte[] command) {
		cmd = command;
		notify();
	}

	public synchronized byte[] get() {
		while (cmd == null) {
			try {
				wait(TIMEOUT);
			} catch (InterruptedException e) {
				System.err.println("Error Getting Command");
			}
		}
		byte[] temp = cmd;
		cmd = null;
		return temp;
	}
}
