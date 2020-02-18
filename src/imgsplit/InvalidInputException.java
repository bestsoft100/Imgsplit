package imgsplit;

public class InvalidInputException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String inputName;
	private String input;
	private String description;
	
	public InvalidInputException(String inputName, String input, String description) {
		super("Invalid input \""+input+"\" in \""+inputName+"\""+(description!=null?": "+description:""));
		this.inputName = inputName;
		this.input = input;
		this.description = description;
		
		
	}
	
	public String getInput() {
		return input;
	}
	
	public String getInputName() {
		return inputName;
	}
	
	public String getDescription() {
		return description;
	}
	
}
