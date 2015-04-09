package the_catwolf.BadNight;

public interface GoogleServices {
	public void signIn();
	public void signOut();
	public void rateGame();
	public void submitScore(long score);
	public void showScores();
	public void showAchievements();
	public void unlockAchievement(int id);
	public void showAd();
	public boolean isSignedIn();
}
