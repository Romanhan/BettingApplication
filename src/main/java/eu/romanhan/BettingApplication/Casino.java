package eu.romanhan.BettingApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import eu.romanhan.BettingApplication.exceptions.IllegalWithdrawAmountException;
import eu.romanhan.BettingApplication.exceptions.InsufficientFundsException;

public class Casino {

	private List<Match> matches;
	private List<Player> legitimatePlayers = new ArrayList<Player>();
	private List<Player> illegitimatePlayers = new ArrayList<Player>();

	private long totalBalance;

	public Casino(List<Match> matches) {
		this.matches = matches;
		this.totalBalance = 0;
	}

	public void processAction(List<Player> players) {
		Player player;

		for (int i = 0; i < players.size(); i++) {
			player = players.get(i);

			List<String[]> actionData = player.getActionData();

			for (int y = 0; y < actionData.size(); y++) {
				String[] action = actionData.get(y);

				try {
					switch (action[0]) {
					case "DEPOSIT" -> {
						int amount = Integer.parseInt(action[1]);
						player.deposit(amount);

					}
					case "WITHDRAW" -> {
						int amount = Integer.parseInt(action[1]);
						player.withdraw(amount);

					}
					case "BET" -> {
						UUID matchId = UUID.fromString(action[1]);
						int amount = Integer.parseInt(action[2]);
						String side = action[3];
						Match match = findMatch(matchId);

						int betForeGame = player.placeBet(match, amount, side);
						totalBalance += betForeGame;
					}
					}
				} catch (InsufficientFundsException | IllegalWithdrawAmountException e) {
					player.setHasIllegalAction(true);
					if (e instanceof InsufficientFundsException) {
						String[] lastAction = new String[] { String.valueOf(player.getPlayerId()), action[0], action[1],
								action[2], action[3] };
						List<String[]> resultList = new ArrayList<>();
						resultList.add(lastAction);
						player.setActionData(resultList);
					} else if (e instanceof IllegalWithdrawAmountException) {
						String[] lastAction = new String[] { action[0], null, action[1], null };
						List<String[]> resultList = new ArrayList<>();
						resultList.add(lastAction);
						player.setActionData(resultList);
					}
					illegitimatePlayers.add(player);
					writeResults();
					return;
				}
			}
			legitimatePlayers.add(player);
		}
		writeResults();
	}

	private Match findMatch(UUID matchId) {
		return matches.stream().filter(match -> match.getMatchId().equals(matchId)).findFirst().orElseThrow();
	}

	public void writeResults() {
		try (Writer writer = new FileWriter("result.txt", true)) {
			if (legitimatePlayers.isEmpty()) {
				writer.write("\n");
			} else {
				for (Player legatimatePlayer : legitimatePlayers) {
					writer.write("%s, %s, %s\n".formatted(legatimatePlayer.getPlayerId(), legatimatePlayer.getBalance(),
							legatimatePlayer.calculateWinRate()));
				}
			}
			writer.write("\n");

			if (illegitimatePlayers.isEmpty()) {
				writer.write("\n");
			} else {
				for (Player illegitimatePlayer : illegitimatePlayers) {
					String allActions = illegitimatePlayer.getActionData().stream().flatMap(arr -> Arrays.stream(arr))
							.collect(Collectors.joining(" "));
					writer.write("%s %s\n".formatted(illegitimatePlayer.getPlayerId(), allActions));
				}
			}
			writer.write("\n" + totalBalance + "\n");
		} catch (IOException e) {
			System.err.println("Error while writing result.txt " + e.getMessage());
		}
	}
}
