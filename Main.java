import java.util.Scanner;

public class Main {
	static int chouse = 0;
	static boolean isNotChousen = true;
	static String[] games = { "Stock Market Game (ready for production)", "Worm Game (alfa ver ready)",
			"Tetris (alfa ver ready)", "BattleShip (ready, might have a bug)", "Exit" };
	static Scanner in = new Scanner(System.in);
	static String userIn;

	public static void main(String[] args) {
		begin();
	}

	public static void begin() {
		Display.start();
		menue();
		startGame();
	}

	static void menue() {
		do {
			Display.clear();
			Display.line();
			System.out.println("Please choose game using " + Color.yellow + "w " + Color.reset + "and " + Color.green + "s"
					+ Color.reset + " key,\nand press " + Color.red + "e " + Color.reset + "to start chousen game from list:\n");
			for (int i = 0; i < games.length; i++) {
				System.out.println((chouse == i ? Color.red + "==> " : "    ") + Color.blue + games[i] + Color.reset);
			}
			System.out.println("Press Enter to continue");

			userIn = in.next();
			switch (userIn) {
			case "w":
				if (chouse > 0)
					chouse--;
				else
					chouse = games.length - 1;
				break;
			case "s":
				if (chouse < games.length - 1)
					chouse++;
				else
					chouse = 0;
				break;
			case "e":
				isNotChousen = false;
				break;
			default:
				System.out.println("Invalid key entered.");
			}

		} while (isNotChousen);
		isNotChousen = true;
	}

	static void startGame() {
		switch (chouse) {
		case 0:
			StockGame.stockGame();
			break;
		case 1:
			WormGame.wormGame();
			break;
		case 2:
			Tetris.tetrisGame();
			break;
		case 3:
			BattleShip.battleShip();
			break;
		case 4:
			Display.exit();
		}
	}
}
