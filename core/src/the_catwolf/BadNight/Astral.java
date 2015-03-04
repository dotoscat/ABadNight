package the_catwolf.BadNight;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

public class Astral {
	
	public static final Vector2[] standardPath = {
		new Vector2(64f, 0f),
		new Vector2(BadNight.VWIDTH/2f, BadNight.VHEIGHT*2f - BadNight.VHEIGHT/4f),
		new Vector2(BadNight.VWIDTH - 64f, 0f)
	};
	
	private Bezier<Vector2> path;
	
	public Astral(){
		path = new Bezier<Vector2>();
	}
	
	public void set(Vector2[] path2){
		path.set(path2);
	}
		
	public void update(Physics physics, float value, float maxValue){
		path.valueAt(physics.position, value / maxValue);
	}
	
}
