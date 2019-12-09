package exception;

public class RequestTimedOut extends RuntimeException{
	
	 public RequestTimedOut() {
	      super("Database request timed out");
	    }

}
