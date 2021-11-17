package filters;

public class ToLowerCaseFilter implements Filter {

	@Override
	public String execute(String content) {
		return content.toLowerCase();
	}
	
}
