package the_catwolf.BadNight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends Window{
	
	static class ShowScores extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.showScores, BadNight.FROM1, BadNight.TO1, BadNight.LEAVE1);
		}
		
	}
	
	static class ShowLeaderboards extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.showLeaderboards, BadNight.FROM1, BadNight.TO1, BadNight.LEAVE1);
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
			BadNight.badNight.setContainer(BadNight.badNight.selectGameMode, BadNight.FROM1, BadNight.TO1, BadNight.LEAVE1);
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
			BadNight.badNight.setContainer(BadNight.badNight.credits, BadNight.FROM1, BadNight.TO1, BadNight.LEAVE1);
		}
		
	} 
	
	public MainMenu(BadNight game){
		super("Main Menu", GUI.get().getWindowStyle());	
		GUI gui = GUI.get();
		
		this.pad(GUI.ELEMENT_SPACE2);
		this.padTop(GUI.ELEMENT_SPACE3);
		this.setWidth(BadNight.VWIDTH/2f);
		this.setBackground(gui.giveMeWindowsDrawable());
		this.setMovable(false);
		this.setResizable(false);
		
		TextButton play = gui.giveMeTextButton("Play");
		play.addListener( new Play() );
		TextButton options = Options.giveMeAOptionsButton(this);
		TextButton achievements = gui.giveMeTextButton("Achievements");
		achievements.addListener( new ShowAchievements() );
		TextButton leaderboards = gui.giveMeTextButton("Leaderboards");
		leaderboards.addListener( new ShowLeaderboards() );
		TextButton localScores = gui.giveMeTextButton("Local scores");
		localScores.addListener( new ShowScores() );
		
		TextButton credits = gui.giveMeTextButton("Credits");
		credits.addListener( new Credits() );
		
		TextButton exit = gui.giveMeTextButton("Exit");
		exit.addListener(new Exit() );
		GUI.setButtonFakeSize(exit, 128f, GUI.BUTTON_HEIGHT);
		
		this.add(play).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(options).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(achievements).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(leaderboards).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(localScores).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(credits).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(exit).fill().space(GUI.ELEMENT_SPACE2);
		
		//center();
		//layout();
	}	
}
