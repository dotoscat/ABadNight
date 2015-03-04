package the_catwolf.BadNight;

public class LifeSpan {

	private float time;
	private float total;
	public boolean show = true;
	static float BLINK = 0.07f;
	private float blink = 0f;
	
	public void update(float dt){
		time += dt;
		if (isAboutToDissappear()){
			blink += dt;
			if (blink > BLINK){
				blink = 0f;
				show = !show;
			}
		}
	}
	
	public void set(float total){
		this.total = total;
		time = 0f;
	}
	
	public boolean isOver(){
		return time > total;
	}
	
	public boolean isAboutToDissappear(){
		return time > total-total/4f;
	}
	
}
