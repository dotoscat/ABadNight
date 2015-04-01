package the_catwolf.BadNight;

import java.util.HashMap;

import the_catwolf.BadNight.CreateParticleEffect.Point;
import the_catwolf.BadNight.Engine.Layer.ArrayLayer;
import the_catwolf.BadNight.Engine.Layer.Drawable;
import the_catwolf.BadNight.Engine.Layer.SparseLayer;
import the_catwolf.BadNight.GUI.MessageWindow;
import the_catwolf.BadNight.MessageSystem.Message;
import the_catwolf.BadNight.MessageSystem.Type;
import the_catwolf.BadNight.GameMode.GameMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.StringBuilder;

public class Engine implements Screen, InputProcessor {
	private TextureAtlas textureAtlas;
	
	public final static float FLOOR = 64f;
	
	final static int ENTITIES = 256;
	final static int PHYSICS_COMPONENT = 0x001;
	final static int GRAPHICS_COMPONENT = 0x002;
	final static int COLLISION_COMPONENT = 0x004;
	final static int UFO_COMPONENT = 0x008;
	final static int LIFESPAN_COMPONENT = 0x010;
	final static int LAUNCHER_COMPONENT = 0x020;
	final static int PARTICLEEFFECT_COMPONENT = 0x040;
	final static int CARRYPARTICLEEFFECT_COMPONENT = 0x080;
	final static int ASTRAL_COMPONENT = 0x100;
	//Some entities will have some particle emiters (particle effect, offset)
	//which will be updated along the physics component
	final static float G = BadNight.VHEIGHT/2f;
	
	public final static float[] meteorSize = {8f, 16f, 32f};//meteor radius
	public static ArrayMap<Float, Long> meteorsPoints;
		
	private MessageSystem messageSystem;
	
	HashMap<String, ParticleEffectPool> particleEffectPool;
	
	private IntArray menuStars;
	private IntArray engineStars;
	
	int[] flags;
	Physics[] physics;
	Graphics[] graphics;
	Collision[] collision;
	IntArray collisionList;
	UFO[] ufo;
	PooledEffect[] particleEffect;//this is for process the particle effects
	DrawableParticleEffect[] drawableParticleEffect;
	//this is for draw them on layers when are needed
	Drawable[] drawable;//Any drawable is stored here for abstraction to be used for destroyEntity
	CreateParticleEffect[] createParticleEffect;
	PowerUp[] powerUp;
	LifeSpan[] lifeSpan;
	Astral[] astral;

	private boolean shootAMissile;
	
	public static interface Layer{
		
		public static abstract class Drawable{
			
			private Layer layer;
			private int index;
			
			abstract public void draw(SpriteBatch batch);
			
			private void setLayer(Layer layer, int index){
				this.layer = layer;
				this.index = index;
			}
			
			private void setLayer(Layer layer){
				this.layer = layer;
			}
			
			private Layer getLayer(){
				return layer;
			}
			
			public void removeFromLayer(){
				if (layer != null){
					layer.remove(this, index);
				}
			}
			
		}
		
		public void add(Drawable aDrawable, int i);
		public void remove(Drawable aDrawable, int i);
		public void removeAll();
		public void draw(SpriteBatch batch);
		
		public static class ArrayLayer implements Layer{

			Array<Drawable> drawable;
			
			public ArrayLayer(int nElements){
				drawable = new Array<Drawable>(false, nElements);
			}
			
			@Override
			public void add(Drawable aDrawable, int i) {
				// TODO Auto-generated method stub
				if (aDrawable.getLayer() != null) return;
				drawable.add(aDrawable);
				aDrawable.setLayer(this, i);
			}

			@Override
			public void remove(Drawable aDrawable, int i) {
				// TODO Auto-generated method stub
				if (aDrawable.getLayer() != this) return;
				drawable.removeValue(aDrawable, true);
				aDrawable.setLayer(null);
			}

			@Override
			public void removeAll() {
				// TODO Auto-generated method stub
				for(Drawable aDrawable: drawable){
					if (aDrawable == null) continue;
					remove(aDrawable, 0);//0 doesn't mind here
				}
			}

			@Override
			public void draw(SpriteBatch batch) {
				// TODO Auto-generated method stub
				for(Drawable aDrawable: drawable){
					if (aDrawable == null){
						continue;
					}
					aDrawable.draw(batch);
				}
			}
			
		}
		
		public static class SparseLayer implements Layer{

			Drawable[] drawable;
			
			public SparseLayer(int nElements){
				drawable = new Drawable[nElements];
			}
			
			@Override
			public void add(Drawable aDrawable, int i) {
				// TODO Auto-generated method stub
				if (aDrawable.getLayer() != null) return;
				drawable[i] = aDrawable;
				aDrawable.setLayer(this, i);
			}

			@Override
			public void remove(Drawable aDrawable, int i) {
				// TODO Auto-generated method stub
				drawable[i].setLayer(null);
				drawable[i] = null;
			}

			@Override
			public void removeAll() {
				// TODO Auto-generated method stub
				for(int i = 0; i < drawable.length; i += 1){
					remove(null, i);//null doesn't mind here
				}
			}

			@Override
			public void draw(SpriteBatch batch) {
				// TODO Auto-generated method stub
				int length = drawable.length;
				for(int i = 0; i < length; i += 1){
					if (drawable[i] == null) continue;
					drawable[i].draw(batch);
				}
			}
			
		}
		
	}
	
	static private class DrawableParticleEffect extends Layer.Drawable{

		private PooledEffect effect;
		private boolean continuous;
		
		@Override
		public void draw(SpriteBatch batch) {
			// TODO Auto-generated method stub
			if (continuous && effect != null){
				Array<ParticleEmitter> emitter = effect.getEmitters();
				for (ParticleEmitter aEmitter: emitter){
					aEmitter.durationTimer = 0f;
					//this is a workaround to a endless emitter
				}
			}
			effect.draw(batch);
		}
		
		public void setEffect(PooledEffect effect){
			this.effect = effect;
			continuous = false;
		}
		
		public void setContinous(){
			continuous = true;
		}
		
	} 
	
	private Layer sky1Layer;
	private Layer sky2Layer;
	private Layer particlesLayer;
	private Layer gameObjectsLayer;
	private Layer particles2Layer;
	private Layer powerUpLayer;
	private Layer launcherLayer;
	
	private Buildings buildings;
	
	private int meteorsOnScreen = 0;
	private int ufoOnScreen = 0;
	
	private int totalUfoInGame;
	private int ufoDestroyed;
	
	StringBuilder messageStringBuilder;
	float messageTime;
	float messageDuration;
	BitmapFont.TextBounds messageTextBounds;
	
	HashMap<String, Animation> animation;
		
	Sprite moveArea;
	Rectangle moveAreaRectangle;
	Sprite HUDbackground;
	
	Rectangle shootArea;
	
	public Window gameOptions;
			
	public TextButton continueButton;
	TextButton submitScoreButton;
		
	public enum State{
		PAUSED,
		//PAUSED_AND_SHOW_MESSAGE,
		RUNNING,
		BAD_GAME_OVER,
		GOOD_GAME_OVER,
		INSERT_RECORD,
		DISPLAY_STARS
	}
	float stateTime = 0f;
	private boolean userInput = true;
	
	private State state = State.RUNNING;
	
	public GameMode gameMode;
		
	class PowerGauge{
		
		private Sprite powerGaugeContainer;
		private Sprite powerGauge;
		
		private float basePower = BadNight.VHEIGHT / 4f;
		private float maxPower = BadNight.VHEIGHT;
		private float maxTime = 0.5f;
		private boolean acum = false;
		private float time = 0f;
		
