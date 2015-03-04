package the_catwolf.BadNight;

import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ScoreScreen extends MenuWithParent {
	private Label name;
	private Label[] entry;
	private StringBuilder entryBuilder;
		
	public ScoreScreen(BadNight game, int entries){
		name = GUI.get().GiveMeLabel("");
		
		this.addActor(name);
		
		entry = new Label[entries];
		for(int i = 0; i < entries; i += 1){
			entry[i] = GUI.get().GiveMeLabel("");
			addActor(entry[i]);
		}
		entryBuilder = new StringBuilder();
		
		HorizontalGroup controlGroup = new HorizontalGroup();
		TextButton backButton = this.giveMeBackButtonToLastMenu("Back");
		GUI.setButtonFakeSize(backButton, 128f, 42f);
		
		controlGroup.addActor(backButton);
		addActor(controlGroup);
		
	}
	
	public void fill(Score score){
		
		name.setText(score.getName());
		
		score.load();
		for(int i = 0; i < score.entry.length && i < entry.length; i += 1){
			entryBuilder.setLength(0);
			entryBuilder.append(i+1);
			entryBuilder.append(". ");
			entryBuilder.append(score.entry[i].name);
			entryBuilder.append(" - ");
			entryBuilder.append(score.entry[i].points);
			//entry[i].setText(i+1 + ". " + score.entry[i].name + " - " + score.entry[i].points);
			entry[i].setText(entryBuilder);
		}
		
		this.fill();
		
	}
		
}
