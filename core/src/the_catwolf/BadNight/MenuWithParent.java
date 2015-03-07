package the_catwolf.BadNight;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class MenuWithParent extends Window {

	private Actor lastMenu;
	
	public static class BackButton extends ChangeListener{

		private MenuWithParent menuWithParent;
		
		protected BackButton(MenuWithParent theMenu){
			menuWithParent = theMenu;
		}
		
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(menuWithParent.getLastMenu());
		}
		
	}
	
	public MenuWithParent(String title, WindowStyle style) {
		super(title, style);
		this.setMovable(false);
		this.setResizable(false);
		// TODO Auto-generated constructor stub
	}
	
	protected TextButton giveMeBackButtonToLastMenu(String text){
		TextButton newButton = GUI.get().giveMeTextButton(text);
		newButton.addListener( new BackButton(this) );
		return newButton;
	}
	
	public void setLastMenu(Actor last){
		lastMenu = last;
	}
	
	public Actor getLastMenu(){
		return lastMenu;
	}
	
}
