package the_catwolf.BadNight.desktop;
import the_catwolf.BadNight.BadNight;
import the_catwolf.BadNight.GoogleServices;

public class DesktopGoogleServices implements GoogleServices {
	
	boolean isSigned = false;
	
	@Override
	public void signIn()
	{
	System.out.println("DesktopGoogleServies: signIn()");
	isSigned = true;
	}

	@Override
	public void signOut()
	{
	System.out.println("DesktopGoogleServies: signOut()");
	isSigned = false;
	}

	@Override
	public void rateGame()
	{
	System.out.println("DesktopGoogleServices: rateGame()");
	}

	@Override
	public void submitScore(long score)
	{
	System.out.println("DesktopGoogleServices: submitScore(" + score + ")");
	}

	@Override
	public void showScores()
	{
	System.out.println("DesktopGoogleServices: showScores()");
	System.out.println( BadNight.badNight.getCurrentGameMode().getLeaderboardId() );
	}

	@Override
	public boolean isSignedIn()
	{
	//System.out.println("DesktopGoogleServies: isSignedIn()");
	return isSigned;
	}

	@Override
	public void showAchievements() {
		// TODO Auto-generated method stub
		System.out.println("DesktopGoogleServices: showAchievements()");
	}

	@Override
	public void unlockAchievement(int id) {
		// TODO Auto-generated method stub
		System.out.println("DesktopGoogleServices: showAchievements(" + id + ")");
	}
}
