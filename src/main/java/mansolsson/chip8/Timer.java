package mansolsson.chip8;

public class Timer {
	private int time;

	public synchronized void reduce() {
		if (time > 0) {
			time--;
		}
	}

	public synchronized void setTime(final int time) {
		this.time = time;
	}

	public synchronized int getTime() {
		return time;
	}
}
