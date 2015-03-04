package the_catwolf.BadNight;

import java.util.HashMap;

import the_catwolf.BadNight.GameMode.GameMode;
import the_catwolf.BadNight.GameMode.Resistance;
import the_catwolf.BadNight.GameMode.TimeAttack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BadNight extends Game{
	
	static public HashMap<String, Integer> achievement;
	static public HashMap<String, Integer> leaderboard;
	
	static public BadNight badNight;
		
	AssetManager assets;
	
	InputMultiplexer inputMulti;
	
	private Camera camera;
	public SpriteBatch batch;
	
	public final static float VWIDTH = 320f;
	public final static float VHEIGHT = 480f;
	
	public Vector3 point;
			
	public BitmapFont font;
	
	Engine engine;
	public Title title;
	public MainMenu mainMenu;
	public Options options;
	ScoreScreen scoreScreen;
	public Credits credits;
	
	public SelectGameMode selectGameMode;
	public ShowLeaderboards showLeaderboards;
	public ShowScores showScores;
	
	public Viewport viewport;
	
	GameData gameData;
	
	HashMap<String, Sound> sounds;
	HashMap<String, Music> music;
	Music currentMusic;
	
	Stage ui;
	Container<Actor> container;
	
	public GoogleServices googleServices;
	
	public GameMode currentGameMode;
	//public GameMode test;
	public GameMode timeAttack2;
	public GameMode timeAttack5;
	public GameMode resistance100;
	
	static private Vector2 velocity;
	
	final static public float MAIN_MENU_POSITION = VHEIGHT/2f + VHEIGHT;
	final static public float ENGINE_POSITION = VHEIGHT/2f;
	
	final static public float MENU_POSITION = MAIN_MENU_POSITION - VHEIGHT/6f;
	
	static public class Camera extends OrthographicCamera{
		
		private static enum Movement{
			PATH,
			TO_TARGET
		};
		
		Movement movement;
		
		private Bezier<Vector3> path;
		private Vector3 newPosition;
		private float time;
		private float maxTime;
		private float delta;
		
		public Camera(float viewportWidth, float viewportHeight){
			super(VWIDTH, VHEIGHT);
			translate(VWIDTH/2f, VHEIGHT/2f);
			update();
			
			newPosition = new Vector3(this.position);
			path = new Bezier<Vector3>(
					new Vector3(BadNight.VWIDTH/2f, BadNight.ENGINE_POSITION, 0f),
					new Vector3(BadNight.VWIDTH/2f, BadNight.VHEIGHT*4f, 0f),
					new Vector3(BadNight.VWIDTH/2f, BadNight.ENGINE_POSITION, 0f)
					);
		}
		
		public void update(float dt){
			if (time < maxTime){
				time += dt;
			}
			if (time > maxTime){
				time = maxTime;
			}
			if (movement == Movement.TO_TARGET){				
				position.lerp(newPosition, delta);
				if (time == maxTime){
					position.set(newPosition);
				}
				update();
			}
			else if (movement == Movement.PATH){
				if (time == maxTime){
					path.valueAt(this.position, time/maxTime);
					update();
				}
				if (time != maxTime){
					path.valueAt(this.position, time/maxTime);
					update();
				}
			}			
		}
		
		public void doBackCome(float maxTime){
			movement = Movement.PATH;
			time = 0f;
			this.maxTime = maxTime;
		}
		
		public void setPositionTo(float y, float maxTime){
			movement = Movement.TO_TARGET;
			newPosition.y = y;
			delta = maxTime/20f;
			time = 0f;
			this.maxTime = maxTime;
			update();
		}
		
		public void setPosition(float y){
			newPosition.y = y;
			this.position.y = y;
			time = 0f;
			maxTime = 0f;
			update();
		}
		
		public boolean isMoving(){
			boolean itIs = false;
			if (movement == Movement.TO_TARGET){
				itIs = this.position.y != newPosition.y;
			}
			if (movement == Movement.PATH){
				itIs = time != maxTime;
			}
			return itIs;
			//return this.position.y != newPosition.y;
		}
		
	}
	
	public static void init(){
		//this static method must called before fill the achievements
		//in the Android launcher 'onCreate'
		achievement = new HashMap<String, Integer>();
		achievement.put("achievement_good_aim", 1); 
		achievement.put("achievement_survivor_of_100", 2);
		achievement.put("achievement_have_a_good_night", 3);
		achievement.put("achievement_good_defender", 4);
		achievement.put("achievement_here_nothing_happened", 5);
		achievement.put("achievement_time_of_2_minutes", 6);
		achievement.put("achievement_time_of_5_minutes", 7);
		achievement.put("achievement_ufo_destroyer", 8);
		achievement.put("achievement_lazy", 9);
		
		leaderboard = new HashMap<String, Integer>();
		leaderboard.put("leaderboard_resistance_100_levels", 1);
		leaderboard.put("leaderboard_time_2_minutes", 2);
		leaderboard.put("leaderboard_time_5_minutes", 3);
		
		velocity = new Vector2();
		Engine.staticInit();
	}
	
	public BadNight(GoogleServices googleServices){
		this.googleServices = googleServices;
	}
	
	@Override
	public void create () {
		gameData = new GameData();
		
		timeAttack2 = new TimeAttack(2, "Time 2:00", BadNight.leaderboard.get("leaderboard_time_2_minutes"), BadNight.achievement.get("achievement_time_of_2_minutes"), "leaderboard_time2", 5);
		timeAttack5 = new TimeAttack(5, "Time 5:00", BadNight.leaderboard.get("leaderboard_time_5_minutes"), BadNight.achievement.get("achievement_time_of_5_minutes"), "leaderboard_time5", 5);
		resistance100 = new Resistance(100, "Resistance of 100", BadNight.leaderboard.get("leaderboard_resistance_100_levels"), BadNight.achievement.get("achievement_survivor_of_100"), "leaderboard_resistance100", 5);
		
		assets = new AssetManager();
		assets.load("pack.atlas", TextureAtlas.class);
		assets.load("Light_ta-Diode111-8758_hifi.wav", Sound.class);
		assets.load("Explosio-Diode111-8778_hifi.wav", Sound.class);
		assets.load("UFO_Hu-Mr_PantZ-7379_hifi.wav", Sound.class);
		assets.load("powerUp.wav", Sound.class);
		assets.load("A-Bad-Night.ogg", Music.class);
		
		ParticleEffectParameter effectParameter = new ParticleEffectParameter();
		effectParameter.atlasFile = "pack.atlas";
		
		assets.load("explosion.particle", ParticleEffect.class, effectParameter);
		assets.load("shootingStar.particle", ParticleEffect.class, effectParameter);
		assets.load("smoke.particle", ParticleEffect.class, effectParameter);
		assets.load("smoke2.particle", ParticleEffect.class, effectParameter);
		assets.load("star.particle", ParticleEffect.class, effectParameter);
		assets.load("paralizer.particle", ParticleEffect.class, effectParameter);
		assets.finishLoading();
		
		sounds = new HashMap<String, Sound>();
		sounds.put("launcher", (Sound)assets.get("Light_ta-Diode111-8758_hifi.wav") );
		sounds.put("explosion", (Sound)assets.get("Explosio-Diode111-8778_hifi.wav") );
		sounds.put("UFO", (Sound)assets.get("UFO_Hu-Mr_PantZ-7379_hifi.wav") );
		sounds.put("powerUp", (Sound)assets.get("powerUp.wav") );
		//if there are more sound for load consider implement
		//a loader that uses the hashmap and a config text file
		
		music = new HashMap<String, Music>();
		music.put("A Bad Night", (Music)assets.get("A-Bad-Night.ogg"));
		
		point = new Vector3();
		batch = new SpriteBatch();
		
		font = new BitmapFont();
		
		camera = new Camera(VWIDTH, VHEIGHT);
		viewport = new FitViewport(VWIDTH, VHEIGHT, camera);
		batch.setProjectionMatrix(camera.combined);
		inputMulti = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMulti);
		
		badNight = this;

		GUI.init();
		PowerUp.init();
		
		ui = new Stage( viewport, batch );
		container = new Container<Actor>();
		//container.setBackground(GUI.get().giveMeWindowsDrawable());
		//instead a background, try add the buttons to a window or something
		
		//container.align(Align.center);
		ui.addActor(container);
		
		inputMulti.addProcessor(0, ui);
		
		title = new Title(this);
		engine = new Engine(this);
		mainMenu = new MainMenu(this);
		options = new Options(this);
		scoreScreen = new ScoreScreen(this, 5);
		credits = new Credits(this);
		camera.setPosition(BadNight.MAIN_MENU_POSITION);
		
		selectGameMode = new SelectGameMode(this);
		showLeaderboards = new ShowLeaderboards(this);
		showScores = new ShowScores(this);
		
		//playMusic("A Bad Night");
				
		setScreen(engine);
		engine.setDisplayStars();
		setTitleMenu();
	}
		
	@Override
	public void render () {
		options.checkWhetherSignedGooglePlayServices(googleServices);
		Gdx.gl.glClearColor(0f, 0f, 0.25f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update(Gdx.graphics.getRawDeltaTime());
		ui.act();
		if (this.getScreen() != null){
			super.render();
		}
		ui.draw();
	}
			
	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
		super.resize(width, height);
	}
	
	@Override
	public void dispose(){
		assets.dispose();
		batch.dispose();
		font.dispose();
		engine.dispose();
		ui.dispose();
	}
	
	public void setContainerWithLastMenu(MenuWithParent menuWithParent){
		menuWithParent.setLastMenu(container.getActor());
		setContainer(menuWithParent);
	}
	
	public void setContainer(Actor actor){
		container.setActor(actor);
		container.center();
	}
	
	public void setContainer(Actor actor, float positionY){
		container.setActor(actor);
		container.center();
		container.setPosition(BadNight.VWIDTH/2f, positionY);
	}
	
	public void setTitleMenu(){
		setContainer(title, BadNight.MAIN_MENU_POSITION);
		camera.setPosition(BadNight.MAIN_MENU_POSITION);
	}
	
	public void setMainMenu(){
		inputMulti.removeProcessor(engine);
		setContainer(mainMenu, BadNight.MAIN_MENU_POSITION);
		engine.setDisplayStars();
		camera.setPositionTo(BadNight.MAIN_MENU_POSITION, 1.5f);
		stopCurrentMusic();
	}
		
	public void setOptions(){
		
	}
	
	public void setEngine(){
		inputMulti.addProcessor(engine);
		camera.setPositionTo(ENGINE_POSITION, 1.5f);
		container.setActor(null);
		playMusic("A Bad Night");
	}
	
	public void hideCurrentMenu(){
		container.setActor(null);
	}
	
	public void vibrate(int ms){
		if (gameData.usesVibration)
			Gdx.input.vibrate(ms);
	}
	
	public void playSound(String key, float pitch){
		sounds.get(key).play(gameData.soundVolume, pitch, 0f);
	}
	
	public long loopSound(String key, float pitch){
		return sounds.get(key).loop(gameData.soundVolume, pitch, 0f);
	}
	
	public void stopSound(String key){
		sounds.get(key).stop();
	}
	
	public void pauseSound(String key, long id){
		sounds.get(key).pause(id);
	}
	
	public void resumeSound(String key, long id){
		sounds.get(key).resume(id);
	}
	
	public void playMusic(String key){
		Music aMusic = music.get(key);
		if (aMusic.isPlaying()){
			aMusic.stop();
		}
		aMusic.setLooping(true);
		aMusic.setVolume(gameData.musicVolume);
		aMusic.play();
		currentMusic = aMusic;
	}
	
	public void stopCurrentMusic(){
		if (currentMusic != null){
			currentMusic.stop();
		}
	}
	
	public void setMusicVolume(float value){
		if (currentMusic == null) return;
		currentMusic.setVolume(value);
	}
		
	public GameMode getCurrentGameMode(){
		return currentGameMode;
	}
	
	public static Vector2 getVelocityFromTo(float originX, float originY, float targetX, float targetY, float speed){
		float angle = MathUtils.atan2(targetY - originY, targetX - originX);
		velocity.x = MathUtils.cos(angle) * speed;
		velocity.y = MathUtils.sin(angle) * speed;
		return velocity;
	}
	
	public BadNight.Camera getCamera(){
		return camera;
	}
	
}