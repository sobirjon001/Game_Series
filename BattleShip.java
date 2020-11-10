import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BattleShip extends Thread {
	// Declaring Global variables:
	static Ship[] shipList = new Ship[12];
	static List<String> names = new ArrayList<String>() {
		{
			add("br");
			add("bl");
			add("mr");
			add("ml");
			add("sr");
			add("sl");
		}
	};

	static boolean isGameNotOver = true;// game over switch
	// **** location positions for display********
	static List<String> myShips = new ArrayList<>();// my ships positions
	static List<String> cShips = new ArrayList<>();// computer ships visible positions
	static List<String> cShipsNotVis = new ArrayList<>();// computer ships not visible positions for calculations
	static List<String> hitStr = new ArrayList<>();// damaged spot
	static List<String> lastKnownStr = new ArrayList<>();// last known comp ship position for me
	static List<String> cLastKnownStr = new ArrayList<>();// last known my ship position for computer
	static List<String> explosionStr = new ArrayList<>();// explosion position
	static List<String> visRadStr = new ArrayList<>();// my ship visibility radius
	static List<String> cVisRadStr = new ArrayList<>();// comp ship visibility radius
	static List<String> shipRack = new ArrayList<>();// ship rack position
	static List<String> splStr = new ArrayList<>();// Splash in watter
	// ********************************************

	static String myS = Color.green + "[]" + Color.reset;
	static String cS = Color.red + "[]" + Color.reset;
	static String hit = "[]";
	static String exp = Color.yellow + "%%" + Color.reset;
	static String userInput;
	static Scanner in = new Scanner(System.in);
	static String[] userInArr = new String[3];
	static List<Integer> fire = new ArrayList<>(2);
	static List<Integer> cFire = new ArrayList<>(2);
	static List<Integer> visMyShipIndex = new ArrayList<>();
	static List<Integer> possibleFire = new ArrayList<>();
	static String action, cDirection;
	static int shipIndex, posFireIndex, myDeadCount = 0, CompDeadCount = 0;

	public static void battleShip() {// replace it to main if outside of mainMenue
		Display.loading();
		Display.welcome(" Battle Ship");
		game();
		Main.begin();
	}

	public static void game() {

		// ******* Initialisation ***********
		createShips();
		analyze();// analyze visibility etc
		toStingList();// create Sting List Positions for ships
		display(); // update display

		// ********* Start the game flow **************
		do {
			userInput();// get user commands
			move();// update ship movements
			analyze();// analyze visibility etc
			toStingList();// create Sting List Positions after analyze change
			display(); // update display
			computerActions();// get computer commands
			if (myDeadCount == 6 || CompDeadCount == 6) {
				isGameNotOver = false;
			}
		} while (isGameNotOver);
	}

	public static void userInput() {
		// fire is cleared in toStingList() after use
		userInput = in.nextLine(); // get user command
		userInArr = userInput.split(" "); // spit parameters to analise
		if (userInArr.length == 3 || userInArr[0].equals("e")) {
			switch (userInArr[0]) {
			case "m":
				if (names.contains(userInArr[1])) {// so user can't control computer ships -- no cheating!
					if (userInArr[2].equals("w") || userInArr[2].equals("s") || userInArr[2].equals("a")
							|| userInArr[2].equals("d") || userInArr[2].equals("n")) {
						for (int i = 0; i < 12; i++) {
							if (shipList[i].name.equals(userInArr[1])) {
								shipList[i].direction = userInArr[2];
							}
						}
					}
				}
				break;
			case "f":
				// if (userInArr[0].matches("^[0-9]*2") && userInArr[1].matches("^[0-9]*2")) {
				fire.add(Integer.parseInt(userInArr[1]));
				fire.add(Integer.parseInt(userInArr[2]));
				// }
				break;
			case "e":
				isGameNotOver = false;
			}
		}
	}

	public static void move() { // update movement
		int s;// how many steps to move depends on ship speed
		for (Ship ship : shipList) {
			if (!ship.isDed) {// dead ships not movable
				s = ship.speed;
				switch (ship.direction) {
				case "w": // going up
					for (int j = 0; j < ship.posInt.size(); j += 2) {
						if (ship.posInt.get(j) > 1) {
							ship.posInt.set(j, ship.posInt.get(j) - s);
							if (j == 0) {
								ship.visRad.set(j, ship.visRad.get(j) - s);
							}
							if (j == ship.posInt.size() - 2) {
								ship.visRad.set(2, ship.visRad.get(2) - s);
							}
						} else {
							ship.direction = "n";
							break;
						} // stop if top reached
					}
					break;
				case "s": // going down
					for (int j = 0; j < ship.posInt.size(); j += 2) {
						if (ship.posInt.get(ship.posInt.size() - 2) < 29) {
							ship.posInt.set(j, ship.posInt.get(j) + s);
							if (j == 0) {
								ship.visRad.set(j, ship.visRad.get(j) + s);
							}
							if (j == ship.posInt.size() - 2) {
								ship.visRad.set(2, ship.visRad.get(2) + s);
							}
						} else {
							ship.direction = "n";
							break;
						} // stop if bottom reached
					}
					break;
				case "a": // going left
					for (int j = 1; j < ship.posInt.size(); j += 2) {
						if (ship.posInt.get(j) > 1) {
							ship.posInt.set(j, ship.posInt.get(j) - s);
							if (j == 1) {
								ship.visRad.set(j, ship.visRad.get(j) - s);
							}
							if (j == ship.posInt.size() - 1) {
								ship.visRad.set(3, ship.visRad.get(3) - s);
							}
						} else {
							ship.direction = "n";
							break;
						} // stop if left reached
					}
					break;
				case "d": // going right
					for (int j = 1; j < ship.posInt.size(); j += 2) {
						if (ship.posInt.get(j) < 29) {
							ship.posInt.set(j, ship.posInt.get(j) + s);
							if (j == 1) {
								ship.visRad.set(j, ship.visRad.get(j) + s);
							}
							if (j == ship.posInt.size() - 1) {
								ship.visRad.set(3, ship.visRad.get(3) + s);
							}
						} else {
							ship.direction = "n";
							break;
						} // stop if right reached
					}
					break;
				} // ent of switch
			}
		} // end of movement for loop
	}

	public static void display() {
		// instructions();
		Display.clear(); // clear display fom previous picture

		System.out
				.println(Color.lightRed + "  00________05________10________15________20________25________30" + Color.reset);
		for (int y = 0; y <= 30; y++) {
			if (y % 5 == 0) {
				System.out.print((y < 10 ? Color.lightBlue + "0" : Color.lightBlue + "") + y + Color.reset);
			} else {
				System.out.print(" |");
			}
			for (int x = 0; x <= 30; x++) {
				System.out.print(explosionStr.contains("" + y + "-" + x) ? exp
						: hitStr.contains("" + y + "-" + x) ? hit
								: myShips.contains("" + y + "-" + x) ? myS
										: cShips.contains("" + y + "-" + x) ? cS
												: splStr.contains("" + y + "-" + x) ? "()"
														: shipRack.contains("" + y + "-" + x) ? Color.grey + "++" + Color.reset
																: lastKnownStr.contains("" + y + "-" + x) ? "::"
																		: (y % 5 == 0 && x % 5 == 0 && x > 3 && x < 25 && y > 3 && y < 27 && x != 5
																				&& x != 15)
																						? Color.lightRed + x + Color.reset
																						: (y % 5 == 0 && x % 5 == 0 && x > 3 && x < 27 && y > 3 && y < 30 && x != 10
																								&& x != 20)
																										? Color.lightBlue + (y == 5 ? "0" + y : y) + Color.reset
																										: y % 5 == 0 && y > 3 && y < 27 ? Color.grey + "--" + Color.reset
																												: x % 5 == 0 && x > 3 && x < 27
																														? Color.grey + "| " + Color.reset
																														: !visRadStr.contains("" + y + "-" + x)
																																? Color.grey + "::" + Color.reset
																																: "  ");
			}
			if (y % 5 == 0) {
				System.out.println((y < 10 ? Color.lightBlue + "0" : Color.lightBlue + "") + y + Color.reset);
			} else {
				System.out.println("|  " + instructions(y));
			}
		}
		System.out
				.println(Color.lightRed + "  00--------05--------10--------15--------20--------25--------30" + Color.reset);
		// System.out.println(x);
	}

	public static String instructions(int index) {
		StringBuilder x = new StringBuilder();
		switch (index) {
		case 1:
			for (int i = 6; i < 9; i++) {
				if (!shipList[i].isDed) {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int j = 0; j < shipList[i].length; j++) {
						x.append(shipList[i].hitPoint.contains(j) ? "[]" : Color.red + "[]" + Color.reset);
					}
				} else {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int k = 0; k < shipList[i].length; k++) {
						x.append("#");
					}
				}
			}
			break;
		case 3:
			for (int i = 11; i > 8; i--) {
				if (!shipList[i].isDed) {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int j = 0; j < shipList[i].length; j++) {
						x.append(shipList[i].hitPoint.contains(j) ? "[]" : Color.red + "[]" + Color.reset);
					}
				} else {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int k = 0; k < shipList[i].length; k++) {
						x.append("#");
					}
				}
			}
			break;
		case 6:
			for (int i = 0; i < 3; i++) {
				if (!shipList[i].isDed) {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int j = 0; j < shipList[i].length; j++) {
						x.append(shipList[i].hitPoint.contains(j) ? "[]" : Color.green + "[]" + Color.reset);
					}
				} else {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int k = 0; k < shipList[i].length; k++) {
						x.append("#");
					}
				}
			}
			break;
		case 8:
			for (int i = 5; i > 2; i--) {
				if (!shipList[i].isDed) {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int j = 0; j < shipList[i].length; j++) {
						x.append(shipList[i].hitPoint.contains(j) ? "[]" : Color.green + "[]" + Color.reset);
					}
				} else {
					x.append(" |  ").append(shipList[i].name).append("  ");
					for (int k = 0; k < shipList[i].length; k++) {
						x.append("#");
					}
				}
			}
			break;
		case 11:
			x.append("You have to enter 3 entries separated by space:");
			break;
		case 12:
			x.append(Color.red + "m" + Color.reset + " to move, and ship name, and direction letters:");
			break;
		case 13:
			x.append(Color.red + "w" + Color.reset + " -- up," + Color.red + "s" + Color.reset + " -- down, " + Color.red
					+ "a" + Color.reset + " -- left, " + Color.red + "d" + Color.reset + " -- right, " + Color.red + "n"
					+ Color.reset + " -- stop");
			break;
		case 14:
			x.append("Example :" + Color.red + "m sr w" + Color.reset + " -- sr will be going up");
			break;
		case 16:
			x.append("To fire You have to enter 3 entries separated by space:");
			break;
		case 17:
			x.append(Color.red + "f" + Color.reset + " to fire and y and x coordinates:");
			break;
		case 18:
			x.append("Example :" + Color.red + "f 10 15" + Color.reset + " will fire at y -- 10 and x -- 15.");
			break;
		case 19:
			x.append("Enter " + Color.red + "e" + Color.reset + " to exit Game.");
			break;
		case 21:
			x.append("* * * * * * *   Map Legend   * * * * * * *");
			break;
		case 22:
			x.append(Color.red + "[]" + Color.reset + " -- enemy Ships, " + Color.green + "[]" + Color.reset
					+ " -- your Ships, [] -- damaged block,");
			break;
		case 23:
			x.append(Color.yellow + "%%" + Color.reset + " -- explosion, () -- missed, " + Color.grey + "++" + Color.reset
					+ " -- ship rack,");
			break;
		case 24:
			x.append(":: -- last known position, " + Color.grey + "::" + Color.reset + " -- unknown area.");
			break;
		}// end of switch
		return x.toString();
	}

	public static void toStingList() {
		myShips.clear();
		cShips.clear();
		cShipsNotVis.clear();
		hitStr.clear();
		explosionStr.clear();
		splStr.clear();
		for (int i = 0; i < 6; i++) { // for user ships
			if (shipList[i].isDed) {
				continue;
			} // if is dead skip
			for (int j = 0; j < shipList[i].posInt.size(); j += 2) { // for every body point of ship length
				if (shipList[i].length == shipList[i].hitPoint.size()) {// if all body is hit -> dead
					shipRack.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create ship rack
					shipList[i].isDed = true;// mark ship as dead
					myDeadCount++;
				} else if (!shipList[i].hitPoint.contains(j / 2)) { // if no damage before
					if (!cFire.isEmpty()) {// if fire in the hall
						if (shipList[i].posInt.get(j).equals(cFire.get(0)) && shipList[i].posInt.get(j + 1).equals(cFire.get(1))) { // if
																																																												// hit:
							explosionStr.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// display
																																																			// explosion
							shipList[i].hitPoint.add(j / 2);// add damage to ship
						} else {// if not hit:
							myShips.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create normal ship
																																																// body
						}
					} else {// if no fire
						myShips.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create normal ship body
					}
				} else { // if damage before
					hitStr.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// print damaged ship body
				}
			}
		} // end of for loop of my ships

		for (int i = 6; i < 12; i++) { // for computer ships
			if (!shipList[i].isDed) {// if is dead skip
				for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// for every body point of ship length
					if (shipList[i].length == shipList[i].hitPoint.size()) {// if all body is hit -> dead
						shipRack.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create ship rack
						shipList[i].isDed = true;// mark ship as dead
						CompDeadCount++;
					} else if (shipList[i].isVisible) {// if computer ship is visible do below for display
						if (!shipList[i].hitPoint.contains(j / 2)) {// if no damage before
							if (!fire.isEmpty()) {// if fire in the hall
								if (shipList[i].posInt.get(j).equals(fire.get(0))
										&& shipList[i].posInt.get(j + 1).equals(fire.get(1))) {// if hit:
									explosionStr.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// display
																																																					// explosion
									shipList[i].hitPoint.add(j / 2);// add damage to ship
								} else {// if not hit:
									cShips.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create normal ship
																																																		// body
								}
							} else {// if no fire
								cShips.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create normal ship
																																																	// body
							}
						} else {// if damage before
							hitStr.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));
						}
					} else {// if computer ship is not visible do below for behind display for calculation
						cShipsNotVis.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// create ship body
																																																		// for calculation
						if (!fire.isEmpty()) {// if fire in the hall
							if (shipList[i].posInt.get(j).equals(fire.get(0)) && shipList[i].posInt.get(j + 1).equals(fire.get(1))) {// if
																																																												// hit:
								explosionStr.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// display
																																																				// explosion
								shipList[i].hitPoint.add(j / 2);// add damage to ship
							}
						}
					}
				}
			}
		} // end of for loop of computer ships
		if (!cFire.isEmpty() && !myShips.contains("" + cFire.get(0) + "-" + cFire.get(1))) {
			splStr.add("" + cFire.get(0) + "-" + cFire.get(1));// create splash if miss
		}
		if (!fire.isEmpty() && !cShips.contains("" + fire.get(0) + "-" + fire.get(1))) {
			splStr.add("" + fire.get(0) + "-" + fire.get(1));// create splash if miss
		}
		fire.clear();
		cFire.clear();
	}

	public static void analyze() {
		// create visibility array positions
		visRadStr.clear();
		cVisRadStr.clear();
		for (int i = 0; i < 12; i++) {
			if (shipList[i].isDed) {
				continue;
			}
			// create visibility radius for my ships
			for (int y = shipList[i].posInt.get(0) - 3; y <= shipList[i].posInt.get(shipList[i].posInt.size() - 2) + 3; y++) {// lowest
																																																												// y
																																																												// -3
																																																												// to
																																																												// highest
																																																												// y
																																																												// +3
				for (int x = shipList[i].posInt.get(1) - 3; x <= shipList[i].posInt.get(shipList[i].posInt.size() - 1)
						+ 3; x++) {// lowest x -3 to highest x +3
					if (y >= 0 && y <= 29 && x >= 0 && x <= 29) { // exclude range outside of map
						if (i < 6) {
							visRadStr.add("" + y + "-" + x);// create visibility radius fo my ships
						} else {
							cVisRadStr.add("" + y + "-" + x);// create visibility radius fo computer ships
						}
					}
				}
			}
		}

		// check if ships are visible:
		List<String> tempLastKnown = new ArrayList<>();
		for (int i = 6; i < 12; i++) {// for computer ships
			if (!shipList[i].isDed) {
				if (!shipList[i].isVisible) {// if was not visible before
					for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// for every body point of ship length
						if (visRadStr.contains("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1))) {// if in
																																																						// visibility
																																																						// range
							shipList[i].isVisible = true;// mark ship as visible
							break;// exit this for loop
						}
					}
				} else {// if ship was visible before
					for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// for every body point of ship length
						if (!visRadStr.contains("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1))) {// if ship
																																																						// part not
																																																						// in
																																																						// visibility
																																																						// range
							tempLastKnown.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// add to temp pos
																																																			// list
						}
					}
				}
				if (tempLastKnown.size() == shipList[i].length) {// if hall ship is outside visibility
					shipList[i].isVisible = false;// mark ship as not visible
					lastKnownStr.addAll(tempLastKnown);// create last Known ship positions
				}
				tempLastKnown.clear();// clear tempLastKnown
			}
		} // end of for loop
		lastKnownStr.removeIf(n -> (visRadStr.contains(n)));// remove last known if in visibility range

		for (int i = 0; i < 6; i++) {// for my ships
			if (!shipList[i].isDed) {
				if (!shipList[i].isVisible) {// if was not visible before
					for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// for every body point of ship length
						if (cVisRadStr.contains("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1))) {// if in
																																																						// visibility
																																																						// range
							shipList[i].isVisible = true;// mark ship as visible
							break;// exit this for loop
						}
					}
				} else {// if ship was visible before
					for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// for every body point of ship length
						if (!cVisRadStr.contains("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1))) {// if ship
																																																							// part
																																																							// not in
																																																							// visibility
																																																							// range
							tempLastKnown.add("" + shipList[i].posInt.get(j) + "-" + shipList[i].posInt.get(j + 1));// add to temp pos
																																																			// list
						}
					}
				}
				if (tempLastKnown.size() == shipList[i].length) {// if hall ship is outside visibility
					shipList[i].isVisible = false;// mark ship as not visible
					cLastKnownStr.addAll(tempLastKnown);// create last Known ship position
				}
				tempLastKnown.clear();
			}
		} // end of for loop
		cLastKnownStr.removeIf(n -> (cVisRadStr.contains(n)));// remove last known if in visibility range
	}

	public static void computerActions() {
		List<String> directionFeed = new ArrayList<String>() {
			{
				add("w");
				add("s");
				add("a");
				add("d");
				add("n");
			}
		};

		visMyShipIndex.clear();
		for (int i = 0; i < 6; i++) {// see if my ship is visible
			if (shipList[i].isVisible && !shipList[i].isDed) {
				visMyShipIndex.add(i);
			}
		}
		possibleFire.clear();
		if (!visMyShipIndex.isEmpty() || !cLastKnownStr.isEmpty()) {// if my ship is visible or if my ship last known
			if (!visMyShipIndex.isEmpty()) {// if my ship is visible
				for (int i : visMyShipIndex) {
					for (int j = 0; j < shipList[i].posInt.size(); j += 2) {// create hit positions
						possibleFire.add(shipList[i].posInt.get(j));// y position
						possibleFire.add(shipList[i].posInt.get(j + 1));// x position
						if (j <= 1) {// create 2 miss positions
							possibleFire.add(shipList[i].posInt.get(j));
							possibleFire.add(shipList[i].posInt.get(j + 1) + 1);
						}
					}
				}
			}

			if (!cLastKnownStr.isEmpty()) {// if my ship last known position
				for (String n : cLastKnownStr) {
					String[] temp = n.split("-");
					possibleFire.add(Integer.parseInt(temp[0]));// y position
					possibleFire.add(Integer.parseInt(temp[1]));// x position
				}
			}
			int x = (int) (Math.random() * 10);
			action = x > 7 ? "m" : "f";
			switch (action) {
			case "m":
				shipIndex = (int) (Math.random() * 6 + 6);// chose computer ship index
				cDirection = directionFeed.get((int) (Math.random() * directionFeed.size()));// chose direction
				shipList[shipIndex].direction = cDirection;// assign movement
				break;
			case "f":
				// cFire is cleared in toStingList() after use
				posFireIndex = (int) (Math.random() * (possibleFire.size() / 2));
				if (posFireIndex % 2 != 0) {
					posFireIndex++;
				}
				cFire.add(possibleFire.get(posFireIndex));// y position
				cFire.add(possibleFire.get(posFireIndex + 1));// x position
				cLastKnownStr.removeIf(n -> (n.contains("" + cFire.get(0) + "-" + cFire.get(1))));
				break;
			}
		} else {// if my ship not visible and not last known
			shipIndex = (int) (Math.random() * 6 + 6);// chose computer ship index
			cDirection = directionFeed.get((int) (Math.random() * directionFeed.size()));// chose direction
			shipList[shipIndex].direction = cDirection;// assign movement
		}
	}

	public static void createShips() {
		// user ships
		shipList[0] = new Ship("sl", "n", 2, 1, false, false);
		shipList[1] = new Ship("ml", "n", 1, 3, false, false);
		shipList[2] = new Ship("bl", "n", 1, 4, false, false);
		shipList[3] = new Ship("br", "n", 1, 4, false, false);
		shipList[4] = new Ship("mr", "n", 1, 3, false, false);
		shipList[4] = new Ship("mr", "n", 1, 3, false, false);
		shipList[5] = new Ship("sr", "n", 2, 1, false, false);
		// computer ships
		shipList[6] = new Ship("csl", "n", 2, 1, false, false);
		shipList[7] = new Ship("cml", "n", 1, 3, false, false);
		shipList[8] = new Ship("cbl", "n", 1, 4, false, false);
		shipList[9] = new Ship("cbr", "n", 1, 4, false, false);
		shipList[10] = new Ship("cm", "n", 1, 3, false, false);
		shipList[11] = new Ship("csr", "n", 2, 1, false, false);
		// creating ship bodies and position ships on map and visibility
		int x = 3;
		for (int i = 0; i < 6; i++) {// for user ships
			for (int y = 0; y < shipList[i].length; y++) {
				// bodies
				shipList[i].posInt.add(y + 26);
				shipList[i].posInt.add(x);
				// visibility
				if (y == 0) {
					shipList[i].visRad.add(y + 23);
					shipList[i].visRad.add(x - 3);
				}
				if (y == shipList[i].length - 1) {
					shipList[i].visRad.add(y + 28);
					shipList[i].visRad.add(x + 3);
				}
			}
			x += 5;
		}

		x = 3;
		for (int i = 6; i < 12; i++) {// for computer ships
			for (int y = 0; y < shipList[i].length; y++) {
				// bodies
				shipList[i].posInt.add(y + 1);
				shipList[i].posInt.add(x);
				// visibility
				if (y == 0) {
					shipList[i].visRad.add(y - 3);
					shipList[i].visRad.add(x - 3);
				}
				if (y == shipList[i].length - 1) {
					shipList[i].visRad.add(y + 4);
					shipList[i].visRad.add(x + 3);
				}
			}
			x += 5;
		}
	}

}

class Ship {// Ship object
	String name, direction;
	List<Integer> posInt;
	List<Integer> hitPoint;
	List<Integer> lastKnInt;
	List<Integer> visRad;
	int speed, length;
	boolean isVisible, isDed;

	// constructor
	Ship(String n, String d, int s, int l, boolean v, boolean ded) {
		name = n;
		direction = d;
		speed = s;
		length = l;
		isVisible = v;
		isDed = ded;
		posInt = new ArrayList<>();
		hitPoint = new ArrayList<>();
		lastKnInt = new ArrayList<>();
		visRad = new ArrayList<>();
	}
}