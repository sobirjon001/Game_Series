import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Tetris extends Thread{

	static boolean isGameNotOver=true,newIn=false,isHitBase=false,needNewShape=false;
	static int shapeIndex,shapeRotation=0,yy=1,xx=5;
	static List<Integer> shapeInt = new ArrayList<>();
	static List<Integer> baseInt = new ArrayList<>();
	static List<String> shapeStr = new ArrayList<>();
	static List<String> baseStr = new ArrayList<>();
	static List<String> toDelete = new ArrayList<>();
	static Scanner in = new Scanner(System.in);
	static String userIn,body=Color.red+"[]"+Color.reset,base=Color.green+"%%"+Color.reset,toDel=Color.yellow+"()"+Color.reset;



	public static void tetrisGame(){
		//Display.loading();
		//Display.welcome(" T E T R I S");
		//intro();
		game();
		Main.begin();
	}

	public static void intro(){
		try {
			Display.clear();
			Display.line();
			System.out.println("To play game you have to press next keys:\n"+Color.green + "s" + Color.reset + " to do down, " + Color.blue + "a" + Color.reset
					+ " to shift left,\n" + Color.yellow + "d" + Color.reset + " to shift right, " + Color.red + "r" + Color.reset + " to rotate, " + Color.green + "e" + Color.reset + " to exit game,\nand press Enter.");
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

	public static void game(){
		isGameNotOver=true;
		createShape(1,5,true); // create start shape ready
		baseStr.clear();
		for(int i=0;i<10;i++){ //create base Int
			baseInt.add(19);baseInt.add(i);
		}
		for(int i=0;i<10;i++){ //create base Str
			baseStr.add("19-"+i);
		}

		Tetris t1 = new Tetris(); //create new Thread
		t1.start(); // new Thread will run run()
			// *** user controll ****
		do{ //user input indefinet loop on Thread.0
			userIn=in.next();
			newIn=true;
			
			switch(userIn){
				case "a": //shift left
					if(xx>0){
						for(int i=1;i<shapeInt.size();i+=2){
							shapeInt.set(i, shapeInt.get(i)-1);
						}
						xx--;
					}
					break;
				case "d": //shift right
					if(xx<9){
						for(int i=1;i<shapeInt.size();i+=2){
							shapeInt.set(i, shapeInt.get(i)+1);
						}
						xx++;
					}
					break;
				case "s": //put down
						//
					break;
				case "r": //rotate shape
						if(shapeRotation==3){
							shapeRotation=0;
						}else{
							shapeRotation++;
						}
						createShape(yy,xx,needNewShape);
					break;
				case "e": //exit game
					isGameNotOver=false;// kills infinit loop
					break;
			}

			shapeStr.clear();
			for(int i=0;i<shapeInt.size();i+=2){// update shapeStr
				shapeStr.add(shapeInt.get(i).toString()+"-"+shapeInt.get(i+1).toString());
			}

			t1.suspend();
			display();
			t1.resume();
		}while(isGameNotOver);
	}

	

	public void run(){
		try{
			do{ // print screen and update changes
				Thread.sleep(800L);
				for(int i=0;i<shapeInt.size();i+=2){
					shapeInt.set(i, shapeInt.get(i)+1); //moving down
				}
				shapeStr.clear();
				for(int i=0;i<shapeInt.size();i+=2){// update shapeStr
					shapeStr.add(shapeInt.get(i).toString()+"-"+shapeInt.get(i+1).toString());
				}
				for(String check:shapeStr){// check if we hit base
					isHitBase=baseStr.contains(check);
					if(isHitBase){break;}
				}
				if(isHitBase){ //check if we hit base
					for(int i=0;i<shapeInt.size();i+=2){
					shapeInt.set(i, shapeInt.get(i)-1); //returning
					}
					shapeStr.clear();
					for(int i=0;i<shapeInt.size();i+=2){ //add shape to base
						baseStr.add(shapeInt.get(i).toString()+"-"+shapeInt.get(i+1).toString());
						baseInt.add(shapeInt.get(i));
						baseInt.add(shapeInt.get(i+1));
					}
					shapeInt.clear();
					yy=1;xx=5;needNewShape=true;
					
					display();
					Thread.sleep(300L);
					checkBase();
				}else{
					yy++;
					display();
				}
				
				
				
			}while(isGameNotOver);
		}catch(InterruptedException e){}
	}

	public static void checkBase(){
		int highest = 19;
		for(int i=0;i<baseInt.size();i+=2){ //find highest y for base
			if(baseInt.get(i)<highest){highest=baseInt.get(i);}
		}
		int containsScore=0;
		List<Integer> containsIndex = new ArrayList<>();
		for(int y=18;y>highest;y--){
			for(int x=0;x<10;x++){
				for(int i=0;i<baseInt.size();i+=2){
					if(baseInt.get(i)==y&&baseInt.get(i+1)==x){
						containsScore++;
					}
				}
			}
			if(containsScore==10){//we found line to delete
				containsScore=0;containsIndex.add(y); 
			}else{ //reset containsScore and check another y line of x
				containsScore=0;
			}
		}

		if(containsIndex.size()>0){
			List<Integer> temp = new ArrayList<Integer>(baseInt);
			baseInt.clear();
			for(int i=0;i<containsIndex.size();i++){
				for(int j=0;j<temp.size();j+=2){
					if(temp.get(j)==containsIndex.get(i)){
						toDelete.add(temp.get(j).toString()+"-"+temp.get(j+1).toString());
					}else if(temp.get(j)<containsIndex.get(i)){
						shapeInt.add(temp.get(j));
						shapeInt.add(temp.get(j+1));
					}else{
						baseInt.add(temp.get(j));
						baseInt.add(temp.get(j+1));
					}
				}
			}
			shapeStr.clear();
			for(int i=0;i<shapeInt.size();i+=2){// update shapeStr
				shapeStr.add(shapeInt.get(i).toString()+"-"+shapeInt.get(i+1).toString());
			}
			baseStr.clear();
			for(int i=0;i<baseInt.size();i+=2){// update baseStr
				baseStr.add(baseInt.get(i).toString()+"-"+baseInt.get(i+1).toString());
			}
			display();
			toDelete.clear();
		}else{
			createShape(yy,xx,needNewShape);
		}
	}

	public static void display(){
		Display.clear();
		System.out.println("To play game you have to press next keys:\n"+Color.green + "s" + Color.reset + " to do down, " + Color.blue + "a" + Color.reset + " to shift left,\n" + Color.yellow + "d" + Color.reset + " to shift right, " + Color.red + "r" + Color.reset + " to rotate, " + Color.green + "e" + Color.reset + " to exit game,\nand press Enter.");
		System.out.println(" ____________________");
		for(int y=0;y<20;y++){
			System.out.print("|");
			for(int x=0;x<10;x++){
				System.out.print(shapeStr.contains(""+y+"-"+x)?body:toDelete.contains(""+y+"-"+x)?toDel:baseStr.contains(""+y+"-"+x)?base:"  ");
			}
			System.out.println("|");
		}
		System.out.println(" --------------------");
	}

	public static void createShape(int y, int x, boolean b){
		shapeInt.clear();
		if(b){
			shapeIndex = (int)(Math.random()*8);
			needNewShape=false;
		}
		switch(shapeIndex){
			case 0: // stick
				if(shapeRotation==0||shapeRotation==2){ //vertical
					shapeInt.add(y-1);shapeInt.add(x);
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y+1);shapeInt.add(x);
					shapeInt.add(y+2);shapeInt.add(x);
				}else{ //horizontal 
					shapeInt.add(y);shapeInt.add(x-1);
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y);shapeInt.add(x+1);
					shapeInt.add(y);shapeInt.add(x+2);
				}
				break;
			case 1: //cube
				shapeInt.add(y-1);shapeInt.add(x);
				shapeInt.add(y);shapeInt.add(x);
				shapeInt.add(y-1);shapeInt.add(x+1);
				shapeInt.add(y);shapeInt.add(x+1);
				break;
			case 2: // semy plus
				switch(shapeRotation){ //4 rotation positions
					case 0: 
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						shapeInt.add(y+1);shapeInt.add(x);
						break;
					case 1:
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						shapeInt.add(y+1);shapeInt.add(x);
						break;
					case 2:
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						break;
					case 3:
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y+1);shapeInt.add(x);
						break;
				}
				break;
			case 3: // r shape
				switch(shapeRotation){ //4 rotation positions
					case 0:
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y-1);shapeInt.add(x+1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x);
						break;
					case 1:
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						shapeInt.add(y+1);shapeInt.add(x+1);
						break;
					case 2:
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x-1);
						break;
					case 3:
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y-1);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						break;
				}
				break;
			case 4: // r reverse shape
				switch(shapeRotation){
					case 0:
						shapeInt.add(y-1);shapeInt.add(x-1);
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x);
						break;
					case 1:
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						shapeInt.add(y-1);shapeInt.add(x+1);
						break;
					case 2:
						shapeInt.add(y-1);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x);
						shapeInt.add(y+1);shapeInt.add(x+1);
						break;
					case 3:
						shapeInt.add(y);shapeInt.add(x-1);
						shapeInt.add(y+1);shapeInt.add(x-1);
						shapeInt.add(y);shapeInt.add(x);
						shapeInt.add(y);shapeInt.add(x+1);
						break;
				}
				break;
			case 5: //s shape
				if(shapeRotation==0||shapeRotation==2){
					shapeInt.add(y-1);shapeInt.add(x+1);
					shapeInt.add(y);shapeInt.add(x+1);
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y+1);shapeInt.add(x);
				}else{
					shapeInt.add(y);shapeInt.add(x-1);
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y+1);shapeInt.add(x);
					shapeInt.add(y+1);shapeInt.add(x+1);
				}
				break;
			case 6: //s shape reverse
				if(shapeRotation==0||shapeRotation==2){
					shapeInt.add(y-1);shapeInt.add(x);
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y);shapeInt.add(x+1);
					shapeInt.add(y+1);shapeInt.add(x+1);
				}else{
					shapeInt.add(y);shapeInt.add(x);
					shapeInt.add(y);shapeInt.add(x+1);
					shapeInt.add(y+1);shapeInt.add(x-1);
					shapeInt.add(y+1);shapeInt.add(x);
				}
				break;
			case 7: //plus shape
				shapeInt.add(y-1);shapeInt.add(x);
				shapeInt.add(y);shapeInt.add(x-1);
				shapeInt.add(y);shapeInt.add(x);
				shapeInt.add(y);shapeInt.add(x+1);
				shapeInt.add(y+1);shapeInt.add(x);
				break;
		}
	}
}