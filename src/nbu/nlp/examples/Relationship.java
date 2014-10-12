package nbu.nlp.examples;

public class Relationship {
	
	
	private int firstId;
	private String firstCont;
	private String firstPos;
	private int secondId;
	private String secondCont;
	private String secondPos;
	private String relation;
	private double rawScore;
	
	
	public void setfirstId(int firstId){
		this.firstId=firstId;
	}
	
	public void setfirstCont(String firstCont){
		this.firstCont=firstCont;
	}
	
	public void setfirstPos(String firstPos){
		this.firstPos=firstPos;
	}

	
	public void setsecondId(int secondId){
		this.secondId=secondId;
	}
	
	public void setsecondCont(String secondCont){
		this.secondCont=secondCont;
	}
	
	public void setsecondPos(String secondPos){
		this.secondPos=secondPos;
	}
	
	public void setrelation(String relation){
		this.relation=relation;
	}

	public void setrawScore(double rawScore){
		this.rawScore=rawScore;
	}
	
	public int getfirstId(){
		return firstId;
	}
	
	public String getfirstCont(){
		return firstCont;
	}
	
	public String getfirstPos(){
		return firstPos;
	}
	
	public int getsecondId(){
		return secondId;
	}
	
	public String getsecondCont(){
		return secondCont;
	}
	
	public String getsecondPos(){
		return secondPos;
	}
	
	public String getrelation(){
		return relation;
	}
	
	public double getrawScore(){
		return rawScore;
	}
	
}
