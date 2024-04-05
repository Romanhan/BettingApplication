package eu.romanhan.BettingApplication.exceptions;

public class IllegalWithdrawAmountException extends RuntimeException {

	public IllegalWithdrawAmountException(String message) {
		super(message);
	}

}
