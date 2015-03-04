package the_catwolf.BadNight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ShowScores extends VerticalGroup {

	static class ViewResistance100Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.resistance100.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen);
		}
		                
	}
		                
	static class ViewTimeAttack2Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.timeAttack2.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen);
		}
	}

	static class ViewTimeAttack5Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.timeAttack5.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen);
		}
	}
	
	public ShowScores(BadNight game){
		space(GUI.ELEMENT_SPACE);
		GUI gui = GUI.get();
		
		Label scores = gui.GiveMeLabel("Scores");
		this.addActor(scores);
		
		TextButton viewResistanceScores = gui.giveMeTextButton("Resistance 100");
		viewResistanceScores.addListener(new ViewResistance100Score() );
		
		TextButton viewTimeAttack2Score = gui.giveMeTextButton("Time 2 min");
		viewTimeAttack2Score.addListener( new ViewTimeAttack2Score() );
		
		TextButton viewTimeAttack5Score = gui.giveMeTextButton("Time 5 min");
		viewTimeAttack5Score.addListener( new ViewTimeAttack5Score() );
		
		TextButton back = gui.giveMeABackButton("back", BadNight.badNight.mainMenu);
		
		this.addActor(viewResistanceScores);
		this.addActor(viewTimeAttack2Score);
		this.addActor(viewTimeAttack5Score);
		this.addActor(back);
		this.fill();
		
	}
	
}
