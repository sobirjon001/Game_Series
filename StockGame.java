import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class StockGame {
  static List<Integer> stockValues = new ArrayList<>();
  static int capital = 1000, stockNum, stockOwn = 0, stockValue = 100, day, canDo = 0;
  static String name, action = "skip";
  static boolean valid0 = false, valid1 = false, valid2 = false;
  static Scanner in = new Scanner(System.in);

  public static void stockGame() {
    Display.loading();
    Display.welcome("Stock Market");
    intro();
    game();
    end();
  }

  public static void game() {
    for (int i = 1; i < 11; i++) {
      try {
        Display.clear();
        System.out.println(Color.blue + "\n==================" + Color.red + " DAY " + i + Color.blue
            + " ==================\n" + Color.reset);
        stockValue = ((int) (Math.random() * 9 + 1)) * 100;
        System.out.println(name + "! Your banck hase balace of" + Color.red + " $" + capital + Color.reset + "\n");
        System.out.print("Bank: " + Color.yellow);
        for (int j = (capital / 100); j > 0; j-=3) {
          System.out.print("#");
        }
        System.out.println(Color.red + " $" + capital + Color.reset);
        System.out.println("You own " + Color.blue + stockOwn + Color.reset + " stocks today.\n"
            + "Todays stock value is" + Color.red + " $" + stockValue + Color.reset + "\nYou own" + Color.red + " $ "
            + (stockOwn * stockValue) + Color.reset + " in your stocks.");
        Display.line();
        System.out.println(Color.blue + "========= Stock value market fluctuation =========\n" + Color.reset);
        int d = 0;
        stockValues.add(stockValue);
        for (Integer num : stockValues) {
          System.out.print("Day " + (d++) + ": " + Color.yellow);
          for (int k = (num / 100); k > 0; k--) {
            Thread.sleep(100L);
            System.out.print("###");
          }
          Thread.sleep(100L);
          System.out.println(Color.red + "  $" + num + Color.reset);
        }
        Display.line();
        do {
          System.out.print("Please enter" + Color.red + " 's'" + Color.reset + " to sell," + Color.blue + " 'b'"
              + Color.reset + " to buy or" + Color.green + " 'n'" + Color.reset + " to skip: ");
          String ch = in.next();
          valid1 = ch.equals("b") || ch.equals("s") || ch.equals("n");
          if (ch.equals("b")) {
            action = "buy";
          } else if (ch.equals("s")) {
            action = "sell";
          } else if (ch.equals("n")) {
            action = "skip";
          } else if (!valid1) {
            System.out.println(Color.red + "Invalid entry! Please try again." + Color.reset);
          }
        } while (!valid1);
        do {
          switch (action) {
          case "sell":
            System.out.println("\nHow many stock do you want to sell today?\n");
            canDo = stockOwn;
            System.out.print("You can sell " + Color.red + canDo + Color.reset + " stocks for" + Color.red + " $"
                + stockValue + Color.reset + " each.\n" + "Please enter how many stocks to sell from" + Color.red + " 1"
                + Color.reset + " to " + Color.red + canDo + Color.reset + ": ");
            stockNum = in.nextInt();
            break;
          case "buy":
            System.out.println("\nHow many stock do you want to buy today?\n");
            canDo = capital / stockValue;
            System.out.print("You can buy " + Color.red + canDo + Color.reset + " stocks for" + Color.red + " $"
                + stockValue + Color.reset + " each.\n" + "Please enter how many stocks to buy from" + Color.red + " 1"
                + Color.reset + " to " + Color.red + canDo + Color.reset + ": ");
            stockNum = in.nextInt();
            break;
          default:
            System.out.println(Color.green + "\nOk! Lets wait for better offer.\n" + Color.reset);
            stockNum = 0;
          }

          System.out.print(Color.yellow);
          for (int t = 0; t < 25; t++) {
            System.out.print("==");
            Thread.sleep(100L);
          }
          System.out.print(Color.reset);

          if (stockNum <= canDo) {
            valid2 = true;
            switch (action) {
            case "sell":
              stockOwn -= stockNum;
              capital += (stockNum * stockValue);
              System.out.println(Color.green + "\nSuccessful sale!\n" + Color.blue + stockNum + Color.reset
                  + " stocks sold for" + Color.red + " $" + stockValue + Color.reset + ".\n"
                  + "Your new banck balace is now" + Color.red + " $" + capital + Color.reset + "\nYou now own "
                  + Color.blue + stockOwn + Color.reset + " stocks today.");
              break;
            case "buy":
              stockOwn += stockNum;
              capital -= (stockNum * stockValue);
              System.out.println(Color.green + "\nSuccessful purchase!\n" + Color.blue + stockNum + Color.reset
                  + " stocks purchased for" + Color.red + " $" + stockValue + Color.reset + ".\n"
                  + "Your new banck balace is now" + Color.red + " $" + capital + Color.reset + "\nYou now own "
                  + Color.blue + stockOwn + Color.reset + " stocks today.");
              break;
            default:
              System.out.println(Color.green + "\nYou skipped sale for today!\n" + Color.reset
                  + "Your banck balace remains" + Color.red + " $" + capital + Color.reset + "\nYou still own "
                  + Color.blue + stockOwn + Color.reset + " stocks today.");
            }
          } else {
            valid2 = false;
            System.out.println(Color.red + "\nInvalid number of stocks entered!\n" + "Please try again!" + Color.reset);
          }
          Thread.sleep(5000L);
        } while (!valid2);
      } catch (InterruptedException e) {
        System.out.println("Error");
      }
    }

  }

  public static void intro() {
    Display.line();
    System.out.print("Please enter your name: ");
    name = in.next();
    Display.clear();
    Display.line();
    System.out.print("Welcome " + name + "! Your capital is" + Color.red + " $" + capital + Color.reset
        + ". You can now buy stocks.");

    do {
      System.out.print("\n\nCurrent stock value is" + Color.red + " $100" + Color.reset + " each."
          + "\nHow many stocks do you want to buy?" + "\nPlease enter from" + Color.red + " 1" + Color.reset + " to "
          + Color.red + (capital / stockValue) + Color.reset + ": ");
      stockValues.add(100);
      stockNum = in.nextInt();
      if (stockNum <= 10) {
        capital -= (stockValue * stockNum);
        stockOwn += stockNum;
        valid0 = true;
      } else {
        System.out.println(Color.red + "Invalid numbr entered!\nPlease try again." + Color.reset);
      }
    } while (!valid0);
  }

  public static void end() {
    Display.clear();
    Display.line();
    if (capital > 1000) {
      System.out.println(Color.green + "Congradulations " + Color.red + name + Color.reset + "!\n" + Color.blue
          + "You've increased your capital and won this game!");
    } else if (capital == 1000) {
      System.out.println(Color.green + "Hello " + Color.red + name + Color.reset + "!\n" + Color.blue
          + "Your capital remains the same but you did not win this game!");
    } else {
      System.out.println(Color.green + "Unfortunate " + Color.red + name + Color.reset + "!\n" + Color.blue
          + "You lost your capital and loose this game!");
    }
    try{
      Thread.sleep(3000L);
    } catch (InterruptedException e) {
      System.out.println("Error");
    }
    Main.begin();
  }

}