		public PowerGauge(TextureAtlas atlas, float basePower, float maxPower, float maxTime){
			this.basePower = basePower;
			this.maxPower = maxPower;
			this.maxTime = maxTime;
			powerGaugeContainer = atlas.createSprite("gauge");
			powerGaugeContainer.setY(24f);
			powerGauge = atlas.createSprite("gaugeFill");
			powerGauge.setY(24f);
			updateSprite();
		}
		
		public void startToAcum(){
			acum = true;
		}
		
		public void stopToAcum(){
			acum = false;
			time = 0f;
			updateSprite();
		}
		
		public boolean isAccumulating(){
			return acum;
		}
		
		public void step(float dt){
			if (!acum) return;
			time += dt;
			if (time > maxTime) time = maxTime;
			updateSprite();
		}
		
		public void draw(SpriteBatch batch){
			powerGaugeContainer.draw(batch);
			powerGauge.draw(batch);
		}
		
		public float getPower(){
			float power = basePower + (time * maxPower / maxTime);
			stopToAcum();
			return power;
		}
		
		private void updateSprite(){
			float powerGaugeSize = time * powerGaugeContainer.getWidth() / maxTime;
			powerGauge.setRegionWidth((int) powerGaugeSize);
			powerGauge.setSize(powerGaugeSize, powerGauge.getHeight());
		}
		
	}
	
	private PowerGauge powerGauge;
	
	public class Launcher{
		
		private int base = -1;
		private int cannon = -1;
		private Vector2 offsetLaunchPoint;
		private Vector2 launchPoint;
		private float toX;
		
		private float timeWithoutMoving = 0f;
		
		private float initialTimeParalized;
		private float timeParalized;
		private boolean _movedAtSometime;
		
		public Launcher(float offsetX, float offsetY){
			offsetLaunchPoint = new Vector2(offsetX, offsetY);
			launchPoint = new Vector2();
		}
		
		public void setBase(int i){
			base = i;
			timeParalized = 0f;
			timeWithoutMoving = 0f;
			_movedAtSometime = false;
		}
		
		public int getBase(){
			return base;
		}
		
		public void setCannon(int i){
			cannon = i;
		}
		
		public int getCannon(){
			return cannon;
		}
		
		public void update(Engine engine, float dt){
			Physics basePhysics = engine.physics[base];
			if (timeParalized > 0f){
				if (initialTimeParalized == timeParalized){
					basePhysics.velocity.x = 0f;
					toX = basePhysics.position.x;
				}
				timeParalized -= dt;
				return;
			}
			if (
				(basePhysics.velocity.x > 0f && basePhysics.position.x > toX) ||
				(basePhysics.velocity.x < 0f && basePhysics.position.x < toX)
			){
				Graphics baseGraphics = engine.graphics[base];
				baseGraphics.animationTimeFactor = 0f;
				basePhysics.velocity.x = 0f;
				basePhysics.position.x = toX;
			}
			
			if ((basePhysics.velocity.x == 0f)){
				timeWithoutMoving += dt;
			}
			
			Graphics cannonGraphics = engine.graphics[cannon];
			cannonGraphics.update(basePhysics.position.x, basePhysics.position.y+8f, 0f);
		}
			
		public void updateCannon(Engine engine, Vector3 point){
			if (timeParalized > 0f) return;
			float cannonX = engine.graphics[cannon].sprite.getX();
			float cannonY = engine.graphics[cannon].sprite.getY();
			float angle = MathUtils.atan2(point.y - cannonY, point.x - cannonX);
			engine.graphics[cannon].sprite.setRotation(angle * MathUtils.radiansToDegrees);
			launchPoint.set(offsetLaunchPoint);
			launchPoint.rotateRad(angle);
			Vector2 basePhysicsPosition = engine.physics[base].position;
			launchPoint.add(basePhysicsPosition);
		}
		
		public void moveToX(float x){
			toX = x;
			timeWithoutMoving = 0f;
			_movedAtSometime = true;
		}
		
		public Vector2 getLaunchPoint(){
			return launchPoint;
		}
		
		public float getTimeWithoutMoving(){
			return timeWithoutMoving;
		}
		
		public void setTimeParalized(float time){
			initialTimeParalized = time;
			timeParalized = time;
		}
		
		public boolean isParalized(){
			return timeParalized > 0f;
		}
		
		public boolean movedAtSometime(){
			return _movedAtSometime;
		}
		
		public void resetTimeWithoutMoving(){
			timeWithoutMoving = 0f;
		}
		
	}
	
	private Launcher launcher;
	
	public static class Buildings{
		
		final public static int BUILDINGS = 7;
		
		private int[] building;
		private Vector2[] buildingPosition;
		private int maxBuildings;
		private int buildings;
		private boolean lostHouse = false;
		
		public Buildings(int nBuildings, float buildingSize, float floor, float width){
			maxBuildings = nBuildings;
			building = new int[nBuildings];
			float step = width / nBuildings;
			float margin = step / 2f;
			//float step2 = space / (nBuildings-1);
			this.buildingPosition = new Vector2[nBuildings];
			for(int i = 0; i < nBuildings; i += 1){
				float x = margin + step * i;
				//System.out.println("x: " + x);
				buildingPosition[i] = new Vector2(x, floor);
			}
			reset();
		}
		
		public void reset(){
			for(int i = 0; i < maxBuildings; i += 1){
				building[i] = -1;
			}
			buildings = 0;
			lostHouse = false;
		}
		
		public boolean hasBuildings(){
			return buildings > 0;
		}
		
		public boolean isFull(){
			return buildings == maxBuildings;
		}
		
		public int getBuildingEntityIndex(int position){
			if (position < 0 || position >= maxBuildings) return -1;
			return building[position];
		}
		
		public boolean addBuilding(int position, int i){
			if (position < 0 || position >= maxBuildings || building[position] != -1) return false;
			building[position] = i;
			buildings += 1;
			//System.out.println("addBuilding, now " + buildings + " of " + maxBuildings + " with " + building.length);
			return true;
		}
		
		public int getPositionEntity(int entity){
			int position = -1;
			for(int i = 0; i < maxBuildings; i += 1){
				if (building[i] == entity){
					position = i;
					break;
				}
			}
			return position;
		}
		
		public boolean quitBuilding(int entity){
			if (entity == -1) return false;
			int position = getPositionEntity(entity);
			if (position == -1) return false;
			building[position] = -1;
			buildings -= 1;
			lostHouse = true;
			return true;
		}
		
		public int getMaxBuildings(){
			return maxBuildings;
		}
		
		public int getNumberOfBuildings(){
			return buildings;
		}
		
		public Vector2 getBuildingPosition(int position){
			return buildingPosition[position];
		}
		
		public boolean hasLostHouses(){
			return lostHouse;
		}
		
		public int getRandomValidBuildingIndex(int tries){
			int index = -1;
			for(int i = 0; i < tries; i += i){
				int aPosition = MathUtils.random(maxBuildings-1);
				if (building[aPosition] == -1) continue;
				index = building[aPosition];
				break;
			}
			return index;
		}
				
	}
	
