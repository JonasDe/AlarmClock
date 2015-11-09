package todo;

import org.omg.CORBA.CODESET_INCOMPATIBLE;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class ClockCounter extends Thread {
	private long lastDiff = 0;
	private SharedData sharedData;
	private Semaphore mutex;
	private Semaphore sem;
	private long t;

	public ClockCounter(SharedData sharedData, ClockInput input) {
		this.sharedData = sharedData;
		mutex = sharedData.getSemaphore();
		sem = input.getSemaphoreInstance();
	}

	public void run() {
		while (true) {
			sleepSec();
			sharedData.increaseTime();
			sharedData.showTime();
			lastDiff = System.currentTimeMillis() - t;
		}
	}
	
	private void sleepSec(){
		
		t = System.currentTimeMillis();
		t += 1000- lastDiff;
		try {
			Thread.sleep(1000 - lastDiff);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
