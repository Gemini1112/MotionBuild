package nbu.nlp.examples;

public class GroupWord {
	private String word;
	private String type;
	private double intensity;
	
	public void setWord(String word){
		this.word=word;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setIntensity(double intensity){
		this.intensity=intensity;
	}

	public String getWord(){
		return word;
	}
	public String getType(){
		return type;
	}
	public double getIntensity(){
		return intensity;
	}
}
