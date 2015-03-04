package the_catwolf.BadNight;

import the_catwolf.BadNight.Engine.Launcher;
import the_catwolf.BadNight.MessageSystem.Message;
import the_catwolf.BadNight.MessageSystem.Type;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

public class UFO {

	float maxTime;
	float time;
	
	private static int POSITIONS = 4;
	Vector2[] position;
	Path<Vector2> path;
	
	private long soundID = -1;
	
	public boolean hostile;
	private boolean shoot;
	
	public UFO(){
		position = new Vector2[POSITIONS];
		for(int i = 0; i < POSITIONS; i +=1 ) position[i] = new Vector2();
		path = new Bezier<Vector2>(position);
	}
	
	public void init(float maxTime, float y, float height, float ufoRadius){
		this.time = 0f;
		this.maxTime = maxTime;
		
		float startX = BadNight.VWIDTH/2f;
		float endX = BadNight.VWIDTH;
		int step = 1;
		
		if (MathUtils.randomBoolean()){
			startX = -ufoRadius;
			endX = BadNight.VWIDTH + ufoRadius;
		}else{
			startX = BadNight.VWIDTH + ufoRadius;
			endX = -ufoRadius;
			step = -step;
		}
		
		for(int i = 0; i < POSITIONS; i += 1){
			if (i == 0){
				position[i].x = startX;
			}
			else if (i == POSITIONS-1){
				position[i].x = endX;
			}
			else{
				/*
				if (endX > startX){
					position[i].x = i * step * (BadNight.VWIDTH / 4f);
				}
				else if (startX < endX){
					position[i].x = i * step * (BadNight.VWIDTH - BadNight.VWIDTH / 4f);
				}
				*/
				position[i].x = MathUtils.random(BadNight.VWIDTH);
			}
			position[i].y = MathUtils.random(y, height);
		}
		hostile = false;
		shoot = false;
	}
	
	public boolean process(Physics physics, float dt){
		time += dt;
		path.valueAt(physics.position, time/maxTime);
		return time > maxTime;
	}
	
	public void think(Physics ufoPhysics, Engine engine){
		if (!hostile) return;
		//if this UFO is hostile towards player then drops something nasty
		Launcher launcher = engine.getLauncer();
		float ufoX = ufoPhysics.position.x;
		float baseX = engine.collision[launcher.getBase()].circle.x;
		float baseRadius = engine.collision[launcher.getBase()].circle.radius;
		if (shoot == false && ufoX > baseX - baseRadius && ufoX < baseX + baseRadius){
			Message aMessage = engine.getMessageSystem().addMessage(Type.DROP_PARALIZER, "ufo::think");
			aMessage.positionX = ufoPhysics.position.x;
			aMessage.positionY = ufoPhysics.position.y;
			aMessage.value = 3f;
			shoot = true;
		}
	}
	
	public void setSoundID(long id){
		soundID = id;
	}
	
	public long getSoundID(){
		return soundID;
	}
	
}
