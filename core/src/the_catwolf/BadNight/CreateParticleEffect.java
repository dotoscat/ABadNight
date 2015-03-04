package the_catwolf.BadNight;

import the_catwolf.BadNight.Engine.Layer;
import the_catwolf.BadNight.MessageSystem.Message;
import the_catwolf.BadNight.MessageSystem.Type;

import com.badlogic.gdx.math.Vector2;

public class CreateParticleEffect {
		
	public static class Point{
		public String key;
		public Vector2 offset;
		public Vector2 position;
		public Layer layer;
		
		public Point(){
			offset = new Vector2();
			position = new Vector2();
		}
		
		public void process(Vector2 position, float angle){
			this.position.set(offset);
			this.position.rotate(angle);
			this.position.add(position);
		}
		
	}
		
	private Point[] point;
	private int usedPoints = 0;
	
	public CreateParticleEffect(int nPoints){
		point = new Point[nPoints];
		for(int i = 0; i < nPoints; i += 1) point[i] = new Point();
	}
	
	public void process(Vector2 position, float rotation, Engine engine){
		Message aMessage = engine.getMessageSystem().addMessage(Type.CREATE_PARTICLE, "create particle");
		
		for (int i = 0; i < usedPoints; i += 1){
			Point aPoint = point[i];
			aPoint.process(position, rotation);
			
			aMessage.key = aPoint.key;
			aMessage.positionX = aPoint.position.x;
			aMessage.positionY = aPoint.position.y;
			aMessage.layer = aPoint.layer;
		}
		
	}
		
	public Point getPoint(int i){
		if (i < 0 || i > usedPoints) return null;
		return point[i];
	}
	
	public void setUsedPoints(int used){
		usedPoints = used;
	}
	
}
