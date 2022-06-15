package Game;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game implements Cloneable, Serializable {

    private static List<Port> portsList;
    private static List<Item> itemsList;
    private static Captain captain;
    private static Ship ship;

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }
    public Captain getCaptain() {
        return captain;
    }
    public  Game() throws IOException {
        this.itemsList = new ArrayList<Item>();
        this.portsList = new ArrayList<Port>();
    }
    public void newGame() throws IOException {
        initializeItemsAndPorts();
        this.captain = new Captain();
        this.ship = new Ship(captain,portsList.get(0));
        captain.setShip(ship);
    }
    public void initializeItemsAndPorts() throws IOException {
        //Items
        Item polymer = new Item("Polymer",2,20 );
        Item computer = new Item("Computer",1,50);
        Item waschmittel = new Item("Waschmittel",4,10);
        Item treibstoff = new Item("Treibstoff",1,1);
        Item halbleiter = new Item("Halbleiter",3,30);
        Item neon = new Item("Neon",2,15);

        //List of Ports
        Port berlin = new Port("Berlin");
        Port neudorf = new Port("Neudorf");
        Port tortuga = new Port("Tortuga");
        Port trier = new Port("Trier");

        this.itemsList.add(neon);
        this.itemsList.add(halbleiter);
        this.itemsList.add(treibstoff);
        this.itemsList.add(waschmittel);
        this.itemsList.add(computer);
        this.itemsList.add(polymer);

        this.portsList.add(trier);
        this.portsList.add(berlin);
        this.portsList.add(neudorf);
        this.portsList.add(tortuga);

        randomizeItemsAndPorts();
    }
    public void randomizeItemsAndPorts() throws IOException {
        Random rand = new Random();

        //Assign for every item a random value for isTradedIn between (4-0) for PriceMultiplier + Tradeability
        for (Item i: itemsList){
            for (Port p: portsList) {
                i.addIsTradedIn(p,rand.nextInt(4));
            }
        }

        //Assign for every port a random value of items in stock
        for (Port p: portsList) {
            for (Item i: itemsList) {
                p.addItemToStock(i,rand.nextInt(15));
            }
        }

        //Assign for every port a rand dist to the other ports
        for (Port p1: portsList) {
            for (Port p2: portsList) {
                if (!p1.getName().equals(p2.getName())){
                    p1.addDistanceTo(p2, rand.nextInt(4));
                }
            }
        }
    }
    public void printGameData() throws IOException {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Current credits: "+ this.getCaptain().getCredit());
        System.out.println("Current Trip counter: " + this.getCaptain().getShip().getTripCounter());
        System.out.println("Current fuel is: "+this.getCaptain().getShip().getFuel());
        System.out.println("-----------------------------------------------------------------------------");
        this.getCaptain().getShip().printCurrentInventory();
        System.out.println("Current port is: " + this.getCaptain().getShip().getCurrentPort().toString());
        System.out.println("-----------------------------------------------------------------------------");
        this.getCaptain().getShip().getCurrentPort().printAllItemsInStock();
    }
    public void saveGameBinary(String filename){
        try{
            File file = new File(filename);
            String currentDir = System.getProperty("user.dir");
            FileOutputStream fileOut = new FileOutputStream(currentDir+"\\gameSaves\\"+file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(captain);
            fileOut.close();
            System.out.println("Serialized data is saved in " + filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadGameBinary(String filename) {
        try {
            File file = new File(filename);
            String currentDir = System.getProperty("user.dir");
            FileInputStream fileIn = new FileInputStream(currentDir+"\\gameSaves\\"+file);
            ObjectInputStream inputStream = new ObjectInputStream(fileIn);
            Captain captain = (Captain) inputStream.readObject();
            setCaptain(captain);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGameXML(String filename) throws FileNotFoundException {
        XMLEncoder encoder = null;
        File file = new File(filename);
        String currentDir = System.getProperty("user.dir");
        try {
            FileOutputStream fileOut = new FileOutputStream(currentDir+"\\gameSaves\\"+file);
            encoder = new XMLEncoder(fileOut);
            encoder.writeObject(captain);
            encoder.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGameXML(String filename) {
        File file = new File(filename);
        String currentDir = System.getProperty("user.dir");
        XMLDecoder decoder = null;
        try {
            FileInputStream inputStream = new FileInputStream(currentDir + "\\gameSaves\\" + file);
            decoder = new XMLDecoder(inputStream);
            Captain captain = (Captain) decoder.readObject();
            setCaptain(captain);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
