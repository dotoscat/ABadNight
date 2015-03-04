package the_catwolf.BadNight;

import the_catwolf.BadNight.Engine.Layer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Graphics extends Layer.Drawable{
	Sprite sprite;
	Vector2 spriteOffset;
	boolean setRotationFromSpeed;
	float animationStateTime;
	Animation animation;
	float animationTimeFactor;
	
	public Graphics(){
		sprite = new Sprite();
		spriteOffset = new Vector2();
	}

	public void reset(){
		sprite.setAlpha(1f);
		sprite.setColor(Color.WHITE);
		sprite.setRotation(0f);
		setRotationFromSpeed = false;
		animationStateTime = 0f;
		animationTimeFactor = 1f;
		animation = null;
	}
	
	public void update(float x, float y, float dt){
		if (animation != null){
			animationStateTime += dt * animationTimeFactor;
			sprite.setRegion( animation.getKeyFrame(animationStateTime, true) );
		}
		sprite.setPosition(x + spriteOffset.x, y + spriteOffset.y);
	}

	public Sprite getSprite(){
		return sprite;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		sprite.draw(batch);
	}
	
}
