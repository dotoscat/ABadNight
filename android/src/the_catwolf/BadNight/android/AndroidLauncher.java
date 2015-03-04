package the_catwolf.BadNight.android;

import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.GoogleServices;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

//GameHelper 

public class AndroidLauncher extends AndroidApplication implements GoogleServices {
	
	private final static int REQUEST_CODE_UNUSED = 9002;
	private GameHelper gameHelper;
	private AdView adView;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 2;
		BadNight.init();
		
		BadNight.achievement.put("achievement_good_aim", R.string.achievement_good_aim); 
		BadNight.achievement.put("achievement_survivor_of_100", R.string.achievement_survivor_of_100);
		//BadNight.achievement.put("achievement_have_a_good_night", R.string.achievement_have_a_good_night);
		BadNight.achievement.put("achievement_lazy", R.string.achievement_lazy);
		BadNight.achievement.put("achievement_good_defender", R.string.achievement_good_defender);
		BadNight.achievement.put("achievement_here_nothing_happened", R.string.achievement_here_nothing_happened);
		BadNight.achievement.put("achievement_time_of_2_minutes", R.string.achievement_time_of_2_minutes);
		BadNight.achievement.put("achievement_time_of_5_minutes", R.string.achievement_time_of_5_minutes);
		BadNight.achievement.put("achievement_ufo_destroyer", R.string.achievement_ufo_destroyer);
		
		BadNight.leaderboard.put("leaderboard_resistance_100_levels", R.string.leaderboard_resistance_100_levels);
		BadNight.leaderboard.put("leaderboard_time_2_minutes", R.string.leaderboard_time_2_minutes);
		BadNight.leaderboard.put("leaderboard_time_5_minutes", R.string.leaderboard_time_5_minutes);
		
		BadNight badNight = null;
		badNight = new BadNight(this);
		
		//initialize(badNight, config);
		createWithAds(badNight, config);
		BadNight.badNight = badNight;
		
		// Create the GameHelper.
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(true);

		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInSucceeded()
			{
			}

			@Override
			public void onSignInFailed()
			{
			}
		};

		gameHelper.setup(gameHelperListener);

		// The rest of your onCreate() code here...
		
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

	private void createWithAds(ApplicationListener listener, AndroidApplicationConfiguration config){		
		RelativeLayout layout = new RelativeLayout(this);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = this.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		View gameView = this.initializeForView(listener, config);
		
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-6259651941560265/3614807437");
				
		layout.addView(gameView);
		
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_TOP);
		layout.addView(adView, adParams);
		adView.setVisibility(View.VISIBLE);
		
		AdRequest.Builder builder = new AdRequest.Builder();
		builder.tagForChildDirectedTreatment(true);
		builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
		///builder.addTestDevice("XXXXXXXXXXXXXXXXX");
		builder.addTestDevice("F52C626B06EBC43FCC089A0B771E7A8");
		AdRequest request = builder.build();
		adView.loadAd(request);
		adView.requestLayout();
		//adView.setBackgroundColor(Color.BLUE);
		
		setContentView(layout);
	}
	
	@Override
	public void signIn() {
		// TODO Auto-generated method stub
		try
		{
			runOnUiThread(new Runnable(){
				
				@Override
				public void run(){
					gameHelper.beginUserInitiatedSignIn();
				}
			} );
		}
		catch (Exception e){
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		// TODO Auto-generated method stub
		try
		{
			runOnUiThread(new Runnable(){
				
				@Override
				public void run(){
					gameHelper.signOut();
				}
			} );
		}
		catch (Exception e){
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		// TODO Auto-generated method stub
		// Replace the end of the URL with the package of your game
		//String str ="https://play.google.com/store/apps/details?id=org.fortheloss.plunderperil";
		//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(long score) {
		// TODO Auto-generated method stub
		if (isSignedIn() == true)
		{
			String strId = getString(BadNight.badNight.getCurrentGameMode().getLeaderboardId());
			log("BadNight", "send to: " + strId);
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), strId, score);
		}else{
		// Maybe sign in here then redirect to submitting score?

		}
	}

	@Override
	public void showScores() {
		// TODO Auto-generated method stub
		if (isSignedIn() == true){
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient() , getString(BadNight.badNight.getCurrentGameMode().getLeaderboardId()) ), REQUEST_CODE_UNUSED );
		}else{
			//maybe show an message for sign in
		}
		
	}

	@Override
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return gameHelper.isSignedIn();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showAchievements() {
		// TODO Auto-generated method stub
		if ( isSignedIn() ){
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_CODE_UNUSED);
		}else{
			gameHelper.makeSimpleDialog("Please go to Options and sign in");
		}
	}

	@Override
	public void unlockAchievement(int id) {
		// TODO Auto-generated method stub
		if ( isSignedIn() ){
			//Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_have_a_good_night), 1);
			Games.Achievements.unlock(gameHelper.getApiClient(), getString(id));
		}else{
			//gameHelper.makeSimpleDialog("Please go to Options and sign in");
		}
	}
	
}
