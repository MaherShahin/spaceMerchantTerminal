import Game.*;

import java.io.IOException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {


        Scanner sc = new Scanner(System.in);
        GameState state = GameState.ENTRY;
        System.out.println("Enter help to view available commands");
        Game game = new Game();

        while (state!=GameState.EXIT){
            System.out.println("Please Enter Command");
            String input = sc.nextLine().trim();
            if(state == GameState.ENTRY){
                if (input.equals("quit")){
                    System.out.println("Goodbye!");
                    state = GameState.EXIT;
                } else if (input.equals("load") ) {
                    System.out.println("Please enter the file name + .ser");
                    String filename = sc.nextLine();
                    game.loadGameXML(filename);
                    state = GameState.ACTIVE;
                    System.out.println();
                    game.printGameData();
                } else if (input.equals("new") ) {
                    System.out.println("Available commands are [sail], [buy], [sell] ");
                    game.newGame();
                    state = GameState.ACTIVE;
                    game.printGameData();
                } else if (input.equals("help")){
                    System.out.println("available commands are [help], [new], [load] and [quit]");
                } else if (input.equals("save")){
                    System.out.println("Please enter your desired filename");
                    String filename = sc.nextLine();
                    game.saveGameXML(filename);
                } else {
                    System.out.println("Command unknown, please enter a valid command, enter help to see valid commands");
                }
            } else if (state == GameState.ACTIVE) {
                switch (input){
                    case ("sail"): {
                        try {
                            System.out.println("Please enter your destination: ");
                            String destination = sc.nextLine();
                            game.getCaptain().sail(destination);
                            game.printGameData();
                        } catch (IOException e){
                            System.out.println("Error " + e.getMessage());
                            System.out.println("-------------------------------------------------------------");
                        }
                        break;
                    }
                    case ("buy"): {
                        try {
                            System.out.println("Enter your desired item name: ");
                            String itemName = sc.nextLine().toLowerCase().trim();
                            System.out.println("Please enter the desired quantity: ");
                            String quantity = sc.nextLine().trim();
                                game.getCaptain().buy(itemName, Integer.parseInt(quantity));
                                game.printGameData();
                            }
                        catch (IOException | NumberFormatException e){
                            System.out.println("Error: " + e.getMessage());
                            System.out.println("-------------------------------------------------------------");
                        }
                        break;
                    }
                    case("sell"): {
                        try {
                            System.out.println("Enter your desired item name: ");
                            String itemName = sc.nextLine().toLowerCase();
                            System.out.println("Please enter the desired quantity: ");
                            String quantity = sc.nextLine();
                            game.getCaptain().sell(itemName, Integer.parseInt(quantity));
                            game.printGameData();
                        } catch (IOException | NumberFormatException e){
                            System.out.println("Error " + e.getMessage());
                            System.out.println("-------------------------------------------------------------");
                        }

                        break;
                    }
                    case ("save"):{
                        System.out.println("Please enter your desired filename");
                        String filename = sc.nextLine();
                        game.saveGameXML(filename);
                        break;
                    }
                    case ("quit"):{
                        state = GameState.EXIT;
                        break;

                    }
                    default: {
                        System.out.println("Not a valid input");
                        System.out.println("-------------------------------------------------------------");
                    }
                    if (game.getCaptain().getCredit()==600){
                        System.out.println("Congratulations! You have won");
                        state = GameState.EXIT;
                    }
                }
            }
        }
    }
}