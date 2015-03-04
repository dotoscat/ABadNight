package the_catwolf.BadNight;

import com.badlogic.gdx.math.Circle;

public class Collision {
	
	final static int ROCKET = 0x01;
	final static int METEOR = 0x02;
	final static int BUILDING = 0x04;
	final static int UFO = 0x08;
	final static int POWER_UP = 0x10;
	final static int LAUNCHER = 0x20;
	final static int PARALIZER = 0x40;
	
	Circle circle;
	float circleOffsetX;
	float circleOffsetY;
	int isA;
	int collidesWith;
	
	public Collision(){
		circle = new Circle();
	}
	
	public void reset(){
		isA = 0;
		collidesWith = 0;
		circleOffsetX = 0f;
		circleOffsetY = 0f;
	}
	
	public void update(float x, float y){
		circle.x = x + circleOffsetX;
		circle.y = y + circleOffsetY;
	}
	
}
