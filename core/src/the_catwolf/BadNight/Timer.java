package the_catwolf.BadNight;

import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.TimeUtils;

public class Timer {

	private enum Mode{
		Count,
		Countdown
	}
	
	Mode mode = Mode.Count;
	
	long prevMs;
	long ms;
	int seconds;
	int minutes;
	float max_time;
	StringBuilder strTimer;
	
	public Timer(){
		strTimer = new StringBuilder();
	}
	
	public void update(){
		if (prevMs == 0L){
			prevMs = TimeUtils.millis();
		}
		if (mode == Mode.Count){
			if (minutes == 99 && seconds == 99 && ms == 999L) return;
			ms += TimeUtils.timeSinceMillis(prevMs);
			prevMs = TimeUtils.millis();
		
			if(ms > 999L){
				ms = 0L;
				seconds += 1;
			}
			if (seconds > 59){
				seconds = 0;
				minutes += 1;
			}
			if (minutes > 99){
				minutes = 99;
				seconds = 59;
				ms = 999L;
			}
			return;
		}
		//Mode.Countdown
		if (minutes == 0 && seconds == 0 && ms == 0L) return;
		ms += -TimeUtils.timeSinceMillis(prevMs);
		prevMs = TimeUtils.millis();
		
		if (ms < 0L){
			ms = 999L;
			seconds -= 1;
		}
		if (seconds < 0){
			seconds = 59;
			minutes -= 1;
		}
		if (minutes < 0){
			minutes = 0;
			seconds = 0;
			ms = 0L;
		}
	}
	
	public CharSequence getString(){
		strTimer.setLength(0);
		strTimer.append(minutes, 2);
		strTimer.append(":");
		strTimer.append(seconds, 2);
		strTimer.append(":");
		strTimer.append(ms, 3);
		return strTimer;
	}
	
	public void reset(){
		prevMs = 0L;
		ms = 0L;
		seconds = 0;
		minutes = 0;
	}
	
	public void set(int minutes, int seconds, long ms){
		if (minutes < 0) minutes = 0;
		if (minutes > 99) minutes = 99;
		this.minutes = minutes;
		
		if (seconds < 0) seconds = 0;
		if (seconds > 59) seconds = 59;
		this.seconds = seconds;
		
		if (ms < 0L) ms = 0L;
		if (ms > 999L) ms = 999L;
		this.ms = ms;
	}
	
	public void setCountMode(){
		mode = Mode.Count;
	}
	
	public void setCountdownMode(){
		mode = Mode.Countdown;
	}
	
	public float getFloatMs(){
		return minutes * 60 * 1000 + seconds * 1000 + ms;
	}
	
	public long getLongMs(){
		return minutes * 60 * 1000 + seconds * 1000 + ms;
	}
	
	public boolean isOver(){
		return mode == Mode.Countdown && minutes == 0 && seconds == 0 && ms == 0L;
	}
	
}
