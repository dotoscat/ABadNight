package the_catwolf.BadNight.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import the_catwolf.BadNight.BadNight;

public class DesktopLauncher {
	
	public static int leaderboard_test = 1;
	public static int leaderboard_timeattack2 = 2;
	public static int leaderboard_resistance100 = 3;
	
	public static void main (String[] arg) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 480;
		config.resizable = false;
		config.forceExit = true;
		config.x = -1;
		config.y = -1;
		config.samples = 4;
		//config.overrideDensity = 1024;
		BadNight.init();
		BadNight badNight = new BadNight(new DesktopGoogleServices());
		new LwjglApplication(badNight, config);
	}
}
