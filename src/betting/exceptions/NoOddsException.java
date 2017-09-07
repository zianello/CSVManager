package betting.exceptions;

public class NoOddsException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoOddsException()
	  {
	    super("Non esistono quote per questa categoria");
	  }
	}