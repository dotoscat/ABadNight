package the_catwolf.BadNight;

import com.badlogic.gdx.math.Vector2;

public class Physics {
	Vector2 position;
	Vector2 velocity;
	Vector2 acceleration;
	float gravityScale;
	
	public Physics(){
		position = new Vector2();
		velocity = new Vector2();
		acceleration = new Vector2();
		gravityScale = 0.f;
	}
	
	public void update(float dt, float g){
		velocity.x += acceleration.x * dt;
		velocity.y += acceleration.y * dt + g * gravityScale * dt;
		position.x += velocity.x * dt;
		position.y += velocity.y * dt;
	}
	
	public void reset(){
		velocity.setZero();
		acceleration.setZero();
		gravityScale = 0f;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
}
