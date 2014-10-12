package nbu.nlp.examples;

public class MotionWord {

	private int id;
	private String text;
	private double score;
	private String type;
	
	public void setId(int id){
		this.id=id;
	}
	public void setText(String text){
		this.text=text;
	}
	public void setScore(double score){
		this.score=score;
	}
	public void setType(String type){
		this.type=type;
	}
	
	
	public  int getId(){
		return id;
	}
	public String getText(){
		return text;
	}
	public double getScore(){
		return score;
	}
	public String getType(){
		return type;
	}
}
