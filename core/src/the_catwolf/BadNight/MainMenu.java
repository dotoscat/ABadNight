package the_catwolf.BadNight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends VerticalGroup{
	
	static class ShowScores extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.showScores);
		}
		
	}
	
	static class ShowLeaderboards extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.showLeaderboards);
		}
		
	}
	
	static class ShowAchievements extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.googleServices.showAchievements();
		}
		
	}
	
	static class Play extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.selectGameMode);
		}
		
	}
	
	static class Exit extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			Gdx.app.exit();
		}
		
	}
	
	private static class Credits extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.credits);
		}
		
	} 
	
	public MainMenu(BadNight game){
						
		space(GUI.ELEMENT_SPACE);
		
		GUI gui = GUI.get();
		
		TextButton play = gui.giveMeTextButton("Play");
		play.addListener( new Play() );
		TextButton options = Options.giveMeAOptionsButton(this);
		TextButton achievements = gui.giveMeTextButton("Achievements");
		achievements.addListener( new ShowAchievements() );
		TextButton leaderboards = gui.giveMeTextButton("Leaderboards");
		leaderboards.addListener( new ShowLeaderboards() );
		TextButton localScores = gui.giveMeTextButton("Scores");
		localScores.addListener( new ShowScores() );
		
		TextButton credits = gui.giveMeTextButton("Credits");
		credits.addListener( new Credits() );
		
		TextButton exit = gui.giveMeTextButton("Exit");
		exit.addListener(new Exit() );
		GUI.setButtonFakeSize(exit, 128f, GUI.BUTTON_HEIGHT);
		
		addActor(play);
		addActor(options);
		addActor(achievements);
		addActor(leaderboards);
		addActor(localScores);
		addActor(credits);
		addActor(exit);
		
		center();
		layout();
		fill();
	}	
}
