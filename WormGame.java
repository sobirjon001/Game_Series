
//import java.io.*;
import java.util.Scanner;
//import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class WormGame extends Thread {
	static boolean isGameNotOver = true, reverce = false, notValid1 = false, isDotEaten = true;
	static List<String> worm = new ArrayList<String>() {
		{
			add("10-23");
			add("10-24");
			add("10-25");
			add("10-26");
			add("10-27");
		}
	};
	static String wormBody = Color.green + "0" + Color.reset, dir = "d", indir = "", dot = Color.red + "#" + Color.reset,
			dotPos = "10-22";
	static Scanner in = new Scanner(System.in);
	static List<Integer> pos = new ArrayList<>(2);
	static List<Integer> dpos = new ArrayList<Integer>(2) {
		{
			add(10);
			add(22);
		}
	};
	static String[] temp = new String[2];
	static int lives = 3;

	public static void wormGame() {
		Display.loading();
		Display.welcome(" W  O  R  M ");
		intro();
		game();
		Main.begin();
	}

	public static void intro() {
		try {
			Display.clear();
			Display.line();
			System.out.println("To play game you have to press next keys:\n" + Color.red + "w" + Color.reset
					+ " for going up, " + Color.green + "s" + Color.reset + " to do down, " + Color.blue + "a" + Color.reset
					+ " to turn left,\n" + Color.yellow + "d" + Color.reset + " to turn right, " + Color.red + "r" + Color.reset
					+ " to reverse, " + Color.green + "e" + Color.reset + " to exit game,\nand press Enter.");
			Display.line();
			System.out.println("\n\n     " + Color.yellow);
			for (int i = 0; i < 6; i++) {
				System.out.print("======");
				Thread.sleep(300L);
			}
			System.out.println(Color.reset);
		} catch (InterruptedException e) {
			System.out.println("Error");
		}
	}

	public static void game() {
		WormGame t1 = new WormGame();
		t1.start();// starting disply on another thread
		// worm movement modifier
		do {
			indir = in.next();

			if (indir.equals("a") || indir.equals("d") || indir.equals("w") || indir.equals("s") || indir.equals("r")
					|| indir.equals("e")) {
				if (indir.equals("e")) {
					isGameNotOver = false;
				}
				if (indir.equals("r") || indir.equals("a") && dir.equals("d") || indir.equals("d") && dir.equals("a")
						|| indir.equals("w") && dir.equals("s") || indir.equals("s") && dir.equals("w")) {
					reverce = true;
				}
				if (!indir.isEmpty() || !indir.equals("e") || !indir.equals("r")) {
					dir = indir;
				}
			}

			if (reverce) {// here all worm positions will be rewritten backwards
				List<String> wormCopy = new ArrayList<>(worm);
				worm.clear();
				for (int i = wormCopy.size() - 1; i >= 0; i--) {
					worm.add(wormCopy.get(i));
				}
				reverce = false;
			}
		} while (isGameNotOver);
	}

	public void run() {
		try {
			do {
				if(isDotEaten){ //creade the dot
					do{ 
						  dpos.set(0,(int)(Math.random()*20));
				      dpos.set(1,(int)(Math.random()*50));
							dotPos = (dpos.get(0).toString()+"-"+dpos.get(1).toString());
							}while(worm.contains(dotPos));
						isDotEaten=false;
				}
				pos.clear(); Arrays.fill(temp, null);// clear previous data
				temp = worm.get(worm.size() - 1).split("-");// getting from end of list
				for (int i = 0; i < 2; i++) {
					pos.add(Integer.parseInt(temp[i]));
				}
				switch (dir) {// updating worm positio
				case "d":
					pos.set(1, pos.get(1) + 1);
					worm.add(pos.get(0).toString() + "-" + pos.get(1).toString());
					break;
				case "a":
					pos.set(1, pos.get(1) - 1);
					worm.add(pos.get(0).toString() + "-" + pos.get(1).toString());
					break;
				case "w":
					pos.set(0, pos.get(0) - 1);
					worm.add(pos.get(0).toString() + "-" + pos.get(1).toString());
					break;
				case "s":
					pos.set(0, pos.get(0) + 1);
					worm.add(pos.get(0).toString() + "-" + pos.get(1).toString());
					break;
				}
				if (dpos.equals(pos)) { // eating the dot
					isDotEaten = true;
				} else {
					worm.remove(0);
				}

				// print screen using Thread t1
				Display.clear();

				System.out.println("To play game you have to press next keys:\n" + Color.red + "w" + Color.reset
						+ " for going up, " + Color.green + "s" + Color.reset + " to do down, " + Color.blue + "a" + Color.reset
						+ " to turn left,\n" + Color.yellow + "d" + Color.reset + " to turn right, " + Color.red + "r" + Color.reset
						+ " to reverse, " + Color.green + "e" + Color.reset + " to exit game,\nand press Enter.");

				System.out.println(" __________________________________________________");
				for (int y = 0; y < 20; y++) {
					System.out.print("|");
					for (int x = 0; x < 50; x++) {
						System.out.print(worm.contains("" + y + "-" + x) ? wormBody : dotPos.equals("" + y + "-" + x) ? dot : " ");
					}
					System.out.print("|\n");
				}
				System.out.println(" --------------------------------------------------");
				Thread.sleep(800L);
			} while (isGameNotOver);
		} catch (InterruptedException e) {
			System.out.println("Error");
		}
	}
}
