package eu.romanhan.BettingApplication;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Match {

	private UUID matchId;
	private double rateA;
	private double rateB;
	private String result;

	public long calculateWinnings(String side, long amount) {
		return (long) (side.equals("A") ? rateA * amount : rateB * amount);
	}

}
