package eu.romanhan.BettingApplication;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Match {

	private UUID matchId;
	private double rateA;
	private double rateB;
	private String result;

	public int calculateWinnings(String side, int amount) {
		if (result.equals("DRAW")) {
			return amount;
		} else if (result.equals("A") && side.equals("A")) {
			return (int) (rateA * amount);
		} else if (result.equals("B") && side.equals("B")) {
			return (int) (rateB * amount);
		}
		return 0;
	}

}
