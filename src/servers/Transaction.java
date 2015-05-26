package servers;
public class Transaction {

	public static final int STARTED = 1;
	public static final int COMMITED = 2;
	public static final int ABORTED = 3;

	private String fileName;
	private long id;
	private int state;
	private long timeStamp;

	public Transaction(String fileName, long id) {
		this.fileName = fileName;
		this.id = id;
		state = STARTED;
		timeStamp = id;
	}

	public String getFileName() {
		return fileName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
