package betting.exceptions;

public class BetterNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public BetterNotFoundException()
	  {
	    super("La casa scommettitrice non è stata trovata");
	  }
	}