package eu.romanhan.BettingApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BettingApplication {

	private static List<Match> matches = new ArrayList<>();
	private static List<Player> players = new ArrayList<>();

	public static void main(String[] args) {
		readMatchData();
		readPlayerData();

		Casino casino = new Casino(matches);
		casino.processAction(players);
	}

	public static void readMatchData() {
		try (InputStream inputStream = BettingApplication.class.getClassLoader().getResourceAsStream("match_data.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;

			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");

				UUID matchId = UUID.fromString(data[0]);
				double sideA = Double.parseDouble(data[1]);
				double sideB = Double.parseDouble(data[2]);
				String side = data[3];

				matches.add(new Match(matchId, sideA, sideB, side));
			}

		} catch (IOException e) {
			System.out.println("Error in reading Match!");
			e.printStackTrace();
		}
	}

	public static void readPlayerData() {
		try (InputStream inputStream = BettingApplication.class.getClassLoader().getResourceAsStream("player_data.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			Player player = null;
			List<String[]> actionData = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");

				UUID currentPlayerId = UUID.fromString(data[0]);

				if (player == null) {
					player = new Player(currentPlayerId);
					actionData = new ArrayList<>();
				}

				if (!player.getPlayerId().equals(currentPlayerId)) {
					player.setActionData(actionData);
					players.add(player);

					player = new Player(currentPlayerId);
					actionData = new ArrayList<>();
				}
				actionData.add(readPlayerActions(data));
			}

			if (player != null) {
				player.setActionData(actionData);
				players.add(player);
			}

		} catch (IOException e) {
			System.out.println("Error in reading Player!");
			e.printStackTrace();
		}

	}

	public static String[] readPlayerActions(String[] data) {

		String[] actionDetails = null;

		switch (data[1]) {
		case "DEPOSIT" -> {
			String depositAmount = data[3];
			actionDetails = new String[] { "DEPOSIT", depositAmount };
		}
		case "BET" -> {
			String matchId = data[2];
			String betAmount = data[3];
			String betSide = data[4];
			actionDetails = new String[] { "BET", matchId, betAmount, betSide };
		}
		case "WITHDRAW" -> {
			String withdrawAmount = data[3];
			actionDetails = new String[] { "WITHDRAW", withdrawAmount };
		}
		}
		return actionDetails;
	}

}
