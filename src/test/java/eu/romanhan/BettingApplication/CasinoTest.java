package eu.romanhan.BettingApplication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class CasinoTest {

	@Test
	void testProcessActionDeposit() {
		Player player = new Player();
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		actionData.add(action);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61639"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		assertThat(player.getBalance(), is(equalTo(4000L)));
	}

	@Test
	void testProcessActionWithdraw() {
		Player player = new Player();
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		String[] action2 = { "WITHDRAW", "200" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61640"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		assertThat(player.getBalance(), is(equalTo(3800L)));
	}

	@Test
	void testProcessActionBetOnWinSide() {
		Player player = new Player();
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61641", "500", "A" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61641"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		assertThat(player.getBalance(), is(equalTo(4225L)));
	}

	@Test
	void testProcessActionBetOnLoseSide() {
		Player player = new Player();
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61642", "500", "B" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61642"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		assertThat(player.getBalance(), is(equalTo(3500L)));
	}

	@Test
	void testProcessActionBetOnDraw() {
		Player player = new Player();
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61643", "500", "B" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61643"), 1.45, 0.75, "DRAW")));
		casino.processAction(List.of(player));

		assertThat(player.getBalance(), is(equalTo(4000L)));
	}

	@Test
	void testProcessActionInsufficientFunds() {
		Player player = new Player();
		player.setPlayerId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "400" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61646", "500", "B" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61646"), 1.45, 0.75, "DRAW")));

		Field illegitimatePlayersField = null;
		List<Player> illegitimatePlayers = null;
		try {
			illegitimatePlayersField = Casino.class.getDeclaredField("illegitimatePlayers");
			illegitimatePlayersField.setAccessible(true);

			casino.processAction(List.of(player));

			illegitimatePlayers = (List<Player>) illegitimatePlayersField.get(casino);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		assertTrue(illegitimatePlayers.contains(player));
	}

	@Test
	void testProcessActionIllegalWithdrawAmountException() {
		Player player = new Player();
		player.setPlayerId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "400" };
		String[] action2 = { "WITHDRAW", "600" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61647"), 1.45, 0.75, "DRAW")));

		Field illegitimatePlayersField = null;
		List<Player> illegitimatePlayers = null;
		try {
			illegitimatePlayersField = Casino.class.getDeclaredField("illegitimatePlayers");
			illegitimatePlayersField.setAccessible(true);

			casino.processAction(List.of(player));

			illegitimatePlayers = (List<Player>) illegitimatePlayersField.get(casino);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		assertTrue(illegitimatePlayers.contains(player));
	}

	@Test
	void testWriteResultsLegalPlayerAction() {
		Player player = new Player();
		player.setPlayerId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "4000" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61648", "500", "A" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		try {
			Files.deleteIfExists(Paths.get("result.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61648"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		StringBuilder actualLines = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("result.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					actualLines.append(line).append(System.lineSeparator());
				}
				actualLines.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(actualLines.toString().contains("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4, 4225, 1.00"),
				"Player output is not Equals");
		assertTrue(actualLines.toString().contains("-725"), "Casino balance is not Equals");
	}

	@Test
	void testWriteResultsIlleegalPlayerAction() {
		Player player = new Player();
		player.setPlayerId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "20" };
		String[] action2 = { "BET", "abae2255-4255-4304-8589-737cdff61649", "100", "B" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		try {
			Files.deleteIfExists(Paths.get("result.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61649"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		StringBuilder actualLines = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("result.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					actualLines.append(line).append(System.lineSeparator());
				}
				actualLines.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(
				actualLines.toString().contains(
						"163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61649 100 B"),
				"Player output is not Equals");
	}

	@Test
	void testWriteResultsIlleegalPlayerWithdraw() {
		Player player = new Player();
		player.setPlayerId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
		List<String[]> actionData = new ArrayList<>();
		String[] action = { "DEPOSIT", "100" };
		String[] action2 = { "WITHDRAW", "1000" };
		actionData.add(action);
		actionData.add(action2);
		player.setActionData(actionData);

		try {
			Files.deleteIfExists(Paths.get("result.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Casino casino = new Casino(
				List.of(new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61650"), 1.45, 0.75, "A")));
		casino.processAction(List.of(player));

		StringBuilder actualLines = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("result.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					actualLines.append(line).append(System.lineSeparator());
				}
				actualLines.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(actualLines.toString().contains("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 WITHDRAW null 1000 null"),
				"Player output for Widthraw is not Equals");
	}

}
