package the_catwolf.BadNight.GameMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import the_catwolf.BadNight.Astral;
import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.Engine;
import the_catwolf.BadNight.Engine.State;

public class Test extends GameMode {
	float timePath = 0f;
	float maxTimePath = 7f;
	
	final float METEOR_MAX_SPEED = BadNight.VHEIGHT/8f;
	final float METEOR_MIN_SPEED = BadNight.VHEIGHT/16f;
	
	final long MAX_METEORS_ON_SCREEN = 4L;
	final long MIN_METEORS_ON_SCREEN = 2L;
	
	
	boolean oneBigMeteorOnScreen = false;
	
	float ufoEvent = 0f;
	
	int meteorsLeft = 10;
	StringBuilder stringMeteorsLeft;
	
	final float MAX_SEC = 7f;
	float sec = MAX_SEC;
	
	boolean play = false;
	boolean ufoOnScreen = false;
	
	public Test(String name, int leaderboardId, int achievementId, String fileName, int entries) {
		super(name, leaderboardId, achievementId, fileName, entries);
		stringMeteorsLeft = new StringBuilder();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Engine engine) {
		// TODO Auto-generated method stub
		engine.setMessageOnScreen("Ready to test this?", 1f);
		engine.setUserInput(false);
		meteorsLeft = 12;
		sec = MAX_SEC;
		play = false;
		engine.newAstral("moon", 96f, Astral.standardPath);
	}

	@Override
	public void drawHUD(Engine engine) {
		// TODO Auto-generated method stub
		SpriteBatch batch = BadNight.badNight.batch;
		stringMeteorsLeft.setLength(0);
		stringMeteorsLeft.append("Meteors left ");
		stringMeteorsLeft.append(meteorsLeft);
		BadNight.badNight.font.draw(batch, stringMeteorsLeft, BadNight.VWIDTH/2f, 16f);
	}

	@Override
	public void step(Engine engine, float dt) {
		// TODO Auto-generated method stub
		if (engine.isShowingMessage()
		|| !engine.isState(State.RUNNING)) return;
		if (!play){
			engine.setUserInput(true);
			play = true;
		}
		
	}

	@Override
	public float getValueForAstral() {
		// TODO Auto-generated method stub
		return timePath;
	}

	@Override
	public float getMaxValueForAstral() {
		// TODO Auto-generated method stub
		return maxTimePath;
	}

	@Override
	protected float getDifficulty() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void newMeteor(int i) {
		// TODO Auto-generated method stub
		
	}

}
