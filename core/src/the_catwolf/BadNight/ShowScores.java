package the_catwolf.BadNight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ShowScores extends Window {

	static class ViewResistance100Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.resistance100.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		}
		                
	}
		                
	static class ViewTimeAttack2Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.timeAttack2.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		}
	}

	static class ViewTimeAttack5Score extends ChangeListener{
		 
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.scoreScreen.fill(BadNight.badNight.timeAttack5.getScore());
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.scoreScreen, BadNight.CURRENT_FROM, BadNight.CURRENT_TO, BadNight.CURRENT_LEAVE);
		}
	}
	
	public ShowScores(BadNight game){
		super("Scores", GUI.get().getWindowStyle());
		GUI gui = GUI.get();
		
		this.pad(GUI.ELEMENT_SPACE2);
		this.padTop(GUI.ELEMENT_SPACE3);
		this.setWidth(BadNight.VWIDTH/2f);
		this.setBackground(gui.giveMeWindowsDrawable());
		this.setMovable(false);
		this.setResizable(false);
		
		Label scores = gui.GiveMeLabel("Scores");
		this.addActor(scores);
		
		TextButton viewResistanceScores = gui.giveMeTextButton("Resistance 100");
		viewResistanceScores.addListener(new ViewResistance100Score() );
		
		TextButton viewTimeAttack2Score = gui.giveMeTextButton("Time 2 min");
		viewTimeAttack2Score.addListener( new ViewTimeAttack2Score() );
		
		TextButton viewTimeAttack5Score = gui.giveMeTextButton("Time 5 min");
		viewTimeAttack5Score.addListener( new ViewTimeAttack5Score() );
		
		TextButton back = gui.giveMeABackButton("back", BadNight.badNight.mainMenu);
		
		this.add(viewResistanceScores).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(viewTimeAttack2Score).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(viewTimeAttack5Score).fill().space(GUI.ELEMENT_SPACE2).row();
		this.add(back).fill().space(GUI.ELEMENT_SPACE2);
		
	}
	
}
