package the_catwolf.BadNight;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class ShowLeaderboards extends Window {
	
	static class ViewResistance100Leaderboard extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.currentGameMode = BadNight.badNight.resistance100;
			BadNight.badNight.googleServices.showScores();
		}
		
	}
	
	static class ViewTimeAttack2Leaderboard extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.currentGameMode = BadNight.badNight.timeAttack2;
			BadNight.badNight.googleServices.showScores();
		}
		
	}
	
	static class ViewTimeAttack5Leaderboard extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.currentGameMode = BadNight.badNight.timeAttack5;
			BadNight.badNight.googleServices.showScores();
		}
		
	}
	
	public ShowLeaderboards(BadNight game){
		super("Leaderboards", GUI.get().getWindowStyle());
		GUI gui = GUI.get();
		
		this.pad(GUI.ELEMENT_SPACE2);
		this.padTop(GUI.ELEMENT_SPACE3);
		this.setWidth(BadNight.VWIDTH/2f);
		this.setBackground(gui.giveMeWindowsDrawable());
		this.setMovable(false);
		this.setResizable(false);
		
		Label leaderboards = gui.GiveMeLabel("Leaderboards");
		this.addActor(leaderboards);
		
		TextButton viewResistanceLeaderboard = gui.giveMeTextButton("Resistance 100");
		viewResistanceLeaderboard.addListener(new ViewResistance100Leaderboard() );
			
		TextButton viewTimeAttack2Leaderboard = gui.giveMeTextButton("Time 2 min.");
		viewTimeAttack2Leaderboard.addListener( new ViewTimeAttack2Leaderboard() );
		
		TextButton viewTimeAttack5Leaderboard = gui.giveMeTextButton("Time 5 min");
		viewTimeAttack5Leaderboard.addListener( new ViewTimeAttack5Leaderboard() );
		
		TextButton back = gui.giveMeABackButton("Back", BadNight.badNight.mainMenu);
		
		this.add(viewResistanceLeaderboard).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(viewTimeAttack2Leaderboard).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(viewTimeAttack5Leaderboard).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(back).fill().space(GUI.ELEMENT_SPACE2);
	}
}
