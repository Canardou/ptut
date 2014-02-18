package communication;

public class Constants {
	public static final int BT_DEAD = -1;
	public static final int ROBOT_READY = 0;
	public static final int ACK_SIGNAL = 1;
	public static final int TOUCH_SIGNAL = 2;
	public static final int COMMAND_SIZE = 3;

	// not a real command!
	public static final byte[] KEEP_ALIVE = { -1, 0, 0 };
	public static final int STOP = 4;
	public static final int RESET = 98;
}
