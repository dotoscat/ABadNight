package the_catwolf.BadNight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SelectGameMode extends VerticalGroup {

	static class Resistance100Mode extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.gameMode = BadNight.badNight.resistance100;
			BadNight.badNight.currentGameMode = BadNight.badNight.resistance100;
			BadNight.badNight.engine.labelShowGameMode.setText(BadNight.badNight.engine.gameMode.getName());
			BadNight.badNight.engine.init();
			BadNight.badNight.setEngine();
		}
		
	}
	
	static class TimeAttack2Mode extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.gameMode = BadNight.badNight.timeAttack2;
			BadNight.badNight.currentGameMode = BadNight.badNight.timeAttack2;
			BadNight.badNight.engine.labelShowGameMode.setText(BadNight.badNight.engine.gameMode.getName());
			BadNight.badNight.engine.init();
			BadNight.badNight.setEngine();
		}
		
	}
	
	static class TimeAttack5Mode extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.gameMode = BadNight.badNight.timeAttack5;
			BadNight.badNight.currentGameMode = BadNight.badNight.timeAttack5;
			BadNight.badNight.engine.labelShowGameMode.setText(BadNight.badNight.engine.gameMode.getName());
			BadNight.badNight.engine.init();
			BadNight.badNight.setEngine();
		}
		
	}
	
	public SelectGameMode(BadNight game){
		space(GUI.ELEMENT_SPACE);
		GUI gui = GUI.get();
		
		Label play = gui.GiveMeLabel("Play");
		this.addActor(play);
		
		TextButton resistanceMode = gui.giveMeTextButton("Resistance 100");
		resistanceMode.addListener(new Resistance100Mode() );
		this.addActor(resistanceMode);
		
		TextButton timeAttack2Mode = gui.giveMeTextButton("Time 2 min.");
		timeAttack2Mode.addListener( new TimeAttack2Mode() );
		this.addActor(timeAttack2Mode);
		
		TextButton timeAttack5Mode = gui.giveMeTextButton("Time 5 min.");
		timeAttack5Mode.addListener( new TimeAttack5Mode() );
		this.addActor(timeAttack5Mode);
		
		TextButton back = gui.giveMeABackButton("Back", game.mainMenu);
		this.addActor(back);
		this.fill();
		
	}
	
}
