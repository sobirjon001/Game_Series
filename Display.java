public class Display{

  public static void start() {
    try {
      clear();
    
      System.out.println(" ___________________________________________________");
      System.out.println("|                                                   |");
      System.out.println("|                                                   |");
      System.out.println("|                      "+Color.green+"Welcome to"+Color.reset+"                   |");
      System.out.println("|             "+Color.red+"Sobir's Console Series Game!"+Color.reset+"          |");
      System.out.println("|                 "+Color.yellow+"My first Java game!"+Color.reset+"               |");
      System.out.println("|                                                   |");
      System.out.println("|___________________________________________________|");

      Thread.sleep(3000L);
      clear();
    } catch (InterruptedException e) {
      System.out.println("Error");
    }
  }

  public static void welcome(String gameName) {
    try {
      clear();
    
      System.out.println(" ___________________________________________________");
      System.out.println("|                                                   |");
      System.out.println("|                                                   |");
      System.out.println("|                      "+Color.green+"Welcome to"+Color.reset+"                   |");
      System.out.println("|              "+Color.red+"Sobir's "+gameName+" Game!"+Color.reset+"           |");
      System.out.println("|                 "+Color.yellow+"My first Java game!"+Color.reset+"               |");
      System.out.println("|                                                   |");
      System.out.println("|___________________________________________________|");

      Thread.sleep(3000L);
      clear();
    } catch (InterruptedException e) {
      System.out.println("Error");
    }
  }

  public static void loading(){
    for(int i=1;i<5;i++){
      try{
      clear();
      System.out.println(" ___________________________________________________");
      System.out.println("|                                                   |");
      System.out.println("|                                                   |");
      System.out.println("|                 "+Color.red+"L O A D I N G . . ."+Color.reset+"               |");
      System.out.print("|           "+Color.green);
      for(int k=0;k<i;k++){
        System.out.print("=======");
      }
      System.out.print(Color.reset);
      for(int j=4;j>i;j--){
          System.out.print("       ");
      }
      System.out.println("            |");
      System.out.println("|                                                   |");
      System.out.println("|                                                   |");
      System.out.println("|___________________________________________________|");

    Thread.sleep(1000L);
    } catch (InterruptedException e) {
      System.out.println("Error");
    }
    }
  }

  public static void exit(){
    clear();
    System.out.println(" ___________________________________________________");
    System.out.println("|                                                   |");
    System.out.println("|                                                   |");
    System.out.println("|              "+Color.green+"Thank you for playing!"+Color.reset+"               |");
    System.out.println("|           "+Color.red+"Sobir's Console Series Game!"+Color.reset+"            |");
    System.out.println("|                 "+Color.yellow+"My first Java game!"+Color.reset+"               |");
    System.out.println("|                                                   |");
    System.out.println("|___________________________________________________|");
  }

  public static void clear() {
    System.out.print("\033[H\033[2J");
  }

  public static void line() {
    System.out.println("\n===================================================\n");
  }
}