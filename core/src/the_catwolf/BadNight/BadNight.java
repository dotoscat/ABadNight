package the_catwolf.BadNight;

import java.util.HashMap;

import the_catwolf.BadNight.GameMode.GameMode;
import the_catwolf.BadNight.GameMode.HowToPlay;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BadNight extends Game{
	
	static public HashMap<String, Integer> achievement;
	static public HashMap<String, Integer> leaderboard;
	
	static public BadNight badNight;
	final private static int VERSION = 1;
	final private static int FEATURES = 6;
	final private static int BUGS_SOLVED = 0;
		
	AssetManager assets;
	
	InputMultiplexer inputMulti;
	
	private Camera camera;
	public SpriteBatch batch;
	
	public final static float VWIDTH = 320f;
	public final static float VHEIGHT = 480f;
	
	public Vector3 point;
			
	public BitmapFont font;
	
	public Engine engine;
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
	
	private Stage ui;
	private Containers containers;
	
	public GoogleServices googleServices;
	
	public GameMode currentGameMode;
	//public GameMode test;
	public GameMode timeAttack2;
	public GameMode timeAttack5;
	public GameMode resistance100;
	public GameMode howToPlay;
	
	static private Vector2 velocity;
	
	final static public float MAIN_MENU_POSITION = VHEIGHT/2f + VHEIGHT;
	final static public float ENGINE_POSITION = VHEIGHT/2f;
		
	public final static Vector2 FROM1 = new Vector2(BadNight.VWIDTH/2-BadNight.VWIDTH, MAIN_MENU_POSITION);
	public final static Vector2 TO1 = new Vector2(BadNight.VWIDTH/2, MAIN_MENU_POSITION);
	public final static Vector2 LEAVE1 = new Vector2(BadNight.VWIDTH/2+BadNight.VWIDTH, MAIN_MENU_POSITION);
	
	public final static Vector2 FROM2 = new Vector2(BadNight.VWIDTH/2-BadNight.VWIDTH, ENGINE_POSITION);
	public final static Vector2 TO2 = new Vector2(BadNight.VWIDTH/2, ENGINE_POSITION);
	public final static Vector2 LEAVE2 = new Vector2(BadNight.VWIDTH/2+BadNight.VWIDTH, ENGINE_POSITION);
	
	public static Vector2 CURRENT_FROM = FROM1;
	public static Vector2 CURRENT_TO = TO1;
	public static Vector2 CURRENT_LEAVE = LEAVE1;
	
	public ShapeRenderer shapeRenderer;
	
	static public class Containers{
		private Container<WidgetGroup>[] _container;
		private Vector2[] _toPosition;
		private float time;
		private float maxTime;
		
		private static Vector2 tmp = new Vector2();
		
		public Containers(Stage stage){
			_container = new Container[2];
			_container[0] = new Container<WidgetGroup>();
			_container[1] = new Container<WidgetGroup>();
			
			stage.addActor(_container[0]);
			stage.addActor(_container[1]);
			
			_toPosition = new Vector2[2];
			_toPosition[0] = new Vector2();
			_toPosition[1] = new Vector2();
			
		}
		
		public void step(float dt){
			if (time < maxTime){
				time += dt;
				if (time > maxTime){
					time = maxTime;
					_container[0].setTouchable(Touchable.enabled);
					_container[1].setActor(null);
				}
				float alpha = time / maxTime;
				tmp.x = _container[0].getX();
				tmp.y = _container[0].getY();
				tmp.interpolate(_toPosition[0], alpha, Interpolation.exp5Out);
				_container[0].setPosition(tmp.x, tmp.y);
				tmp.x = _container[1].getX();
				tmp.y = _container[1].getY();
				tmp.interpolate(_toPosition[1], alpha, Interpolation.exp5Out);
				_container[1].setPosition(tmp.x, tmp.y);
			}
		}
		
		public void set(WidgetGroup group, Vector2 position){
			time = 0f;
			maxTime = 0f;
			_container[0].setActor(group);
			_container[0].center();
			_container[0].setPosition(position.x, position.y);
			_container[0].setVisible(true);
			_container[1].setVisible(false);
		}
		
		public WidgetGroup set(Vector2 start, Vector2 to, Vector2 moveTo, float maxTime, WidgetGroup group){
			_container[0].setPosition(start.x, start.y);
			_toPosition[0].set(to);
			_container[1].setPosition(to.x, to.y);
			_toPosition[1].set(moveTo);
			time = 0f;
			this.maxTime = maxTime;
			_container[0].setVisible(true);
			_container[0].setTouchable(Touchable.disabled);
			_container[1].setVisible(true);
			_container[1].setTouchable(Touchable.disabled);
			WidgetGroup lastGroup = _container[0].getActor();
			_container[1].setActor(lastGroup);
			_container[0].setActor(group);
			_container[0].center();
			return lastGroup;
		}
		
		public void hide(Vector2 moveTo, float maxTime){
			
			time = 0f;
			this.maxTime = maxTime;
			_toPosition[1].set(moveTo);
			_container[1].setPosition(_container[0].getX(), _container[0].getY() );
			_container[1].setActor(_container[0].getActor());
			_container[0].setVisible(false);
			_container[0].setActor(null);
			_container[1].setVisible(true);
			_container[1].setTouchable(Touchable.disabled);
			
			//quit();
		}
		
		public void quit(){
			_container[0].setVisible(false);
			_container[0].setActor(null);
			_container[1].setVisible(false);
		}
		
		public boolean isMoving(){
			return time < maxTime;
		}
		
		public WidgetGroup getMainContainerWidgetGroup(){
			return _container[0].getActor();
		}
		
	}
	
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
				//System.out.println(time + " / " + maxTime + "; " + time/maxTime);
				position.interpolate(newPosition, time/maxTime, Interpolation.pow2Out);
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
		achievement.put("achievement_come_on", 10);
		achievement.put("achievement_you_can", 11);
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
		howToPlay = new HowToPlay("How To Play", -1, -1, "howToPlay", 0);
		
		assets = new AssetManager();
		assets.load("pack.atlas", TextureAtlas.class);
		assets.load("Light_ta-Diode111-8758_hifi.wav", Sound.class);
		assets.load("Explosio-Diode111-8778_hifi.wav", Sound.class);
		assets.load("UFO_Hu-Mr_PantZ-7379_hifi.wav", Sound.class);
		assets.load("powerUp.wav", Sound.class);
		assets.load("paralizerShoot.wav", Sound.class);
		assets.load("paralizerHit.wav", Sound.class);
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
		sounds.put("paralizerShoot", (Sound)assets.get("paralizerShoot.wav" ) );
		sounds.put("paralizerHit", (Sound)assets.get("paralizerHit.wav" ) );
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
		ui = new Stage( viewport, batch );
		GUI.init();
		PowerUp.init();
		containers = new Containers(ui);
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
		
		shapeRenderer = new ShapeRenderer(16);
	}
		
	@Override
	public void render () {
		options.checkWhetherSignedGooglePlayServices(googleServices);
		Gdx.gl.glClearColor(0f, 0f, 0.25f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float rawDeltaTime = Gdx.graphics.getRawDeltaTime();
		containers.step(rawDeltaTime);
		camera.update(rawDeltaTime);
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
		shapeRenderer.dispose();
	}
	
	public void setContainerWithLastMenu(MenuWithParent menuWithParent, Vector2 from, Vector2 to, Vector2 leave){
		menuWithParent.setLastMenu(containers.set(from, to, leave, 0.5f, menuWithParent));
		this.playSound("explosion", 1f);
	}
	
	public void setContainer(WidgetGroup group, Vector2 from, Vector2 to, Vector2 leave){
		WidgetGroup lastGroup = containers.getMainContainerWidgetGroup();
		containers.set(from, to, leave, 0.5f, group);
		if (lastGroup instanceof MenuWithParent){
			this.playSound("launcher", 1f);
		}else{
			this.playSound("explosion", 1f);
		}
	}
		
	public void setTitleMenu(){
		containers.set(title, BadNight.TO1);
		camera.setPosition(BadNight.MAIN_MENU_POSITION);
	}
	
	public void setMainMenu(){
		BadNight.CURRENT_FROM = BadNight.FROM1;
		BadNight.CURRENT_TO = BadNight.TO1;
		BadNight.CURRENT_LEAVE = BadNight.LEAVE1;
		inputMulti.removeProcessor(engine);
		containers.set(mainMenu, BadNight.CURRENT_TO);
		engine.setDisplayStars();
		camera.setPositionTo(BadNight.MAIN_MENU_POSITION, 1.5f);
		stopCurrentMusic();
	}
		
	public void setEngine(){
		BadNight.CURRENT_FROM = BadNight.FROM2;
		BadNight.CURRENT_TO = BadNight.TO2;
		BadNight.CURRENT_LEAVE = BadNight.LEAVE2;
		inputMulti.addProcessor(engine);
		camera.setPositionTo(ENGINE_POSITION, 1.5f);
		//containers.quit();
		playMusic("A Bad Night");
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
	
	public Stage getStage(){
		return ui;
	}
	
	public Containers getContainers(){
		return containers;
	}
		
	public void unlockAchievement(int id){
		if (currentGameMode.unlockAchievements()){
			googleServices.unlockAchievement(id);
		}
	}
		
	static public String getVersionAndRevision(){
		return "version " + VERSION + "." + FEATURES + "." + BUGS_SOLVED;
	}
	
	public boolean signInAtTheStart(){
		//return gameData.signAtTheStart;
		return false;
	}
	
	public void setSignInAtTheStart(boolean set){
		//gameData.signAtTheStart = set;
		gameData.save();
		options.signInAtTheStart.setChecked(set);
	}
	
}