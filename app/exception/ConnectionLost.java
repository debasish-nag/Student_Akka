package exception;

public class ConnectionLost extends RuntimeException{
	
	public ConnectionLost() {
	      super("Database connection lost");
	    }
}
