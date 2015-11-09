package todo;

import javax.crypto.Cipher;

import se.lth.cs.realtime.semaphore.*;
import done.ClockInput;
import done.ClockOutput;

public class SharedData {
	private int time;
	private int alarmTime;
	private ClockOutput co;
	private Semaphore mutex;
	private boolean alarmActive;
	private Semaphore sem;
	private ClockInput ci;

	public SharedData(ClockOutput co, ClockInput ci) {
		this.co = co;
		time = 0;
		alarmTime = 0;
		mutex = new MutexSem();
		this.ci = ci;
		sem = ci.getSemaphoreInstance();

	}

	public Semaphore getSemaphore() {
		return mutex;
	}

	public void increaseTime() {
		mutex.take();
		int hh = time / 10000;
		int mm = (time - hh * 10000) / 100;
		int ss = (time - hh * 10000 - mm * 100);
		ss++;

		if (ss > 59) {
			ss = 0;
			mm += 1;
		}
		if (mm > 59) {
			mm = 0;
			hh += 1;
		}
		if (hh > 23) {
			hh = 0;
		}
		
		// Possibly add time counting up while setting alarm
		if(ci.getChoice() == ci.SHOW_TIME)
		time = hh * 10000 + mm * 100 + ss;

		if (time == alarmTime && ci.getAlarmFlag()) {
			alarmActive = true;
		}
		if (alarmActive) {
			co.doAlarm();
		}
		if (time - alarmTime > 19)
			alarmActive = false;
		mutex.give();
	}

	public void setTime(int value) {
		this.time = value;
	}

	public void showTime() {
		if(ci.getChoice() == ci.SHOW_TIME){
			co.showTime(time);
		}

	}

	public void setAlarm(int value) {
		mutex.take();
		this.alarmTime = value;
		mutex.give();
	}

	public void buttonPressed() {
		mutex.take();
		alarmActive = false;
		mutex.give();
	}

}
