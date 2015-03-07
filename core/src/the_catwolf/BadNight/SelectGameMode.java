package the_catwolf.BadNight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SelectGameMode extends Window {

	static class Resistance100Mode extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.engine.gameMode = BadNight.badNight.resistance100;
			BadNight.badNight.currentGameMode = BadNight.badNight.resistance100;
			BadNight.badNight.engine.gameOptions.setTitle((String)BadNight.badNight.engine.gameMode.getName());
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
			BadNight.badNight.engine.gameOptions.setTitle((String)BadNight.badNight.engine.gameMode.getName());
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
			BadNight.badNight.engine.gameOptions.setTitle((String)BadNight.badNight.engine.gameMode.getName());
			BadNight.badNight.engine.init();
			BadNight.badNight.setEngine();
		}
		
	}
	
	public SelectGameMode(BadNight game){
		super("Play", GUI.get().getWindowStyle());
		GUI gui = GUI.get();
		
		this.pad(GUI.ELEMENT_SPACE2);
		this.padTop(GUI.ELEMENT_SPACE3);
		this.setWidth(BadNight.VWIDTH/2f);
		this.setBackground(gui.giveMeWindowsDrawable());
		this.setMovable(false);
		this.setResizable(false);
		
		TextButton resistanceMode = gui.giveMeTextButton("Resistance 100");
		resistanceMode.addListener(new Resistance100Mode() );
		this.add(resistanceMode).fill().space(GUI.ELEMENT_SPACE2).row();
		
		TextButton timeAttack2Mode = gui.giveMeTextButton("Time 2 min.");
		timeAttack2Mode.addListener( new TimeAttack2Mode() );
		this.add(timeAttack2Mode).fill().space(GUI.ELEMENT_SPACE2).row();
		
		TextButton timeAttack5Mode = gui.giveMeTextButton("Time 5 min.");
		timeAttack5Mode.addListener( new TimeAttack5Mode() );
		this.add(timeAttack5Mode).fill().space(GUI.ELEMENT_SPACE2).row();
		
		TextButton back = gui.giveMeABackButton("Back", game.mainMenu);
		this.add(back).space(GUI.ELEMENT_SPACE2).fill();	
	}
	
}
