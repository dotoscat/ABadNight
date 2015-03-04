package the_catwolf.BadNight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Options extends MenuWithParent {	
	public Slider soundSlider;
	public Slider musicSlider;
	public CheckBox vibrationCheckBox;
	
	private TextButton signGooglePlayServices;
	
	static class SetSoundVolume extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			Slider slider = (Slider)actor;
			BadNight.badNight.gameData.soundVolume = slider.getValue()/slider.getMaxValue();
			BadNight.badNight.playSound("explosion", 1f);
		}
		
	}
	
	static class SetMusicVolume extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			Slider slider = (Slider)actor;
			System.out.println(slider.getValue());
			BadNight.badNight.gameData.musicVolume = slider.getValue()/slider.getMaxValue();
			BadNight.badNight.setMusicVolume(slider.getValue()/slider.getMaxValue());
		}
		
	}
	
	static class SetVibration extends ChangeListener{

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			BadNight.badNight.gameData.usesVibration = !BadNight.badNight.gameData.usesVibration;
			if (BadNight.badNight.gameData.usesVibration){
				Gdx.input.vibrate(500);
			}
		}
		
	}
	
	static class LogoutGooglePlayServices extends ChangeListener{
		
		private GoogleServices googleServices;
	
		public LogoutGooglePlayServices(GoogleServices googleServices){
			this.googleServices = googleServices;
		}
		
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			if (googleServices.isSignedIn()){
				googleServices.signOut();
				return;
			}
			googleServices.signIn();
		}
		
	}
	
	static class GoToOptions extends ChangeListener{

		private VerticalGroup currentMenu;
		
		public GoToOptions(VerticalGroup currentMenu){
			this.currentMenu = currentMenu;
		}
		
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			//actor is a button, which belongs to a "VerticalGroup"
			BadNight.badNight.options.setLastMenu(currentMenu);
			BadNight.badNight.setContainerWithLastMenu(BadNight.badNight.options);
		}
		
	}
	
	static class BackFromOptions extends MenuWithParent.BackButton{

		protected BackFromOptions(MenuWithParent theMenu) {
			super(theMenu);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			super.changed(event, actor);
			BadNight.badNight.gameData.save();
		}
		
	}
	
	public Options(BadNight game){
		space(GUI.ELEMENT_SPACE);
		GUI gui = GUI.get();
		
		HorizontalGroup soundGroup = new HorizontalGroup();
		
		Label soundLabel = gui.GiveMeLabel("Sound");
		soundGroup.space(GUI.ELEMENT_SPACE);
		soundGroup.addActor(soundLabel);
		
		soundSlider = gui.giveMeSlider(0f, 5f, 1f/5f);
		soundSlider.setWidth(128f);
		soundSlider.setHeight(64f);
		soundSlider.setValue(game.gameData.soundVolume * 5f);
		soundSlider.addListener( new SetSoundVolume() );
		soundGroup.addActor(soundSlider);
		
		addActor(soundGroup);
		
		HorizontalGroup musicGroup = new HorizontalGroup();
		musicGroup.space(GUI.ELEMENT_SPACE);
		Label musicLabel = gui.GiveMeLabel("Music");
		musicGroup.addActor(musicLabel);
		
		musicSlider = gui.giveMeSlider(0f, 5f, 1f/5f);
		musicSlider.setWidth(128f);
		musicSlider.setHeight(64f);
		musicSlider.setValue(game.gameData.musicVolume * 5f);
		musicSlider.addListener( new SetMusicVolume() );
		musicGroup.addActor(musicSlider);
		
		addActor(musicGroup);
		
		vibrationCheckBox = gui.giveMeCheckBox("Vibration");
		vibrationCheckBox.setChecked( game.gameData.usesVibration );
		vibrationCheckBox.addListener( new SetVibration() );
		addActor(vibrationCheckBox);
				
		signGooglePlayServices = gui.giveMeTextButton("");
		signGooglePlayServices.addListener( new LogoutGooglePlayServices(game.googleServices) );
		
		addActor(signGooglePlayServices);
		
		TextButton backButton = gui.giveMeTextButton("Back");
		backButton.addListener( new BackFromOptions(this) );
		GUI.setButtonFakeSize(backButton, 0f, 64f);
		addActor(backButton);
		
		fill();
	}
	
	static public TextButton giveMeAOptionsButton(VerticalGroup currentMenu){
		TextButton optionsButton = GUI.get().giveMeTextButton("Options");
		optionsButton.addListener( new GoToOptions(currentMenu) );
		return optionsButton;
	}
	
	public void checkWhetherSignedGooglePlayServices(GoogleServices googleServices){
		if (googleServices.isSignedIn()){
			signGooglePlayServices.setText("Sign out of Google Play Services");
			return;
		}
		signGooglePlayServices.setText("Sign in of Google Play Services");
	}
	
}
