package the_catwolf.BadNight.GameMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.Engine;
import the_catwolf.BadNight.GUI;

public class HowToPlay extends GameMode {

	private boolean showMoveArea;
	private boolean movedLauncher;
	private boolean doneMove;
	
	private boolean showShootArea;
	private boolean shootArea;
	private boolean doneShoot;
	
	private boolean doneFinalText;
	private boolean play;
	
	float time = 3f;
	
	private static String moveLauncherText = "" +
			"Move the missile launcher\n"
			+ "tapping inside the green area";
	
	private static String shootText = "" + 
			"Ok, now shoot inside the red area.\n"
			+ "Touch to store power\n"
			+ "then just lift your finger to shoot.\n"
			+ "Your aim is the point where you lift.";
	
	private static String finalText = "" +
			"Nice! You use those missiles\n"
			+ "for defend those buildings of\n"
			+ "below.\n"
			+ "Shoot at those meteors!\n"
			+ "Don't forget the UFOs...\n"
			//+ "Normally you lose when all\n"
			//+ "your buildings are destroyed\n"
			//+ "but you can practice your aim\n"
			//+ "here.\n"
			+ "Have fun!";
	
	public HowToPlay(String name, int leaderboardId, int achievementId,
			String fileName, int entries) {
		super(name, leaderboardId, achievementId, fileName, entries, false);
		// TODO Auto-generated constructor stub
		this.setLoseIfAllBuildingsAreDestroyed(false);
	}

	@Override
	public void init(Engine engine) {
		// TODO Auto-generated method stub
		showMoveArea = false;
		movedLauncher = false;
		doneMove = false;
		showShootArea = false;
		shootArea = false;
		doneShoot = false;
		doneFinalText = false;
		play = false;
		time = 3f;
	}

	@Override
	public void drawHUD(Engine engine) {
		// TODO Auto-generated method stub
		ShapeRenderer render = BadNight.badNight.shapeRenderer;
				
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			    
		render.setProjectionMatrix(BadNight.badNight.getCamera().combined);
		
		if (!doneMove){
		
			if (showMoveArea && !movedLauncher){
				render.begin(ShapeType.Filled);
				render.setColor(0f, 1f, 0f, 0.25f);
				render.rect(0f, 0f, BadNight.VWIDTH, 80f);
			}
			
			if (showMoveArea && movedLauncher){
				render.begin(ShapeType.Line);
				render.setColor(0f, 1f, 0f, 0.25f);
				render.rect(0f, 0f, BadNight.VWIDTH, 80f);
			}
		
		}
		
		if (doneMove && !doneShoot){
			if (showShootArea && !shootArea){
				render.begin(ShapeType.Filled);
				render.setColor(1f, 0f, 0f, 0.25f);
				render.rect(0f, 80f, BadNight.VWIDTH, BadNight.VHEIGHT-80f);
			}
			if(showShootArea && shootArea){
				render.begin(ShapeType.Line);
				render.setColor(1f, 0f, 0f, 0.25f);
				render.rect(0f, 80f, BadNight.VWIDTH, BadNight.VHEIGHT-80f);
			}
		}
		
		render.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
	}

	@Override
	public void step(Engine engine, float dt){
		if (!doneMove){
			if (!showMoveArea && !movedLauncher){
				engine.pauseAndShowDialog(moveLauncherText);
				showMoveArea = true;
			}
			if (showMoveArea && !movedLauncher && engine.getLauncer().movedAtSometime() ){
				movedLauncher = true;
			}
			
			if (movedLauncher && time > 0f){
				time -= dt;
			}
			if (time <= 0f){
				doneMove = true;
			}
		}
		if (doneMove && !doneShoot){
			if (!showShootArea && !shootArea){
				engine.pauseAndShowDialog(shootText);
				showShootArea = true;
				engine.resetShootAMissile();
				time = 3f;
			}
			if (showShootArea && !shootArea && engine.getShootAMissile()){
				shootArea = true;
			}
			
			if (shootArea && time > 0f){
				time -= dt;
			}
			if (time <= 0f){
				doneShoot = true;
			}
			
		}
		
		if (doneShoot && !doneFinalText){
			engine.pauseAndShowDialog(finalText);
			engine.restoreAllBuildings();
			engine.getLauncer().resetTimeWithoutMoving();
			doneFinalText = true;
		}
		
		if (doneFinalText && engine.getUserInput()){
			play = true;
		}
		
		if (play){
			super.step(engine, dt);
		}
		
		/*
		 * - Add a method to engine to restore only the remaining
		 * buildings
		 */
		
	}
	
	@Override
	protected void newMeteor(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getValueForAstral() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public float getMaxValueForAstral() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	protected float getDifficulty() {
		// TODO Auto-generated method stub
		return 0f;
	}

}
