package the_catwolf.BadNight.GameMode;

import the_catwolf.BadNight.Astral;
import the_catwolf.BadNight.Engine;
import the_catwolf.BadNight.Engine.State;

public class Resistance extends GameMode {

	private int level = 1;
	private int maxLevel;
	
	static final private int METEORS_PER_LEVEL = 1;
	private int meteorsLeft;
	
	public Resistance(int maxLevel, String name, int leaderboardId, int achievementId, String fileName, int entries) {
		super(name, leaderboardId, achievementId, fileName, entries);
		this.maxLevel = maxLevel;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Engine engine) {
		// TODO Auto-generated method stub
		level = 1;
		meteorsLeft = METEORS_PER_LEVEL;
		engine.newAstral("moon", 96f, Astral.standardPath);
		engine.setMessageOnScreen("level " + level, 2f);
	}

	@Override
	public void drawHUD(Engine engine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(Engine engine, float dt) {
		// TODO Auto-generated method stub
		if (!engine.isState(State.RUNNING) || engine.isShowingMessage() ) return;
		if (meteorsLeft > 0) super.step(engine, dt);
		//System.out.println("lastCount: " + lastCount);
		//System.out.println("meteorsLeft: " + meteorsLeft);
		if (meteorsLeft <= 0 && engine.getMeteorsOnScreen() == 0 && engine.getUfoOnScreen() == 0){
			level += 1;
			if (level > maxLevel){
				engine.setGoodGameOver("You had a good night!", 2f);
			}else{
				meteorsLeft = level * METEORS_PER_LEVEL;
				if (level == maxLevel){
					engine.setMessageOnScreen("last level", 2f);
				}else{
					engine.setMessageOnScreen("level " + level, 2f);
				}
			}
		}
	}

	@Override
	public float getValueForAstral() {
		// TODO Auto-generated method stub
		return level;
	}

	@Override
	public float getMaxValueForAstral() {
		// TODO Auto-generated method stub
		return maxLevel;
	}

	@Override
	protected float getDifficulty() {
		// TODO Auto-generated method stub
		return level/maxLevel;
	}

	@Override
	public void newMeteor(int i) {
		// TODO Auto-generated method stub
		meteorsLeft -= 1;
	}
		
}
