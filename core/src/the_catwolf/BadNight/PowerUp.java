package the_catwolf.BadNight;

public abstract class PowerUp {

	public abstract void effect(Engine engine);
	
	//0 - extraHouse
	//1 - incrementMultiplier
	//2 - extraPoints
	
	static PowerUp extraHouse;
	static PowerUp incrementMultiplier;
	static PowerUp extraPoints;
	
	static PowerUp[] powerUp;
	
	public static enum Type{
		EXTRA_HOUSE,
		INCREMENT_MULTIPLIER,
		EXTRA_POINTS
	}
	
	static class ExtraHouse extends PowerUp{

		@Override
		public void effect(Engine engine) {
			// TODO Auto-generated method stub
			engine.getMessageSystem().addMessage(MessageSystem.Type.CREATE_HOUSE, "create house");
		}
		
	}
	
	static class IncrementMultiplier extends PowerUp{

		@Override
		public void effect(Engine engine) {
			// TODO Auto-generated method stub
			engine.getScore().incrementMultiplier();
		}
		
	}
	
	static class ExtraPoints extends PowerUp{

		@Override
		public void effect(Engine engine) {
			engine.getScore().addPoints(10000L, 1L, 1L);
		}
		
	}
	
	static public void init(){
		
		powerUp = new PowerUp[Type.values().length];
				
		powerUp[0] = new ExtraHouse();
		powerUp[1] = new IncrementMultiplier();
		powerUp[2] = new ExtraPoints();
	}	
}
