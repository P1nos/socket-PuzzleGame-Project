package Puzzle_pkg;


public class PuzzleData {
	private String ID,Time,Rank;
	public PuzzleData() {}
	public PuzzleData(String ID, String Time){
		this.ID = ID;
		this.Time = Time;
	}
	
	public PuzzleData(String ID, String Time, String Rank){
		this.ID = ID;
		this.Time = Time;
		this.Rank = Rank;
	}
	
	public PuzzleData(String ID){
		this.ID = ID;
	}
	
	
	public String GetID(){
		return ID;
	}
	public void SetID(String ID){
		this.ID = ID;
	}
	public String GetTime(){
		return Time;
	}
	public void SetTime(String Rank){
		this.Time = Rank;
	}
	public String GetRank(){
		return Rank;
	}
	public void SetRank(String Rank){
		this.Rank = Rank;
	}
	
}
