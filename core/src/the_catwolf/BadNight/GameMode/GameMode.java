package the_catwolf.BadNight.GameMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import the_catwolf.BadNight.Engine;
import the_catwolf.BadNight.Engine.Buildings;
import the_catwolf.BadNight.Engine.State;
import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.Physics;
import the_catwolf.BadNight.Score;

public abstract class GameMode {
	
	private CharSequence name;
	private int leaderboardId;
	private int achievementId;
	private Score score;
	private boolean _unlockAchievements;
	
	static protected final float MIN_VELOCITY = 24f;
	static protected final float MAX_VELOCITY = 64f;
	
	static protected final float MIN_UFO_TIME = 8f;
	static protected final float MAX_UFO_TIME = 4f;
	
	static protected final int MIN_METEORS_ON_SCREEN = 7;
	static protected final int MAX_METEORS_ON_SCREEN = 2;
	
	private float spawnMeteorEach = 1f;
	private float timeSpawnMeteor;
	
	private float spawnUfoEach = 7f;
	private float timeSpawnUfo;
	
	private boolean loseIfAllBuildingsAreDestroyed = true;
	private boolean showMenu = true;
	
	public GameMode(String name, int leaderboardId, int achievementId
			, String fileName, int entries, boolean unlockAchievements){
		this.name = name;
		this.leaderboardId = leaderboardId;
		this.achievementId = achievementId;
		score = new Score(fileName, entries, name);
		_unlockAchievements = unlockAchievements;
	}
	
	public GameMode(String name, int leaderboardId, int achievementId, String fileName, int entries){
		this.name = name;
		this.leaderboardId = leaderboardId;
		this.achievementId = achievementId;
		score = new Score(fileName, entries, name);
		_unlockAchievements = true;
	}
	
	protected void resetSpawnTimes(){
		timeSpawnMeteor = 0f;
		timeSpawnUfo = 0f;
	}
	
	public CharSequence getName(){
		return this.name;
	}
		
	public void step(Engine engine, float dt){
		if (!engine.isState(State.RUNNING)) return;
		timeSpawnMeteor += dt;
		if (engine.getUfoOnScreen() == 0) timeSpawnUfo += dt;
		int currentMeteorsOnScreen = getIntValueFromDifficulty(MIN_METEORS_ON_SCREEN, MAX_METEORS_ON_SCREEN);
		Buildings buildings = engine.getBuildings();
		if (timeSpawnMeteor > spawnMeteorEach  && engine.getMeteorsOnScreen() < currentMeteorsOnScreen){
			int iBuilding = MathUtils.random(Engine.Buildings.BUILDINGS-1);
			float x = MathUtils.random(0f, BadNight.VWIDTH);
			float y = BadNight.VHEIGHT+32f;
			Vector2 position = buildings.getBuildingPosition(iBuilding);			
			Vector2 vel = BadNight.getVelocityFromTo(x, y, position.x, position.y, getVelocity());
			float size = Engine.meteorSize[MathUtils.random(Engine.meteorSize.length-1)];
			int i = engine.newMeteor( x, y, vel.x, vel.y, size, Color.WHITE);
			newMeteor(i);
			timeSpawnMeteor = 0f;
			spawnMeteorEach = MathUtils.random(2f, 4f);
		}
		
		if (timeSpawnUfo > spawnUfoEach && engine.getUfoOnScreen() == 0){
			if (engine.getLauncer().getTimeWithoutMoving() > 13f){
				engine.newAggressiveUFO(MathUtils.random(4f, 7f));
			}else{
				engine.newUFO(MathUtils.random(4f, 7f));
			}
			timeSpawnUfo = 0f;
			spawnUfoEach = MathUtils.random(7f, 13f);
		}
		
	}
		
	public abstract void init(Engine engine);
	
	public void resetTimes(){
		timeSpawnUfo = 0f;
		timeSpawnMeteor = 0f;
	}
	
	public abstract void drawHUD(Engine engine);
	
	public float getVelocity(){
		return MIN_VELOCITY + (getDifficulty() * (MAX_VELOCITY - MIN_VELOCITY));
	}
	
	public boolean unlockAchievements(){
		return _unlockAchievements;
	}
	
	protected abstract void newMeteor(int i);
	public abstract float getValueForAstral();
	public abstract float getMaxValueForAstral();
	protected abstract float getDifficulty();
	
	protected void setLoseIfAllBuildingsAreDestroyed(boolean set){
		loseIfAllBuildingsAreDestroyed = set;
	}
	
	public boolean loseIfAllBuildingsAreDestroyed(){
		return loseIfAllBuildingsAreDestroyed;
	}
	
	public int getLeaderboardId(){
		return leaderboardId;
	}
	
	public int getAchievementId(){
		return achievementId;
	}
	
	public Score getScore(){
		return score;
	}
	
	public float getFloatValueFromDifficulty(int min, int max){
		return (float)(min + (getDifficulty() * (max - min)));
	}
	
	public float getFloatValueFromDifficulty(float min, float max){
		return min + (getDifficulty() * (max - min));
	}
	
	public int getIntValueFromDifficulty(int min, int max){
		return (int) (min + (getDifficulty() * (max - min)));
	}
	
	public int getIntValueFromDifficulty(float min, float max){
		return (int) (min + (getDifficulty() * (max - min)));
	}
	
	public float getRandomFloatValueFromDifficulty(float min1, float min2, float max1, float max2){
		float min = getFloatValueFromDifficulty(min1, min2);
		float max = getFloatValueFromDifficulty(max1, max2);
		return MathUtils.random(min, max);
	}
	
	protected void setShowMenu(boolean show){
		showMenu = show;
	}
	
	public boolean getShowMenu(){
		return showMenu;
	}
	
}
