package the_catwolf.BadNight;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class ShowLeaderboards extends VerticalGroup {
	
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
		space(GUI.ELEMENT_SPACE);
		GUI gui = GUI.get();
		
		Label leaderboards = gui.GiveMeLabel("Leaderboards");
		this.addActor(leaderboards);
		
		TextButton viewResistanceLeaderboard = gui.giveMeTextButton("Resistance 100");
		viewResistanceLeaderboard.addListener(new ViewResistance100Leaderboard() );
			
		TextButton viewTimeAttack2Leaderboard = gui.giveMeTextButton("Time 2 min.");
		viewTimeAttack2Leaderboard.addListener( new ViewTimeAttack2Leaderboard() );
		
		TextButton viewTimeAttack5Leaderboard = gui.giveMeTextButton("Time 5 min");
		viewTimeAttack5Leaderboard.addListener( new ViewTimeAttack5Leaderboard() );
		
		TextButton back = gui.giveMeABackButton("Back", BadNight.badNight.mainMenu);
		
		this.addActor(viewResistanceLeaderboard);
		this.addActor(viewTimeAttack2Leaderboard);
		this.addActor(viewTimeAttack5Leaderboard);
		
		this.addActor(back);
		this.fill();
		
	}
}
