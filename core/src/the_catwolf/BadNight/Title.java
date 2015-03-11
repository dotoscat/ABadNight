package the_catwolf.BadNight;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Title extends VerticalGroup {
	
	private static class GoToMainMenu extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(BadNight.badNight.mainMenu, BadNight.FROM1, BadNight.TO1, BadNight.LEAVE1);
		}
		
	}
	
	public Title(BadNight game){
		
		GUI gui = GUI.get();
		TextureAtlas textureAtlas = game.assets.get("pack.atlas");
		
		Sprite spriteTitle = textureAtlas.createSprite("title");
		spriteTitle.setScale(2f);
		Image title = new Image(spriteTitle);
		this.addActor(title);
		
		Label versionAndRevision = gui.GiveMeLabel(BadNight.getVersionAndRevision());
		this.addActor(versionAndRevision);
		
		TextButton start = gui.giveMeTextButton("START");
		this.addActor(start);
		start.addListener( new GoToMainMenu() );
		GUI.setButtonFakeSize(start, BadNight.VWIDTH/1.2f, 64f);
		
	}
	
}