	class PauseGame extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.pause();
		}
		
	}
	
	class ReanudeGame extends ChangeListener{
		
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.resumeEngine();
		}
		
	}
	
	class Retry extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.init();
			BadNight.badNight.getCamera().doBackCome(1.5f);
			BadNight.badNight.playMusic("A Bad Night");
		}
			
	}
	
	class SubmitScore extends ChangeListener{
				
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			if (BadNight.badNight.googleServices.isSignedIn()){
				BadNight.badNight.googleServices.submitScore( BadNight.badNight.engine.getScore().getScore());
				return;
			}
			BadNight.badNight.googleServices.signIn();
		}
	}
			
	class ExitGame extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setMainMenu();
		}
		
	}
	
	static class InsertRecord implements Input.TextInputListener{

		public int iEntry;
		private Engine engine;
		
		public InsertRecord(Engine engine){
			this.engine = engine;
		}
		
		@Override
		public void input(String text) {
			// TODO Auto-generated method stub
			engine.gameMode.getScore().insertNewRecord(iEntry, text, engine.getScore().getScore());
			engine.gameMode.getScore().save();
			BadNight.badNight.scoreScreen.fill(engine.gameMode.getScore());
			BadNight.badNight.scoreScreen.setLastMenu(engine.gameOptions);
			BadNight.badNight.setContainer(BadNight.badNight.scoreScreen, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		}

		@Override
		public void canceled() {
			engine.showPauseMenu();
			// TODO Auto-generated method stub
		}
		
	}
	
	private InsertRecord insertRecord;
	
	public static class Score{
		long score;
		long multiplier;
		private com.badlogic.gdx.utils.StringBuilder scoreStringBuilder;
		private com.badlogic.gdx.utils.StringBuilder multiplierStringBuilder;
						
		private Score(){
			scoreStringBuilder = new StringBuilder();
			multiplierStringBuilder = new StringBuilder();
		}
		
		public void reset(){
			score = 0L;
			multiplier = 1L;
		}
		
		public void resetMultiplier(){
			multiplier = 1L;
		}
		
		public void incrementMultiplier(){
			multiplier += 1L;
		}
		
		public void addPoints(long points, long level, long buildings){
			score += points * level * buildings * multiplier;
		}
		
		public long getScore(){
			return score;
		}
		
		public CharSequence getScoreString(){
			buildScoreString();
			return scoreStringBuilder;
		}
		
		public CharSequence getMultiplierString(){
			buildMultiplierString();
			return multiplierStringBuilder;
		}
		
		private void buildScoreString(){
			scoreStringBuilder.setLength(0);
			scoreStringBuilder.append(score, 12);
		}
		
		private void buildMultiplierString(){
			multiplierStringBuilder.setLength(0);
			multiplierStringBuilder.append('x');
			multiplierStringBuilder.append(multiplier, 2);
		}
		
	}
	
	private Score score;
		
	public Engine(BadNight game){		
		menuStars = new IntArray(ENTITIES);
		engineStars = new IntArray(32);
		
		messageSystem = new MessageSystem(32);
		
		flags = new int[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) flags[i] = 0;
		
		physics = new Physics[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) physics[i] = new Physics();
		
		lifeSpan = new LifeSpan[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) lifeSpan[i] = new LifeSpan();
		
		textureAtlas = game.assets.get("pack.atlas");
		
		animation = new HashMap<String, Animation>();
		animation.put("rocket", new Animation(0.25f, textureAtlas.findRegions("rocket") ) );
		animation.put("ufo", new Animation(0.25f, textureAtlas.findRegions("ufo") ) );
		animation.put("building", new Animation(0.25f, textureAtlas.findRegions("building")) );//building animation
		animation.put("multiplier", new Animation(0.25f, textureAtlas.findRegions("multiplier")) );
		animation.put("points", new Animation(0.25f, textureAtlas.findRegions("points")) );
		animation.put("tank", new Animation(0.25f, textureAtlas.findRegions("tank")) );
		
		graphics = new Graphics[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1){
			graphics[i] = new Graphics();
		}
		
		collision = new Collision[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) collision[i] = new Collision();
		
		collisionList = new IntArray();
		
		ufo = new UFO[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) ufo[i] = new UFO();
		
		astral = new Astral[ENTITIES];
		for(int i = 0; i < ENTITIES; i += 1) astral[i] = new Astral();
		
		particleEffect = new PooledEffect[ENTITIES];//this is for process the particle effect
		drawableParticleEffect = new DrawableParticleEffect[ENTITIES];//this is for draw them on layers
		for (int i = 0; i < ENTITIES; i += 1){
			drawableParticleEffect[i] = new DrawableParticleEffect();
		}
		
		drawable = new Drawable[ENTITIES];
		
		createParticleEffect = new CreateParticleEffect[ENTITIES];
		for (int i = 0; i < ENTITIES; i += 1){
			createParticleEffect[i] = new CreateParticleEffect(8);
		}
		
		powerUp = new PowerUp[ENTITIES];
		
		particleEffectPool = new HashMap<String, ParticleEffectPool>();
		particleEffectPool.put("star", new ParticleEffectPool( (ParticleEffect) game.assets.get("star.particle"), ENTITIES, ENTITIES ));
		particleEffectPool.put("explosion", new ParticleEffectPool( (ParticleEffect) game.assets.get("explosion.particle"), ENTITIES, ENTITIES ));
		particleEffectPool.put("shootingStar", new ParticleEffectPool( (ParticleEffect) game.assets.get("shootingStar.particle"), ENTITIES, ENTITIES ));
		particleEffectPool.put("smoke", new ParticleEffectPool( (ParticleEffect) game.assets.get("smoke.particle"), ENTITIES*2, ENTITIES*4 ));
		particleEffectPool.put("smoke2", new ParticleEffectPool( (ParticleEffect) game.assets.get("smoke2.particle"), ENTITIES*2, ENTITIES*4 ));
		particleEffectPool.put("paralizer", new ParticleEffectPool( (ParticleEffect) game.assets.get("paralizer.particle"), ENTITIES*2, ENTITIES*4 ));
		
		sky1Layer = new ArrayLayer(32);
		sky2Layer = new ArrayLayer(2);
		particlesLayer = new SparseLayer(ENTITIES);
		gameObjectsLayer = new ArrayLayer(32);
		particles2Layer = new SparseLayer(ENTITIES);
		powerUpLayer = new ArrayLayer(8);
		launcherLayer = new ArrayLayer(4);

		score = new Score();
		
		buildings = new Buildings(Buildings.BUILDINGS, 8f, Engine.FLOOR, BadNight.VWIDTH);
		
		messageStringBuilder = new StringBuilder();
		messageTextBounds = new BitmapFont.TextBounds();
		
		moveArea = textureAtlas.createSprite("moveArea");
		moveArea.setY(32);
		moveAreaRectangle = new Rectangle(0f, 0f, BadNight.VWIDTH, moveArea.getY() + moveArea.getHeight());
		HUDbackground = textureAtlas.createSprite("HUD");
		
		final float VWIDTH = BadNight.VWIDTH;
		final float VHEIGHT = BadNight.VHEIGHT;
		
		shootArea = new Rectangle(0f, moveArea.getY()+moveArea.getHeight(), VWIDTH, VHEIGHT);
		powerGauge = new PowerGauge(textureAtlas, BadNight.VHEIGHT/4f, BadNight.VHEIGHT * 0.75f, 0.5f);
		launcher = new Launcher(32f, 0f);
		
		GUI gui = GUI.get();
				
		continueButton = gui.giveMeTextButton("Continue");
		continueButton.addListener(new ReanudeGame());
		
		TextButton retryButton = gui.giveMeTextButton("Retry");
		retryButton.addListener( new Retry() );
		
		TextButton optionsButton = Options.giveMeAOptionsButton(gameOptions);
		TextButton exitButton = gui.giveMeTextButton("Exit");
		
		submitScoreButton = gui.giveMeTextButton("Submit score");
		submitScoreButton.addListener( new SubmitScore() );
		
		exitButton.addListener(new ExitGame());
				
		gameOptions = GUI.get().giveMeWindow("");
		
		gameOptions.pad(GUI.ELEMENT_SPACE2);
		gameOptions.padTop(GUI.ELEMENT_SPACE3);
		gameOptions.setWidth(BadNight.VWIDTH/2f);
		gameOptions.setBackground(gui.giveMeWindowsDrawable());
		gameOptions.setModal(false);
		gameOptions.setMovable(false);
		gameOptions.setResizable(false);
		
		gameOptions.add(continueButton).fill().space(GUI.ELEMENT_SPACE2).row();
		gameOptions.add(retryButton).fill().space(GUI.ELEMENT_SPACE2).row();
		gameOptions.add(optionsButton).fill().space(GUI.ELEMENT_SPACE2).row();
		gameOptions.add(submitScoreButton).fill().space(GUI.ELEMENT_SPACE2).row();
		gameOptions.add(exitButton).fill().space(GUI.ELEMENT_SPACE2);
		
		insertRecord = new InsertRecord(this);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		if (state == State.RUNNING && gameMode.loseIfAllBuildingsAreDestroyed()
				&& !buildings.hasBuildings()){
			setBadGameOver("You really had a bad night...", 2f);
		}
		
		//System.out.println(state.toString());
		
		if ((state == State.BAD_GAME_OVER || state == State.GOOD_GAME_OVER) && stateTime < 0f){
			if (state == State.GOOD_GAME_OVER){
				if (!buildings.hasLostHouses()){
					BadNight.badNight.unlockAchievement(BadNight.achievement.get("achievement_good_defender"));
				}
				if (buildings.getNumberOfBuildings() == Buildings.BUILDINGS){
					BadNight.badNight.unlockAchievement(BadNight.achievement.get("achievement_here_nothing_happened"));
				}
				if (ufoDestroyed == totalUfoInGame){
					BadNight.badNight.unlockAchievement(BadNight.achievement.get("achievement_ufo_destroyer"));
				}
				if (!launcher.movedAtSometime()){
					BadNight.badNight.unlockAchievement(BadNight.achievement.get("achievement_lazy"));
				}
				BadNight.badNight.unlockAchievement(gameMode.getAchievementId());//achievement by do this mode!
			}
			int iEntry = gameMode.getScore().getNewRecordIndex(score.getScore());
			state = State.INSERT_RECORD;
			continueButton.setVisible(false);
			submitScoreButton.setVisible(true);
			if (iEntry > -1){
				insertRecord.iEntry = iEntry;
				Gdx.input.getTextInput(insertRecord, "New Record!", "player", null);
			}else{
				showPauseMenu();
			}
		}else if ((state == State.BAD_GAME_OVER || state == State.GOOD_GAME_OVER) && stateTime >= 0f){
			stateTime -= delta;
		}
		
		if (state == State.INSERT_RECORD){
			if (BadNight.badNight.googleServices.isSignedIn()){
				submitScoreButton.setText("Submit score");
			}else{
				submitScoreButton.setText("Sign in to submit score");	
			}
		}

		if (messageTime < messageDuration) messageTime += delta;
		
		if (state == State.RUNNING && !BadNight.badNight.getCamera().isMoving()){
			powerGauge.step(delta);
			if (gameMode != null)gameMode.step(this, delta);
		}
		
		if (state == State.DISPLAY_STARS || 
			state == State.RUNNING ){
			processMessages();
			processEntities(delta);
			response();
		}
		
		SpriteBatch batch = BadNight.badNight.batch;
		batch.begin();
		
		sky1Layer.draw(batch);
		sky2Layer.draw(batch);
		
		if (state != State.DISPLAY_STARS){
		
			HUDbackground.draw(batch);
			powerGauge.draw(batch);
			moveArea.draw(batch);
		
		}
		
		particlesLayer.draw(batch);
		gameObjectsLayer.draw(batch);
		powerUpLayer.draw(batch);
		particles2Layer.draw(batch);
		launcherLayer.draw(batch);
				
		BitmapFont font = BadNight.badNight.font;
		if (state != State.DISPLAY_STARS){
		font.draw(batch, score.getScoreString(), 8f, 16f);
		
		//if(pointMultiplier > 1L){
			font.draw(batch, score.getMultiplierString(), BadNight.VWIDTH/2f-32f, 16f);
		//}
		if (isShowingMessage()){
			font.draw(batch, messageStringBuilder, (BadNight.VWIDTH-messageTextBounds.width)/2f, BadNight.VHEIGHT/2f);
		}
		}
		if (gameMode != null && state != State.DISPLAY_STARS || ( state == State.RUNNING && !BadNight.badNight.getCamera().isMoving() ) )gameMode.drawHUD(this);
			
		batch.end();
		
		if(gameMode != null) gameMode.drawHUD(this);
	}

	private void processMessages(){
		for(Message aMessage: messageSystem){
			//System.out.println(aMessage.getOrigin());
			switch(aMessage.type){
			case CREATE_EXPLOSION:
				newExplosion(aMessage.positionX, aMessage.positionY);
				BadNight.badNight.playSound("explosion", MathUtils.random(0.9f, 1.1f));
				break;
			case CREATE_METEOR:
				newMeteor(aMessage.positionX, aMessage.positionY, aMessage.velX, aMessage.velY, aMessage.value, aMessage.sprite);
				break;
			case DESTROY_ENTITY:
				int i = aMessage.i;
				if ((flags[i] & COLLISION_COMPONENT) != 0 && collision[i].isA == Collision.BUILDING){
					buildings.quitBuilding(i);
					score.resetMultiplier();
				}
				else if ((flags[i] & COLLISION_COMPONENT) != 0 && collision[i].isA == Collision.METEOR){
					meteorsOnScreen -= 1L;
				}
				else if ((flags[i] & UFO_COMPONENT) != 0){
					BadNight.badNight.stopSound("UFO");
					ufoOnScreen -= 1;
					ufoDestroyed += 1;
				}
				destroyEntity(i);
				break;
			case CREATE_POWERUP:
				int ordinal = MathUtils.random(PowerUp.Type.values().length-1);
				PowerUp.Type powerUpType = PowerUp.Type.values()[ordinal];
				while(buildings.isFull() && powerUpType == PowerUp.Type.EXTRA_HOUSE){
					ordinal = MathUtils.random(PowerUp.Type.values().length-1);
					powerUpType = PowerUp.Type.values()[ordinal];
				}
				newPowerUp(aMessage.positionX, aMessage.positionY, powerUpType);
				break;
			case CREATE_HOUSE:
				newBuilding();
				break;
			case CREATE_PARTICLE:
				newParticleEffect(aMessage.key, aMessage.positionX, aMessage.positionY, aMessage.layer);
				break;
			case CREATE_UFO:
				newUFO(aMessage.value);
				break;
			case DROP_PARALIZER:
				newParalizer(aMessage.positionX, aMessage.positionY, aMessage.value);
				BadNight.badNight.playSound("paralizerShoot", MathUtils.random(0.9f, 1.1f));
				break;
			case CREATE_PARALIZED_EFFECT:
				newParalizedEffect(aMessage.positionX, aMessage.positionY, aMessage.value);
				BadNight.badNight.playSound("paralizerHit", MathUtils.random(0.9f, 1.1f));
				break;
			case NONE:
				break;
			default:
				break;
			
			}
		}
	}
	
	private void processEntities(float dt){
		
		for(int i = 0; i < ENTITIES; i += 1){
			if (flags[i] == 0) continue;
															
			if ((flags[i] & PHYSICS_COMPONENT) == PHYSICS_COMPONENT){
				physics[i].update(dt, -G);
			}
			
			if ( (flags[i] & UFO_COMPONENT) == UFO_COMPONENT){
				if (ufo[i].process(physics[i], dt) ){
					Message aMessage = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy ufo");
					aMessage.i = i;
				}
				ufo[i].think(physics[i], this);
			}
			
			if ( (flags[i] & LAUNCHER_COMPONENT) == LAUNCHER_COMPONENT){
				//the launcher already stores the base entity and the cannon entity
				launcher.update(this, dt);
				if (powerGauge.isAccumulating()){
					launcher.updateCannon(this, BadNight.badNight.point);
				}
			}
			
			if(( flags[i] & (PHYSICS_COMPONENT | COLLISION_COMPONENT)) == (PHYSICS_COMPONENT | COLLISION_COMPONENT)){
				Vector2 position = physics[i].position;
				collision[i].update(position.x, position.y);
			}
			
			if ( (flags[i] & (ASTRAL_COMPONENT | PHYSICS_COMPONENT)) == (ASTRAL_COMPONENT | PHYSICS_COMPONENT)){
				astral[i].update(physics[i], gameMode.getValueForAstral(), gameMode.getMaxValueForAstral());
			}
			
			if(( flags[i] & (PHYSICS_COMPONENT | GRAPHICS_COMPONENT)) == (PHYSICS_COMPONENT | GRAPHICS_COMPONENT)){
				Vector2 position = physics[i].position;
				graphics[i].update(position.x, position.y, dt);
				if (graphics[i].setRotationFromSpeed){
					graphics[i].sprite.setRotation(physics[i].velocity.angle());
				}
			}
			
			if ((flags[i] & (CARRYPARTICLEEFFECT_COMPONENT | GRAPHICS_COMPONENT | PHYSICS_COMPONENT))
					== (CARRYPARTICLEEFFECT_COMPONENT | GRAPHICS_COMPONENT | PHYSICS_COMPONENT)){
				Vector2 pos = physics[i].position;
				float rotation = graphics[i].sprite.getRotation();
				createParticleEffect[i].process(pos, rotation, this);
			}
			
			if ((flags[i] & PARTICLEEFFECT_COMPONENT) != 0){
				if ((flags[i] & PHYSICS_COMPONENT) == PHYSICS_COMPONENT){
					Vector2 pos = physics[i].position;
					particleEffect[i].setPosition(pos.x, pos.y);
				}
				particleEffect[i].update(dt);
				if ((flags[i] & PARTICLEEFFECT_COMPONENT) != 0 && particleEffect[i].isComplete()){
					//System.out.println("particle " + i + " completed");
					Message aMessage = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy paricle effect component");
					aMessage.i = i;
				}
			}
			
			if( (flags[i] & LIFESPAN_COMPONENT) != 0 ){
				if (!lifeSpan[i].isOver()){
					lifeSpan[i].update(dt);
					if ((flags[i] & GRAPHICS_COMPONENT) != 0 && lifeSpan[i].isAboutToDissappear()){
						if (lifeSpan[i].show){
							graphics[i].sprite.setAlpha(1f);
						}else{
							graphics[i].sprite.setAlpha(0f);
						}
					}
				}
				else{
					Message message = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy, life span ends");
					message.i = i;
				}
			}
		}
	}
	
	private void response(){
		int collisionListSize = collisionList.size;
		int[] collisionListItems = collisionList.items;
		
		for(int i = 0; i < collisionListSize; i += 1){
			
			int id = collisionListItems[i];
			if (collision[id].circle.y - collision[id].circle.radius < FLOOR && physics[id].velocity.y < 0f){
				//is an object is falling and touches the floor
				if (powerUp[id] == null){
					//This is for destroy falling objects such rockets and meteors, no power ups
					Message message = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket or meteor");
					message.i = id;
						
					Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "create explosion");
					createExplosion.positionX = physics[id].position.x;
					createExplosion.positionY = physics[id].position.y;
						
					continue;
				}
				else if (powerUp[id] != null){
					//this is power ups
					physics[id].position.y = FLOOR + collision[id].circle.radius;
					physics[id].acceleration.y = 0f;
					physics[id].velocity.y = 0f;
					physics[id].gravityScale = 0f;
					Vector2 position = physics[id].position;
					graphics[id].update(position.x, position.y, 0f);
					continue;
				}
			}
			Rectangle entityRectangle = graphics[id].sprite.getBoundingRectangle();
			if ( (entityRectangle.x + entityRectangle.width < 0f && physics[id].velocity.x < 0f
				|| entityRectangle.x > BadNight.VWIDTH && physics[id].velocity.x > 0f)
			){
				//This is for entities like UFOs
				Message message = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy ufo");
				message.i = id;
				continue;
			}
							
			for(int j = 0; j < collisionListSize; j += 1){
					
				int id2 = collisionListItems[j];
				if (id2 == id) continue;
								
				if ( collision[id].circle.overlaps( collision[id2].circle ) ){
					//dispatch here
					if (collision[id].isA == Collision.ROCKET && collision[id2].isA == Collision.METEOR){
						Message destroyRocket = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket");
						destroyRocket.i = id;
								
						Message destroyMeteor = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy meteor by rocket");
						destroyMeteor.i = id2;
								
						Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "");
						createExplosion.positionX = physics[id2].position.x;
						createExplosion.positionY = physics[id2].position.y;
								
						float radius = collision[id2].circle.radius;
						if (radius > meteorSize[0]){
							Vector2 position = physics[id2].position;
							Vector2 velocity = physics[id2].velocity;
							float angle = velocity.angle();
							//int n = MathUtils.random(2, 3);
							int n = 2;
							for(int piece = 0; piece < n; piece += 1){
								Message createMeteor = messageSystem.addMessage(Type.CREATE_METEOR, "splitting meteor");
								createMeteor.positionX = position.x;
								createMeteor.positionY = position.y;
								float newMeteorAngle = angle + (-22f + piece * 44f);
								newMeteorAngle *= MathUtils.degreesToRadians;
								float speed = gameMode.getVelocity();
								createMeteor.velX = MathUtils.cos(newMeteorAngle) * speed;
								createMeteor.velY = MathUtils.sin(newMeteorAngle) * speed;
								createMeteor.value = collision[id2].circle.radius / 2f;
								createMeteor.color = Color.WHITE;
								createMeteor.sprite = graphics[id2].sprite;
							}
						}
						manageHit(physics[id], collision[id2], meteorsPoints.get(radius));	
						BadNight.badNight.vibrate(70);
								
						break;
					}
					if (collision[id].isA == Collision.METEOR && collision[id2].isA == Collision.BUILDING){
						//Message destroyMeteor = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy meteor by building");
						//destroyMeteor.i = id;
						
						Message destroyBuilding = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy building");
						destroyBuilding.i = id2;
								
						Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "");
						createExplosion.positionX = physics[id2].position.x;
						createExplosion.positionY = physics[id2].position.y;
						BadNight.badNight.vibrate(500);
								
						continue;
					}
					if (collision[id].isA == Collision.ROCKET && collision[id2].isA == Collision.BUILDING){
						if (physics[id].velocity.y > 0f){
							continue;
						}
						Message destroyRocket = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket");
						destroyRocket.i = id;
							
						Message destroyBuilding = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy building");
						destroyBuilding.i = id2;
							
						Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "");
						createExplosion.positionX = physics[id2].position.x;
						createExplosion.positionY = physics[id2].position.y;
						BadNight.badNight.vibrate(500);
							
						break;
					}
					if (collision[id].isA == Collision.ROCKET && collision[id2].isA == Collision.UFO){
						Message destroyRocket = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket");
						destroyRocket.i = id;
							
						Message destroyBuilding = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy ufo");
						destroyBuilding.i = id2;
							
						Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "");
						createExplosion.positionX = physics[id2].position.x;
						createExplosion.positionY = physics[id2].position.y;
							
						manageHit(physics[id], collision[id2], 1000L);	
						score.addPoints(1000L, 1L, buildings.getMaxBuildings());
						BadNight.badNight.vibrate(50);
							
						Message createPowerUp = messageSystem.addMessage(Type.CREATE_POWERUP, "");
						createPowerUp.positionX = physics[id2].position.x;
						createPowerUp.positionY = physics[id2].position.y;
							
						break;
					}
					if (collision[id].isA == Collision.ROCKET && collision[id2].isA == Collision.ROCKET){
						Message destroyRocket = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket");
						destroyRocket.i = id;
							
						Message destroyRocket2 = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy rocket");
						destroyRocket2.i = id2;
							
						Message createExplosion = messageSystem.addMessage(Type.CREATE_EXPLOSION, "");
						createExplosion.positionX = physics[id2].position.x;
						createExplosion.positionY = physics[id2].position.y;
							
						BadNight.badNight.playSound("explosion", MathUtils.random(0.9f, 1.1f));
						BadNight.badNight.vibrate(70);
							
						break;
					}
					if (collision[id].isA == Collision.LAUNCHER && collision[id2].isA == Collision.POWER_UP){
						Message destroyPowerUp = messageSystem.addMessage(Type.DESTROY_ENTITY, "destroy power up");
						destroyPowerUp.i = id2;
						powerUp[id2].effect(this);
						BadNight.badNight.playSound("powerUp", MathUtils.random(0.9f,1f));
						break;
					}
					if (collision[id].isA == Collision.PARALIZER && collision[id2].isA == Collision.LAUNCHER){
						Message destroyParalizer = messageSystem.addMessage(Type.DESTROY_ENTITY, "");
						destroyParalizer.i = id;
						
						Message createParalizedEffect = messageSystem.addMessage(Type.CREATE_PARALIZED_EFFECT,  "");
						createParalizedEffect.positionX = physics[id2].position.x;
						createParalizedEffect.positionY = physics[id2].position.y;
						createParalizedEffect.value = 3f;
						
						launcher.setTimeParalized(3f);
						powerGauge.stopToAcum();
					}
				}
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		//pause();
	}

	public void animatedPause(){
		if (state != State.RUNNING) return;
		showAnimatedPauseMenu();
		userPause();
		setUserInput(false);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if (state != State.RUNNING) return;
		if (gameMode != null && !gameMode.getShowMenu()) return;
		showPauseMenu();
		userPause();
		setUserInput(false);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
		
	public int newEntity(int newFlags){
		int index = -1;
		for(int i = 0; i < ENTITIES && index == -1; i += 1){
			if (flags[i] == 0){
				flags[i] = newFlags;
				index = i;
			}
		}
		if (index != -1 && (newFlags & COLLISION_COMPONENT) != 0){
			collisionList.add(index);
		}
		return index;
	}

	public int newEntity(int newFlags, Layer layer){
		int i = newEntity(newFlags);
		if ((newFlags & GRAPHICS_COMPONENT) != 0){
			layer.add(graphics[i], i);
			drawable[i] = graphics[i];
		}
		if ((newFlags & PARTICLEEFFECT_COMPONENT) != 0){
			layer.add(drawableParticleEffect[i], i);
			drawable[i] = drawableParticleEffect[i];
		}
		return i;
	}
	
	public void newBuilding(){
		if (buildings.isFull()) return;
		int position = MathUtils.random(buildings.getMaxBuildings()-1);
		while(buildings.getBuildingEntityIndex(position) != -1)position = MathUtils.random(buildings.getMaxBuildings()-1);
		Vector2 vposition = buildings.getBuildingPosition(position);
		int i = newBuilding(vposition.x, vposition.y);
		buildings.addBuilding(position, i);
	}
	
	public int newBuilding(float x, float y){
		int i = newEntity(PHYSICS_COMPONENT | COLLISION_COMPONENT | GRAPHICS_COMPONENT, gameObjectsLayer);
		if (i < 0) return i;
		
		physics[i].reset();
		physics[i].position.set(x, y);
		collision[i].reset();
		collision[i].circle.radius = 8f;
		collision[i].circleOffsetY = 8f;
		collision[i].isA = Collision.BUILDING;
		collision[i].collidesWith = Collision.METEOR;
		graphics[i].reset();
		if (MathUtils.randomBoolean()){
			Engine.setSpriteWithAtlas(graphics[i].sprite, textureAtlas, "building1");
		}else{
			Engine.setSpriteWithAtlas(graphics[i].sprite, textureAtlas, "building2");
		}
		graphics[i].sprite.setSize(16, 16);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.x = -8f;
		graphics[i].spriteOffset.y = 0f;	
		return i;
	}

	public int newMeteor(float x, float y, float velX, float velY, float radius, Sprite sprite){
		
		int i = newMeteor(x, y, velX, velY, radius);
		if (i < 0) return i;
				
		graphics[i].reset();
		graphics[i].sprite.set(sprite);
		graphics[i].sprite.setSize(radius*2f, radius*2f);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.x = -radius;
		graphics[i].spriteOffset.y = -radius;
		graphics[i].sprite.setColor(Color.WHITE);
		graphics[i].sprite.setRotation(MathUtils.random(360f));

		return i;
	}
	
	public int newMeteor(float x, float y, float velX, float velY, float radius, String key){
		
		int i = newMeteor(x, y, velX, velY, radius);
		if (i < 0) return i;
				
		graphics[i].reset();
		setSpriteWithAtlas(graphics[i].sprite, textureAtlas, key);
		graphics[i].sprite.setSize(radius*2f, radius*2f);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.x = -radius;
		graphics[i].spriteOffset.y = -radius;
		graphics[i].sprite.setColor(Color.WHITE);
		graphics[i].sprite.setRotation(MathUtils.random(360f));

		return i;
	}
	
	private int newMeteor(float x, float y, float velX, float velY, float radius){
		int i = newEntity(PHYSICS_COMPONENT | COLLISION_COMPONENT | GRAPHICS_COMPONENT /*| CARRYPARTICLEEFFECT_COMPONENT*/, gameObjectsLayer);
		if (i < 0) return i;
		
		physics[i].reset();
		physics[i].position.set(x, y);
		physics[i].velocity.x = velX;
		physics[i].velocity.y = velY;
		collision[i].reset();
		collision[i].circle.radius = radius;
		collision[i].isA = Collision.METEOR;
		collision[i].collidesWith = Collision.ROCKET;

		meteorsOnScreen += 1L;
		
		return i;
	}
	
	public int newAstral(String key, float size, Vector2[] path){
		int i = newEntity(GRAPHICS_COMPONENT | PHYSICS_COMPONENT | ASTRAL_COMPONENT, sky2Layer);
		astral[i].set(path);
		physics[i].reset();
		physics[i].position.setZero();
		graphics[i].reset();
		Engine.setSpriteWithAtlas(graphics[i].sprite, textureAtlas, "moon");
		graphics[i].sprite.setSize(size, size);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.set(
			graphics[i].sprite.getWidth()/-2f,
			graphics[i].sprite.getHeight()/-2f
		);
		return i;
	}
	
	public int newMissile(float x, float y, float targetX, float targetY, float speed){
		int i = newEntity(PHYSICS_COMPONENT | COLLISION_COMPONENT | GRAPHICS_COMPONENT | CARRYPARTICLEEFFECT_COMPONENT, gameObjectsLayer);
		if (i < 0) return i;
		
		physics[i].reset();
		physics[i].position.set(x, y);
		physics[i].gravityScale = 1f;
		float angle = MathUtils.atan2(targetY - y, targetX- x);
		physics[i].velocity.x = MathUtils.cos(angle) * speed;
		physics[i].velocity.y = MathUtils.sin(angle) * speed;
		collision[i].reset();
		collision[i].circle.radius = 8f;
		collision[i].isA = Collision.ROCKET;
		collision[i].collidesWith = Collision.METEOR;
		graphics[i].reset();
		Animation animationRocket = animation.get("rocket");
		graphics[i].animation = animationRocket;
		graphics[i].animationStateTime = MathUtils.random(animationRocket.getAnimationDuration());
		graphics[i].sprite.setSize(16f, 16f);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.x = -16f/2f;
		graphics[i].spriteOffset.y = -16f/2f;
		graphics[i].setRotationFromSpeed = true;
		
		createParticleEffect[i].setUsedPoints(1);
		Point aPoint = createParticleEffect[i].getPoint(0);
		aPoint.offset.set(-8f, 0f);
		aPoint.key = "smoke";
		aPoint.layer = particlesLayer;
		
		shootAMissile = true;
		
		return i;
	}
	
	public int newParalizer(float x, float y, float seconds){
		int i = newContinuousParticleEffect("paralizer", x, y, gameObjectsLayer);
		if (i == -1) return i;
		flags[i] |= PHYSICS_COMPONENT | COLLISION_COMPONENT;
		
		physics[i].reset();
		physics[i].gravityScale = 1f;
		physics[i].position.set(x, y);
		
		collision[i].reset();
		collision[i].isA = Collision.PARALIZER;
		collision[i].circle.radius = 16f;
		
		collisionList.add(i);
		
		return i;
	}
	
	public int newParalizedEffect(float x, float y, float seconds){
		int i = newContinuousParticleEffect("paralizer", x, y, launcherLayer);
		
		flags[i] |= LIFESPAN_COMPONENT;
		lifeSpan[i].set(3f);
		
		return i;
	}
	
	public int newUFO(float maxTime){
		int i = newEntity(UFO_COMPONENT | PHYSICS_COMPONENT | COLLISION_COMPONENT | GRAPHICS_COMPONENT, gameObjectsLayer);
		if (i < 0) return i;
			
		physics[i].reset();
		collision[i].reset();
		collision[i].circle.radius = 8f;
		collision[i].isA = Collision.UFO;
		collision[i].collidesWith = Collision.ROCKET;
		graphics[i].reset();
		graphics[i].animation = animation.get("ufo");
		graphics[i].sprite.setSize(16f, 16f);
		graphics[i].sprite.setOriginCenter();
		graphics[i].spriteOffset.x = -16f/2f;
		graphics[i].spriteOffset.y = -16f/2f;
		
		ufo[i].setSoundID(BadNight.badNight.loopSound("UFO", MathUtils.random(0.9f, 1.1f)));
		ufo[i].init(MathUtils.random(7f, 12f), BadNight.VHEIGHT/2f, BadNight.VHEIGHT, 16f);
		
		ufoOnScreen += 1;
		totalUfoInGame += 1;
		
		return i;
	}
	
	public int newAggressiveUFO(float maxTime){
		int i = newUFO(maxTime);
		
		ufo[i].hostile = true;
		graphics[i].sprite.setColor(1f, 0.5f, 0.5f, 1f);
		
		return i;
	}
	
	public void newExplosion(float x, float y){
		newParticleEffect("explosion", x, y, particles2Layer);
	}

	public void newMissileLauncher(){
		int base = newEntity(LAUNCHER_COMPONENT | COLLISION_COMPONENT | PHYSICS_COMPONENT | GRAPHICS_COMPONENT, launcherLayer);
		launcher.setBase(base);
		physics[base].reset();
		physics[base].position.set(BadNight.VWIDTH / 2f, FLOOR);
		graphics[base].reset();
		graphics[base].animation = animation.get("tank");
		graphics[base].animationTimeFactor = 0f;
		graphics[base].sprite.setSize(32f, 32f);
		graphics[base].sprite.setOriginCenter();
		graphics[base].spriteOffset.x = -32/2f;
		graphics[base].spriteOffset.y = -32/2f;
		collision[base].reset();
		collision[base].isA = Collision.LAUNCHER;
		collision[base].circle.radius = 16f;
		collision[base].circleOffsetY = 16f;
		
		int cannon = newEntity(GRAPHICS_COMPONENT, launcherLayer);
		launcher.setCannon(cannon);
		graphics[cannon].reset();
		Engine.setSpriteWithAtlas(graphics[cannon].sprite, textureAtlas, "cannon");
		graphics[cannon].sprite.setSize(32f, 16f);
		graphics[cannon].sprite.setOrigin(8f, 8f);
		graphics[cannon].spriteOffset.x = -8f;
		graphics[cannon].spriteOffset.y = -8f;
		
		BadNight.badNight.point.set(BadNight.VWIDTH/2f, BadNight.ENGINE_POSITION, 0f);
		launcher.updateCannon(this, BadNight.badNight.point);
	}
	
	public int newContinuousParticleEffect(String key, float x, float y, Layer layer){
		int i = newParticleEffect(key, x, y, layer);
		drawableParticleEffect[i].setContinous();
		return i;
	}
	
	public int newParticleEffect(String key, float x, float y, Layer layer){
		int i = newEntity(PARTICLEEFFECT_COMPONENT, layer);
		particleEffect[i] = particleEffectPool.get(key).obtain();
		drawableParticleEffect[i].setEffect(particleEffect[i]);
		particleEffect[i].setPosition(x, y);
		particleEffect[i].start();
		return i;
	}
		
	public int newPowerUp(float x, float y, PowerUp.Type type){
		int i = newEntity(PHYSICS_COMPONENT | COLLISION_COMPONENT | GRAPHICS_COMPONENT | LIFESPAN_COMPONENT, powerUpLayer);
		if (i < 0) return -1;
		
		physics[i].reset();
		physics[i].position.set(x, y);
		physics[i].gravityScale = 0.5f;
		collision[i].reset();
		collision[i].isA = Collision.POWER_UP;
		collision[i].circle.radius = 8f;
		graphics[i].reset();
		switch(type){
		case EXTRA_HOUSE:
			graphics[i].animation = animation.get("building");
			powerUp[i] = PowerUp.powerUp[type.ordinal()];
			break;
		case EXTRA_POINTS:
			graphics[i].animation = animation.get("points");
			powerUp[i] = PowerUp.powerUp[type.ordinal()];
			break;
		case INCREMENT_MULTIPLIER:
			graphics[i].animation = animation.get("multiplier");
			powerUp[i] = PowerUp.powerUp[type.ordinal()];
			break;
		default:
			break;
		
		}
		graphics[i].sprite.setSize(16f, 16f);
		graphics[i].spriteOffset.x = -8f;
		graphics[i].spriteOffset.y = -8f;
		lifeSpan[i].set(12f);
		
		return i;
	}
	
	public void destroyEntity(int i){
		/*
		if (i == launcher.getBase()){
			System.out.println("Destroy base");
		}
		if (i == launcher.getCannon()){
			System.out.println("Destroy cannon");
		}
		*/
		if (drawable[i] != null){
			drawable[i].removeFromLayer();
			drawable[i] = null;
		}
		if (particleEffect[i] != null){
			particleEffect[i].free();
			particleEffect[i] = null;
		}
		powerUp[i] = null;
		graphics[i].reset();
		collisionList.removeValue(i);
		flags[i] = 0;
	}
	
	public void resumeEngine(){
		BadNight.badNight.getContainers().hide(BadNight.CURRENT_LEAVE, 1f);
		state = State.RUNNING;
		continueUFOSounds();
		setUserInput(true);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if (BadNight.badNight.getCamera().isMoving()){
			return false;
		}
		boolean pause = keycode == Input.Keys.P || keycode == Input.Keys.ENTER
				|| keycode == Input.Keys.MENU || keycode == Input.Keys.BACK;
		if (pause && state == State.RUNNING){
			animatedPause();
		}
		else if (pause && state == State.PAUSED){
			resumeEngine();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (!userInput || BadNight.badNight.getCamera().isMoving()){
			return false;
		}
		Vector3 point = BadNight.badNight.point;
		point.x = screenX;
		point.y = screenY;
		BadNight.badNight.getCamera().unproject(point);
		if (!launcher.isParalized() && moveAreaRectangle.contains(point.x, point.y)){
			launcher.moveToX(point.x);
			int base = launcher.getBase();
			physics[base].velocity.x = BadNight.VWIDTH / 2f;
			graphics[base].animationTimeFactor = 1f;
			graphics[base].animation.setPlayMode(PlayMode.LOOP);
			if (point.x < physics[base].position.x){
				physics[base].velocity.x = -physics[base].velocity.x;
				graphics[base].animation.setPlayMode(PlayMode.LOOP_REVERSED);
			}
			return false;
		}
		if (!launcher.isParalized() && shootArea.contains(point.x, point.y)){
			powerGauge.startToAcum();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (!userInput || launcher.isParalized() || BadNight.badNight.getCamera().isMoving() ) return false;
		if (!powerGauge.isAccumulating()) return false;
		Vector3 point = BadNight.badNight.point;
		point.x = screenX;
		point.y = screenY;
		BadNight.badNight.getCamera().unproject(point);
		if (!shootArea.contains(point.x, point.y)){
			powerGauge.stopToAcum();
			return false;
		}
		Vector2 launchPoint = launcher.getLaunchPoint();//physics[launcher.getBase()].position;
		newMissile(launchPoint.x, launchPoint.y, point.x, point.y, powerGauge.getPower());
		BadNight.badNight.playSound("launcher", MathUtils.random(0.9f, 1f));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		if (!userInput || launcher.isParalized() || BadNight.badNight.getCamera().isMoving() ) return false;
		Vector3 point = BadNight.badNight.point;
		point.x = screenX;
		point.y = screenY;
		BadNight.badNight.getCamera().unproject(point);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	static public void staticInit(){
		//public final static float[] meteorSize = {8f, 16f, 32f};//meteor radius
		meteorsPoints = new ArrayMap<Float, Long>(3);
		meteorsPoints.put(meteorSize[0], 8L);
		meteorsPoints.put(meteorSize[1], 6L);
		meteorsPoints.put(meteorSize[2], 3L);
	}
	
	public void init(){
		continueButton.setVisible(true);
		submitScoreButton.setVisible(false);
		clean(menuStars);
		createNewStars(MathUtils.random(12, 22), 0f, FLOOR, BadNight.VWIDTH, BadNight.VHEIGHT-FLOOR, engineStars);
		//fill the buildings in order...
		{
			//final float margin = 32f;
			//final float width = BadNight.VWIDTH - margin;
			//final float step = width / Buildings.BUILDINGS;
			for(int i = 0; i < Buildings.BUILDINGS; i += 1){
				Vector2 position = buildings.getBuildingPosition(i);
				//System.out.println(position.toString());
				int iBuilding = newBuilding(position.x, position.y);
				buildings.addBuilding(i, iBuilding);
			}
		
		}
		
		newMissileLauncher();
		//create stars, and other astral objects
		score.reset();
		powerGauge.stopToAcum();
		ufoOnScreen = 0;
		ufoDestroyed = 0;
		totalUfoInGame = 0;
		meteorsOnScreen = 0;
		shootAMissile = false;
		
		resumeEngine();
		gameMode.init(this);
		gameMode.resetTimes();
		
		//processEntities(0f);
	}
	
	private void clean(IntArray starsList){
		BadNight.badNight.stopSound("UFO");
		messageSystem.cleanMessages();
		for(int i = 0; i < ENTITIES; i += 1){
			if (starsList != null && starsList.contains(i)) continue;
			destroyEntity(i);//layers are cleaned implicitaly
		}
		buildings.reset();
		collisionList.clear();
	}
	
	public void setMessageOnScreen(CharSequence message, float duration){
		messageStringBuilder.setLength(0);
		messageStringBuilder.append(message);
		BadNight.badNight.font.getBounds(message, messageTextBounds);
		messageDuration = duration;
		messageTime = 0f;
	}
	
	public boolean isShowingMessage(){
		return messageTime < messageDuration;
	}
		
	public boolean isState(State state){
		return state == this.state;
	}
	
	public void userPause(){
		//pause all the ufo sounds
		stopUFOSounds();
		state = State.PAUSED;
	}
	
	public void setBadGameOver(CharSequence message, float time){
		state = State.BAD_GAME_OVER;
		stateTime = time;
		setMessageOnScreen(message, time);
		setUserInput(false);
		powerGauge.stopToAcum();
	}
	
	public void setGoodGameOver(CharSequence message, float time){
		state = State.GOOD_GAME_OVER;
		stateTime = time;
		setMessageOnScreen(message, time);
		setUserInput(false);
		powerGauge.stopToAcum();
	}
	
	public void stopUFOSounds(){
		//Really don't stop the UFO sounds, just pause them
		for(int i = 0; i < ENTITIES; i += 1){;
			BadNight.badNight.pauseSound("UFO", ufo[i].getSoundID());
		}
	}
	
	public void continueUFOSounds(){
		for(int i = 0; i < ENTITIES; i += 1){
			BadNight.badNight.resumeSound("UFO", ufo[i].getSoundID());
		}
	}
	
	public void showAnimatedPauseMenu(){
		BadNight.badNight.setContainer(gameOptions, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		stopUFOSounds();
	}
	
	public void pauseAndShowDialog(String text){
		MessageWindow messageWindow = GUI.get().getMessageWindow();
		messageWindow.setMessage(text);
		BadNight.badNight.setContainer(messageWindow, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		setUserInput(false);
		powerGauge.stopToAcum();
	}
	
	public void showPauseMenu(){
		BadNight.badNight.getContainers().set(gameOptions, BadNight.CURRENT_TO);
		stopUFOSounds();
	}
				
	public void setUserInput(boolean set){
		userInput = set;
	}
	
	public boolean getUserInput(){
		return userInput;
	}
	
	public int getMeteorsOnScreen(){
		return meteorsOnScreen;
	}
	
	public MessageSystem getMessageSystem(){
		return messageSystem;
	}
	
	public int getUfoOnScreen(){
		return ufoOnScreen;
	}
		
	public Score getScore(){
		return score;
	}
	
	public Launcher getLauncer(){
		return launcher;
	}
	
	public static void setSpriteWithAtlas(Sprite sprite, TextureAtlas atlas, String key){
		AtlasRegion region = atlas.findRegion(key);
		sprite.setRegion(region);
	} 
	
	private void manageHit(Physics physics1, Collision collision2, long points){
		long bullseyeMultiplier = 1L;
		if (physics1.velocity.y < 0f){
			bullseyeMultiplier = 2L;
			Message aMessage = messageSystem.addMessage(Type.CREATE_PARTICLE, "");
			aMessage.key = "shootingStar";
			aMessage.positionX = collision2.circle.x;
			aMessage.positionY = collision2.circle.y;
			aMessage.layer = particles2Layer;
			BadNight.badNight.unlockAchievement(BadNight.achievement.get("achievement_good_aim"));
		}
		score.addPoints(points * bullseyeMultiplier, 1L, buildings.getNumberOfBuildings());
	}
	
	public Buildings getBuildings(){
		return buildings;
	}
	
	public Physics getEntityPhysics(int i){
		return physics[i];
	}
	
	public void setDisplayStars(){
		state = State.DISPLAY_STARS;
		setUserInput(false);
		clean(engineStars);
		createNewStars(MathUtils.random(12, 14), 0f, BadNight.VHEIGHT, BadNight.VWIDTH, BadNight.VHEIGHT, menuStars);
	}

	private void deleteEntityList(IntArray list){
		while(list.size > 0){
			int i = list.pop();
			destroyEntity(i);
		}
	}
	
	private void createNewStars(int nStars, float x, float y, float width, float height, IntArray list){
		deleteEntityList(list);
		for(int i = 0; i < nStars; i += 1){
			float finalX = MathUtils.random(x, x+width);
			float finalY = MathUtils.random(y, y+height);
			int entity = newContinuousParticleEffect("star", finalX, finalY, sky1Layer);
			list.add(entity);
		}

	}
	
	public void restoreAllBuildings(){
		for(int i = 0; i < Buildings.BUILDINGS; i += 1){
			if (buildings.getBuildingEntityIndex(i) == -1){
				Vector2 position = buildings.getBuildingPosition(i);
				int iBuilding = newBuilding(position.x, position.y);
				buildings.addBuilding(i, iBuilding);
			}
		}
	}
	
	public void resetShootAMissile(){
		shootAMissile = false;
	}
	
	public boolean getShootAMissile(){
		return shootAMissile;
	}
	
}