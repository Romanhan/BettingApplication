package eu.romanhan.BettingApplication.exceptions;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(String message) {
		super(message);
	}

}
