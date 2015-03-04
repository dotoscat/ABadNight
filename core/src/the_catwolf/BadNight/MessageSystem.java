package the_catwolf.BadNight;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;

import the_catwolf.BadNight.Engine.Layer;
import the_catwolf.BadNight.MessageSystem.Message;

public class MessageSystem implements Iterable<Message>, Iterator<Message> {
		
	static public enum Type{
		NONE,
		CREATE_EXPLOSION,
		DESTROY_ENTITY,
		CREATE_METEOR,
		CREATE_POWERUP,
		CREATE_HOUSE,
		CREATE_PARTICLE,
		CREATE_UFO,
		DROP_PARALIZER,
		CREATE_PARALIZED_EFFECT
	}
		
	static public class Message{
		private String origin;
		
		public Type type = Type.NONE;
		public String key;
		public Layer layer;
		public float positionX;
		public float positionY;
		public float velX;
		public float velY;
		public float value;
		public Color color;
		public int i = 0;
		
		public String getOrigin(){
			return origin;
		}
	}
	
	int nMessages;
	private Message[] message;
	private int currentMessage = 0;
		
	public MessageSystem(int nMessages){
		this.nMessages = nMessages;
		message = new Message[nMessages];
		for(int i = 0; i < nMessages; i += 1) message[i] = new Message();
	}
	
	public Message addMessage (Type type, String origin){
		for(Message aMessage: message){
			if (aMessage.type == Type.NONE){
				aMessage.type = type;
				aMessage.origin = origin;
				return aMessage;
			}
		}
		return null;
	}
		
	public void cleanMessages(){
		for(Message aMessage: message) aMessage.type = Type.NONE;
	}

	@Override
	public Iterator<Message> iterator() {
		currentMessage = -1;
		return this;
	}

	@Override
	public boolean hasNext() {
		return currentMessage < nMessages-1;
	}

	@Override
	public Message next() {
		if (currentMessage == -1){
			currentMessage += 1;
			return message[0];
		}
		message[currentMessage].type = Type.NONE;
		currentMessage += 1;
		return message[currentMessage];
	}

	@Override
	public void remove() {
		//does nothing
	}
	
}
