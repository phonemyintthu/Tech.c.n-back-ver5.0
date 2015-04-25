package n_back;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

	/** 一定時間毎にタスクを起動するTimer */
	private Timer timer = null;
	/** Timerから一定時間毎に起動されるタスク */
	private TestTask task = null;
	/** 時刻を保持するオブジェクト */
	private Time time = null;

	public TimerUtil() {
		timer = new Timer(true);
		time = new Time();
	}

	// timer起動
	public void start() {
		if (task == null) {
			task = new TestTask(time);
		}

		try{
		timer.schedule(task, 0, 1000);
		}catch(Exception e){
			e.getStackTrace();
		}
	}

	// timer一時停止
	public void stop() {
		task.cancel();
		task = null;
		//System.out.println("Taskが停止しました");
	}

	public int getSecond() {
		return task.getSecond();
	}

}

// タイマから起動されるタスク。
class TestTask extends TimerTask {
	private Time time;
	private int second;

	public TestTask(Time aTime) {
		time = aTime;
	}

	public void run() {
		time.tick();
		second = time.getSecond();
		// System.out.println("Second = " + time.getSecond());
	}

	public int getSecond() {
		return second;
	}

	// timerのリセット ※0に戻す
	public void reset() {
		second = 0;
	}
}

// 時刻を保持するクラス。
class Time {
	private int second = 0;

	public void tick() {
		second++;
	}

	public int getSecond() {
		return second;
	}
}

