package the_catwolf.BadNight.GameMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.Engine;

public class HowToPlay extends GameMode {

	private boolean showMoveArea;
	private boolean showShootArea;
	
	public HowToPlay(String name, int leaderboardId, int achievementId,
			String fileName, int entries) {
		super(name, leaderboardId, achievementId, fileName, entries, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Engine engine) {
		// TODO Auto-generated method stub
		showMoveArea = true;
		showShootArea = true;
	}

	@Override
	public void drawHUD(Engine engine) {
		// TODO Auto-generated method stub
		ShapeRenderer render = BadNight.badNight.shapeRenderer;
				
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		render.setProjectionMatrix(BadNight.badNight.getCamera().combined);
		render.begin(ShapeType.Filled);
		render.setColor(0f, 1f, 0f, 0.25f);
		render.rect(0f, 0f, BadNight.VWIDTH, 80f);
		render.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
	}

	@Override
	public void step(Engine engine, float dt){
		
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
