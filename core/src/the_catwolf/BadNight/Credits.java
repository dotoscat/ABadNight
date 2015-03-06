package the_catwolf.BadNight;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class Credits extends VerticalGroup{

	public Credits(BadNight game){
		this.space(GUI.ELEMENT_SPACE);
		
		GUI gui = GUI.get();
		TextureAtlas textureAtlas = game.assets.get("pack.atlas");
		
		Sprite spriteTitle = textureAtlas.createSprite("title");
		spriteTitle.setScale(2f);
		Image title = new Image(spriteTitle);
		this.addActor(title);
		
		Label versionAndRevision = gui.GiveMeLabel(BadNight.getVersionAndRevision());
		this.addActor(versionAndRevision);
		
		Label author = gui.GiveMeLabel("Design, development and graphics\nOscar Triano García (dotteri_thecatwolf)");
		author.setWidth(BadNight.VWIDTH);
		this.addActor(author);
		
		Label musician = gui.GiveMeLabel("Music, Orry Zickefoose (AphelionSounds)");
		this.addActor(musician);
		
		TextButton back = gui.giveMeABackButton("Back", game.mainMenu);
		this.addActor(back);
		GUI.setButtonFakeSize(back, 128f, 32f);
	}
	
}
