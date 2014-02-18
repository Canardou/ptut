package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Allows other systems to communicate with the robot via a TCP socket
 * 
 * @author Sarun Gulyanon
 * @author Richard Kenyon
 * 
 */
public class CommunicationServer extends Thread {
	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private Command command;

	private static final String NXT_MAC_ADDRESS = "00:16:53:08:A0:E6";
	private static final String NXT_NAME = "group6";
	private static final int PORT = 6789;
	private static final int RECONNECT_WAIT = 5000;

	private static Communication comms;

	/**
	 * Creates a socket and a bluetooth and waits for a connection
	 */
	public CommunicationServer() {
		command = new Command();

		System.out.println("Starting server...");
		comms = new BluetoothCommunication(NXT_NAME, NXT_MAC_ADDRESS);
		try {
			comms.openBluetoothConnection();
		} catch (IOException e) {
			System.err.println("Couldn't make bluetooth connection");
		}

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println("Couldn't Create Server Socket on Port " + PORT);
		}

		openTCPConnection();

		this.start(); // start robot listener loop
		listen(); // listen to socket
	}

	/**
	 * Listens to the socket for commands and forwards any commands it receives
	 * to the robot
	 */
	private void listen() {

		for (;;) {
			if (socket.isClosed()) {
				openTCPConnection();
			}

			try {
				byte[] commandFromClient = new byte[Constants.COMMAND_SIZE];

				int numofBytesRead = in.read(commandFromClient, 0,
						Constants.COMMAND_SIZE);

				if ((numofBytesRead == Constants.COMMAND_SIZE)
						&& numofBytesRead != -1) {

					// TODO: No longer needed, remove?
					if (commandFromClient[0] == Constants.RESET) {
						commandFromClient[0] = Constants.STOP;
					}

					command.set(commandFromClient);

				} else {
					System.out.println("We're at the end of the stream"
							+ " closing stream and socket now!");
					closeTCPConnection();
				}

			} catch (IOException e) {
				System.err.println("Couldn't read from socket, "
						+ "closing socket...");
				closeTCPConnection();
			}
		}
	}

	/**
	 * Get the latest command and send to robot and listen for feedback
	 */
	public void run() {

		byte[] commandToSend;

		for (;;) {
			commandToSend = command.get();

			try {
				comms.sendToRobot(commandToSend);

			} catch (IOException e) {
				System.err.println("Lost bluetooth connection to robot");
				comms.closeBluetoothConnection();

			} catch (IllegalArgumentException e) {
				System.err.println("Server cannot send command: "
						+ e.toString());
			}

			// If connection is down, then reconnect
			while (!comms.hasConnection()) {
				System.out.println("Waiting to reconnect...");

				// Wait a few seconds before attempting
				// to reconnect to the robot
				try {
					Thread.sleep(RECONNECT_WAIT);
				} catch (InterruptedException e) {
					System.err.println("Sleep on RECONNECT_WAIT interrupted");
				}

				// Try re-establishing Bluetooth connection
				try {
					comms.openBluetoothConnection();
				} catch (IOException e) {
					System.err.println("Couldn't make bluetooth connection");
				}
			}
		}
	}

	/**
	 * Send feedback to the strategy system
	 * 
	 * @param status
	 */
	private void sendFeedback(int feedback) {
		try {
			out.write(feedback);
			out.flush();
			System.out.println("Status sent " + feedback);
		} catch (IOException e) {
			System.err.println("Error Couldn't Send Feedback");
		}
	}

	/**
	 * Open TCP connection and connects the input and output streams to this new
	 * TCP connection
	 */
	private void openTCPConnection() {
		try {
			socket = serverSocket.accept();
			System.out.println("socket accepted connection");
		} catch (IOException e) {
			System.err.println("Couldn't listen on port: " + PORT);
		}
		// Open Socket Input/Output Stream
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			System.err.println("Error opening input/output stream");
		}
	}

	/**
	 * Close TCP connection and close input and output streams
	 */
	private void closeTCPConnection() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Couldn't close socket/stream.");
		}
	}

	public static void main(String args[]) {
		System.out.println("Starting communication server...");
		new CommunicationServer();
	}
}
