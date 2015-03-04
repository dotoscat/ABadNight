package the_catwolf.BadNight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class GUI {
	
	private static GUI GUI;
	
	private NinePatch window;
	private NinePatchDrawable windowDrawable;
	private NinePatch buttonUp;
	private NinePatch buttonPressed;
	
	private TextButton.TextButtonStyle textButtonStyle;
	private CheckBox.CheckBoxStyle checkBoxStyle;
	private SliderStyle sliderStyle;
	private Label.LabelStyle labelStyle;
	
	static public void init(){
		GUI = new GUI(BadNight.badNight);
	}
	
	static public GUI get(){
		return GUI;
	}
	
	final static float ELEMENT_SPACE = 8f;
	final static float BUTTON_HEIGHT = 64;
	
	private static class Back extends ChangeListener{

		private VerticalGroup lastMenu;
		
		public Back(VerticalGroup lastMenu){
			this.lastMenu = lastMenu;
		}
		
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.setContainer(lastMenu);
		}
		
	}
	
	public GUI(BadNight game){
		TextureAtlas graphics = (TextureAtlas)game.assets.get("pack.atlas");
		window = graphics.createPatch("window");
		windowDrawable = new NinePatchDrawable(window);
		buttonUp = graphics.createPatch("buttonUp");
		buttonPressed = graphics.createPatch("buttonPressed");
		
		NinePatchDrawable buttonPressedNinePatch = new NinePatchDrawable(buttonPressed);
		NinePatchDrawable buttonUpNinePatch = new NinePatchDrawable(buttonUp);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.font = BadNight.badNight.font;
		textButtonStyle.down = buttonPressedNinePatch;
		textButtonStyle.up = buttonUpNinePatch;
		
		Sprite checkboxOff = graphics.createSprite("checkboxOff");
		Sprite checkboxOn = graphics.createSprite("checkboxOn");
		
		checkBoxStyle = new CheckBoxStyle(
			new SpriteDrawable( checkboxOff ),
			new SpriteDrawable( checkboxOn ),
			game.font,
			Color.WHITE
		);
		
		/*
		imageButtonStyle = new ImageButtonStyle();
		imageButtonStyle.down = new NinePatchDrawable(steel);
		imageButtonStyle.up = imageButtonStyle.down;
		*/
		sliderStyle = new Slider.SliderStyle(
			new NinePatchDrawable( graphics.createPatch("sliderBackground") ),
			new SpriteDrawable( graphics.createSprite("knob") )
		);
		labelStyle = new Label.LabelStyle(game.font, Color.WHITE);
	}
	
	public NinePatchDrawable giveMeWindowsDrawable(){
		return windowDrawable;
	}
	
	public TextButton giveMeTextButton(String text){
		TextButton newButton = new TextButton(text, textButtonStyle);
		return newButton;
	}
	
	public TextButton giveMeABackButton(String text, VerticalGroup last){
		TextButton newButton = giveMeTextButton(text);
		newButton.addListener( new Back(last) );
		return newButton;
	}
	
	public Label GiveMeLabel(String text){
		Label newLabel = new Label(text, labelStyle);
		return newLabel;
	}
	
	public Slider giveMeSlider(float min, float max, float step){
		Slider newSlider = new Slider(min, max, 1f, false, sliderStyle);
		return newSlider;
	}
	
	public CheckBox giveMeCheckBox(String text){
		CheckBox newCheckBox = new CheckBox(text, checkBoxStyle);
		return newCheckBox;
	}
	
	static public void setButtonFakeSize(Button button, float width, float height){
		float padWidth = (width - button.getWidth() )/2f;
		float padHeight = (height - button.getHeight() )/2f;
		button.pad(padHeight, padWidth, padHeight, padWidth);
	}
	
}
