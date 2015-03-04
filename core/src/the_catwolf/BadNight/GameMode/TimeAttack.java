package the_catwolf.BadNight.GameMode;

import the_catwolf.BadNight.Astral;
import the_catwolf.BadNight.Engine;
import the_catwolf.BadNight.Engine.State;

public class TimeAttack extends GameMode {

	private float time;
	private float maxTime;
		
	//yes, really isn't the maximum or greater but less velocity
	//harder the difficulty is
	
	public TimeAttack(int minutes, String name, int leaderboardId, int achievementId, String fileName, int entries) {
		super(name, leaderboardId, achievementId, fileName, entries);
		maxTime = minutes * 60;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Engine engine) {
		// TODO Auto-generated method stub
		time = 0f;
		engine.newAstral("moon", 96f, Astral.standardPath);
	}

	@Override
	public void drawHUD(Engine engine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(Engine engine, float dt) {
		// TODO Auto-generated method stub
		super.step(engine, dt);
		if (!engine.isState(State.RUNNING) ) return;
		if (time > maxTime){
			engine.setGoodGameOver("The night is over!", 2f);
		}
		time += dt;
	}

	@Override
	public float getValueForAstral() {
		// TODO Auto-generated method stub
		return time;
	}

	@Override
	public float getMaxValueForAstral() {
		// TODO Auto-generated method stub
		return maxTime;
	}	

	@Override
	protected float getDifficulty() {
		// TODO Auto-generated method stub
		return time/maxTime;
	}

	@Override
	public void newMeteor(int i) {
		// TODO Auto-generated method stub
		
	}	
}
