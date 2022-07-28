package Login_pkg;

public class LoginData {
	private String ID;
	private String Password;
	public LoginData() { }
	public LoginData(String ID, String Password){
		this.ID = ID;
		this.Password = Password;
		System.out.println("ID"+ID);
	}
	
	public LoginData(String ID){
		this.ID = ID;
	}
	
	public String GetID(){
		return ID;
	}
	public void SetID(String Password){
		this.Password = Password;
	}
	public String GetPassword(){
		return Password;
	}
	public void SetPasswordD(String Password){
		this.Password = Password;
	}
	
}
