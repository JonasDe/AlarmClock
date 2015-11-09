package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static Semaphore	sem;
	private SharedData sharedData;
	private Semaphore mutex;
	

	public AlarmClock(ClockInput i, ClockOutput o, SharedData sharedData) {
		input = i;
		output = o;
		sem = input.getSemaphoreInstance();
		this.sharedData = sharedData;
		mutex = sharedData.getSemaphore();
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		while (true) {
			sem.take();
			sharedData.buttonPressed();
			int choice = input.getChoice();
			if(choice == input.SET_TIME){
				sharedData.setTime(input.getValue());	
			} else if (choice == input.SET_ALARM){
				sharedData.setAlarm(input.getValue());
			} else {
			}
				
		}
	}
}
