package the_catwolf.BadNight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameData {
	private Preferences preferences;
	
	float soundVolume = 1f;
	float musicVolume = 1f;
	boolean usesVibration = true;//mobile/tablet devices only
		
	public GameData(){
		preferences = Gdx.app.getPreferences("dotteriTheCatwolf.ABadNight.settings");
		load();
	}
	
	private void load(){
		soundVolume = preferences.getFloat("sound", 1f);
		musicVolume = preferences.getFloat("music", 1f);
		usesVibration = preferences.getBoolean("vibration", true);
	}
	
	public void save(){
		preferences.putFloat("sound", soundVolume);
		preferences.putFloat("music", musicVolume);
		preferences.putBoolean("vibration", usesVibration);
		preferences.flush();
	}
	
}
