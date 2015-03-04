package the_catwolf.BadNight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StringBuilder;

public class Score {
	
	public static class Entry{
		StringBuilder name;
		public long points;
		
		public Entry(){
			name = new StringBuilder();
			points = 0L;
		}
		
		public void setName(CharSequence newName){
			name.setLength(0);
			name.append(newName);
		}
		
		CharSequence getName(){
			return name;
		}
		
	}
	
	String name = "";
	Preferences file;
	public Entry[] entry;
	
	public Score(String fileName, int entries, String name){
		this.name = name;
		file = Gdx.app.getPreferences("dotteriTheCatwolf.ABadNight."+fileName);
		entry = new Entry[entries];
		for(int i = 0; i < entries; i += 1){
			entry[i] = new Entry();
		}
	}
		
	public int getNewRecordIndex(long points){
		int iEntry = -1;
		for(int i = 0; i < entry.length && iEntry == -1; i += 1){
			if (points > entry[i].points){
				iEntry = i;
			}
		}
		return iEntry;
	}
	
	public void insertNewRecord(int iEntry, CharSequence name, long points){
		for(int i = entry.length-1; i > iEntry; i -= 1){
			entry[i].points = entry[i-1].points;
			entry[i].setName( entry[i-1].getName() );
		}
		entry[iEntry].setName(name);
		entry[iEntry].points = points;
	}
	
	public void load(){
		for(int i = 0; i < entry.length; i += 1){
			String key = Integer.toString(i)+"Name";
			entry[i].setName(Base64Coder.decodeString(file.getString(key, "")));
			key = Integer.toString(i)+"Points";
			String field = file.getString(key);
			if (!field.contentEquals("")){
				entry[i].points = Integer.parseInt( Base64Coder.decodeString(field) );
			}
			
		}
	}
	
	public void save(){
		for(int i = 0; i < entry.length; i += 1){
			file.putString(Integer.toString(i)+"Name", Base64Coder.encodeString(entry[i].getName().toString()));
			file.putString(Integer.toString(i)+"Points", Base64Coder.encodeString(Long.toString(entry[i].points)) );
		}
		file.flush();
	}
	
	public String getName(){
		return name;
	}
	
}
