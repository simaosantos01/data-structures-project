package program;

import CompanyManagement.ManageCompany;
import Locals.ILocal;
import Locals.IMarket;
import Locals.IWarehouse;
import Locals.Market;
import Locals.Warehouse;
import SellersManagement.ISeller;
import SellersManagement.Seller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Simão
 */
public class Program {

    private Scanner scanner;
    private ManageCompany manageCompany;

    public Program() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the program
     */
    public void start() {
        String input;

        do {
            System.out.println("Insert company name: ");
            input = this.scanner.nextLine();
        } while (input.isBlank());
        this.manageCompany = new ManageCompany(input);
        this.showMainMenu();
    }

    private void printMenu() {
        System.out.println("**************************************");
        System.out.println("|               MENU                 |");
        System.out.println("**************************************");
        System.out.println("| 1- Add Local     9- Remove Local   |");
        System.out.println("| 2- Add Road      10- Remove Road   |");
        System.out.println("| 3- Add Seller    11- Remove Seller |");
        System.out.println("|                                    |");
        System.out.println("| 4- Show Locals   12- Edit Locals   |");
        System.out.println("| 5- Show Roads    13- Edit Roads    |");
        System.out.println("| 6- Show Sellers  14- Edit Sellers  |");
        System.out.println("|                                    |");
        System.out.println("| 7- Import JSON   15- Export JSON   |");
        System.out.println("|                                    |");
        System.out.println("| 8- Generate Route          0- Exit |");
        System.out.println("**************************************");
    }

    private void showMainMenu() {
        String input;
        boolean flag = true;

        do {
            this.printMenu();
            System.out.println("Input your selection: ");
            input = scanner.nextLine();

            switch (input) {
                case "1" ->
                    this.addLocal();
                case "2" ->
                    this.addRoad();
                case "3" ->
                    this.addSeller();
                case "4" ->
                    this.showLocals();
                case "5" ->
                    this.showRoads();
                case "6" ->
                    this.showSellers();
                case "7" ->
                    this.importJSON();
                case "8" ->
                    this.generateRoute();
                case "9" ->
                    this.removeLocal();
                case "10" ->
                    this.removeRoad();
                case "11" ->
                    this.removeSeller();
                case "12" ->
                    this.editLocals();
                case "13" ->
                    this.editRoads();
                case "14" ->
                    this.editSellers();
                case "15" ->
                    this.manageCompany.companyToJson();
                case "0" ->
                    flag = false;
            }
        } while (flag);
    }

    private void printAddLocal() {
        System.out.println("**************************************");
        System.out.println("*             NEW LOCAL              *");
        System.out.println("**************************************");
    }

    private void addLocal() {
        String input;
        boolean flag = true;

        do {
            this.printAddLocal();
            System.out.println("Type (W = Warehouse / M = Market / 0 To Exit): ");
            input = scanner.nextLine();

            switch (input) {
                case "M" ->
                    this.addMarket();
                case "W" ->
                    this.addWarehouse();
                case "0" ->
                    flag = false;
            }
        } while (flag);
    }

    private int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void generateRandomClients(String name) {
        int numClients = this.getRandomInt(1, 5);

        for (int i = 0; i < numClients; i++) {
            this.manageCompany.addMarketClient(name, this.getRandomInt(40, 80));
        }
    }

    private void addMarket() {
        String name;
        boolean flag;

        do {
            System.out.println("Insert market name / 0 to Exit: ");
            name = this.scanner.nextLine();

            if (name.isBlank()) {
                flag = true;
            } else if (name.equals("0")) {
                return;
            } else if (this.manageCompany.addLocal(new Market(name)) == false) {
                System.out.println("Local name alreay taken.");
                flag = true;
            } else {
                this.generateRandomClients(name);
                System.out.println("Market added correctly!");
                flag = false;
            }
        } while (flag);
    }

    private void addWarehouse() {
        String name;
        boolean flag;

        do {
            System.out.println("Insert warehouse name / 0 to Exit: ");
            name = this.scanner.nextLine();

            if (name.isBlank()) {
                flag = true;
            } else if (name.equals("0")) {
                return;
            } else if (this.manageCompany.addLocal(new Warehouse(name,
                    this.getRandomInt(500, 1000))) == false) {

                System.out.println("Local name alreay taken.");
                flag = true;
            } else {
                System.out.println("Warehouse added correctly!");
                this.manageCompany.loadWarehouseStock(name,
                        this.getRandomInt(300, this.manageCompany
                                .getWarehouseCapacity(name)));

                flag = false;
            }
        } while (flag);
    }

    private void printAddRoad() {
        System.out.println("**************************************");
        System.out.println("*             NEW ROAD               *");
        System.out.println("**************************************");
    }

    private void addRoad() {
        String startLocal;
        String targetLocal;
        double distance;
        boolean flag;

        do {
            this.printAddRoad();
            System.out.println("Insert the starting local / 0 to Exit: ");
            startLocal = this.scanner.nextLine();

            if ("0".equals(startLocal)) {
                return;
            }
            System.out.println("Insert the target local / 0 to Exit: ");
            targetLocal = this.scanner.nextLine();

            if ("0".equals(targetLocal)) {
                return;
            }
            System.out.println("Insert the distance: ");

            try {
                distance = this.scanner.nextDouble();

                if (startLocal.isBlank() || targetLocal.isBlank()
                        || distance <= 0) {

                    flag = true;
                } else if (this.manageCompany.addRoad(startLocal, targetLocal,
                        distance)
                        == false) {

                    System.out.println("This road impossible.");
                    flag = true;
                } else {
                    System.out.println("Road added correctly!");
                    flag = false;
                }
            } catch (Exception e) {
                System.out.println("This road is impossible");
                flag = true;
            }
            this.scanner = new Scanner(System.in);
        } while (flag);
    }

    private void printAddSeller() {
        System.out.println("**************************************");
        System.out.println("*            NEW SELLER              *");
        System.out.println("**************************************");
    }

    private String addInputMarkets(String markets, String id) {
        char array[] = markets.toCharArray();
        String market = "";
        String addedMarkets = "Added markets: ";

        for (int i = 0; i < array.length; i++) {
            if (array[i] == ',') {
                if (this.manageCompany.addMarketToSeller(market, id)) {
                    addedMarkets += market + ", ";
                }
                market = "";
            } else {
                market += array[i];
            }

            if (i == array.length - 1) {
                if (this.manageCompany.addMarketToSeller(market, id)) {
                    addedMarkets += market + ", ";
                }
            }
        }
        return addedMarkets;
    }

    private void addSeller() {
        String name;
        String markets;
        boolean flag;

        do {
            this.printAddSeller();
            System.out.println("Insert the name of the seller / 0 to Exit: ");
            name = this.scanner.nextLine();
            System.out.println("Insert the markets (format: marketA,marketB) "
                    + "/ 0 to Exit: ");
            markets = this.scanner.nextLine();

            if (name.isBlank() || markets.isBlank() || markets.contains(" ")) {
                flag = true;
            } else if (name.equals("0")) {
                return;
            } else if (this.manageCompany.addSeller(new Seller(String
                    .valueOf(this.manageCompany.getNumberOfSellers() + 1), name,
                    this.getRandomInt(200, 300))) == false) {

                flag = true;
            } else {
                System.out.println(this.addInputMarkets(markets,
                        String.valueOf(this.manageCompany.getNumberOfSellers())));

                System.out.println("Seller added correctly!");
                flag = false;
            }
        } while (flag);
    }

    private void showLocals() {
        this.manageCompany.printLocals();
        System.out.println("\nEnter to proceed.");
        this.scanner.nextLine();
    }

    private void showRoads() {
        this.manageCompany.printRoads();
        System.out.println("\nEnter to proceed.");
        this.scanner.nextLine();
    }

    private void showSellers() {
        this.manageCompany.printSellers();
        System.out.println("\nEnter to proceed.");
        this.scanner.nextLine();
    }

    private void printImportJSON() {
        System.out.println("**************************************");
        System.out.println("*            IMPORT JSON             *");
        System.out.println("**************************************");
    }

    private void printRoute() {
        System.out.println("**************************************");
        System.out.println("*               ROUTE                *");
        System.out.println("**************************************");
    }

    private void generateRoute() {
        boolean flag;
        String id;

        do {
            this.printRoute();
            System.out.println("Insert the seller Id / 0 to Exit: ");
            id = this.scanner.nextLine();

            if (id.isBlank()) {
                flag = true;
            } else if (id.equals("0")) {
                return;
            } else {
                ISeller seller = this.getSeller(id);

                if (seller != null) {
                    seller.loadGoods(seller.getMaxWeight());
                    this.manageCompany.printRouteForSeller(
                            this.manageCompany.getCompany(), seller);

                    flag = false;
                } else {
                    System.out.println("The Seller doesn't exist.");
                    flag = true;
                }
            }
        } while (flag);
    }

    private void importJSON() {
        boolean flag;
        String filePath;

        do {
            this.printImportJSON();
            System.out.println("Insert the file path / 0 to Exit: ");
            filePath = this.scanner.nextLine();

            if (filePath.isBlank()) {
                flag = true;
            } else if (filePath.equals("0")) {
                return;
            } else {
                JsonImporter jsonImporter = new JsonImporter(filePath);
                try {
                    jsonImporter.importData(this.manageCompany);
                    flag = false;
                } catch (FileNotFoundException | ParseException ex) {
                    System.out.println("Invalid path.");
                    flag = true;
                } catch (IOException ex) {
                    System.out.println("Invalid path.");
                    flag = true;
                }
            }
        } while (flag);

        System.out.println("\nEnter to proceed.");
        this.scanner.nextLine();
    }

    private void printRemoveLocal() {
        System.out.println("**************************************");
        System.out.println("*            REMOVE LOCAL            *");
        System.out.println("**************************************");
    }
    
    private void removeFromSeller(String market){
        Iterator<ISeller> sellers = this.manageCompany.getSellers();
        while(sellers.hasNext()){
            ISeller currentSeller = sellers.next();
            currentSeller.removeMarketToVisit(market);
        }
    }

    private void removeLocal() {
        boolean flag;
        String name;

        do {
            this.printRemoveLocal();
            System.out.println("Insert the name of the local to remove "
                    + "/ 0 to Exit: ");

            name = this.scanner.nextLine();

            if (name.isBlank()) {
                flag = true;
            } else if (name.equals("0")) {
                return;
            } else if (name.equals(this.manageCompany.getCompanyName())) {
                System.out.println("Company can't be removed.");
                flag = true;
            } else if (this.manageCompany.removeLocal(name) == false) {
                System.out.println("This local doesn't exist.");
                flag = true;
            } else {
                this.removeFromSeller(name);
                System.out.println("Local removed correctly!");
                flag = false;
            }
        } while (flag);
    }

    private void printRemoveRoad() {
        System.out.println("**************************************");
        System.out.println("*             REMOVE ROAD            *");
        System.out.println("**************************************");
    }

    private void removeRoad() {
        boolean flag;
        String startLocal;
        String targetLocal;

        do {
            this.printRemoveRoad();
            System.out.println("Insert the starting local / 0 to Exit: ");
            startLocal = this.scanner.nextLine();

            if ("0".equals(startLocal)) {
                return;
            }
            System.out.println("Insert the target local / 0 to Exit: ");
            targetLocal = this.scanner.nextLine();

            if ("0".equals(targetLocal)) {
                return;
            }
            if (startLocal.isBlank() || targetLocal.isBlank()) {
                flag = true;
            } else if (this.manageCompany.removeRoad(startLocal, targetLocal)
                    == false && this.manageCompany.removeRoad(targetLocal,
                            startLocal) == false) {

                System.out.println("This Road doesn't exist.");
                flag = true;
            } else {
                System.out.println("Road removed correctly!");
                flag = false;
            }
        } while (flag);
    }

    private void printRemoveSeller() {
        System.out.println("**************************************");
        System.out.println("*           REMOVE SELLER            *");
        System.out.println("**************************************");
    }

    private void removeSeller() {
        boolean flag;
        String id;

        do {
            this.printRemoveSeller();
            System.out.println("Insert the id of the seller to remove "
                    + "/ 0 to Exit: ");

            id = this.scanner.nextLine();

            if (id.isBlank()) {
                flag = true;
            } else if ("0".equals(id)) {
                return;
            } else if (this.manageCompany.removeSeller(id) == false) {
                System.out.println("This Seller doesn't exist.");
                flag = true;
            } else {
                System.out.println("Seller removed correctly!");
                flag = false;
            }
        } while (flag);
    }

    private void printEditLocals() {
        System.out.println("**************************************");
        System.out.println("*             EDIT LOCALS            *");
        System.out.println("**************************************");
    }

    private ILocal getLocal(String name) {
        Iterator<IMarket> markets = this.manageCompany.getMarkets();
        Iterator<IWarehouse> warehouses = this.manageCompany.getWarehouses();

        while (markets.hasNext()) {
            IMarket currentMarket = markets.next();
            if (currentMarket.getName().equals(name)) {
                return currentMarket;
            }
        }
        while (warehouses.hasNext()) {
            IWarehouse currentWarehouse = warehouses.next();
            if (currentWarehouse.getName().equals(name)) {
                return currentWarehouse;
            }
        }
        return null;
    }

    private void editLocals() {
        boolean flag;
        String name;

        do {
            this.printEditLocals();
            System.out.println("Insert the name of the Local to edit "
                    + "/ 0 to Exit: ");

            name = this.scanner.nextLine();

            if (name.isBlank()) {
                flag = true;
            } else if (name.equals("0")) {
                return;
            } else {
                ILocal local = this.getLocal(name);

                if (local instanceof Market) {
                    this.editMarket(name);
                } else if (local instanceof Warehouse) {
                    this.editWarehouse(name);
                }
                flag = false;
            }
        } while (flag);
    }

    private void editMarket(String name) {
        int removeClients;
        int addClients;
        boolean flag;

        do {
            try {
                System.out.println("Number of clients to remove: ");
                removeClients = this.scanner.nextInt();
                System.out.println("Number of clients to add: ");
                addClients = this.scanner.nextInt();

                if (removeClients < 0 || removeClients > this.manageCompany
                        .getNumberOfMarketClients(name) || addClients < 0) {

                    System.out.println("Changes are not possible.");
                    flag = true;
                } else {
                    for (int i = 0; i < removeClients; i++) {
                        this.manageCompany.serveMarketClient(name,
                                Integer.MAX_VALUE);
                    }
                    for (int i = 0; i < addClients; i++) {
                        this.manageCompany.addMarketClient(name,
                                this.getRandomInt(40, 80));
                    }
                    System.out.println("Changes saved!");
                    flag = false;
                }
            } catch (Exception e) {
                flag = true;
            }
            this.scanner = new Scanner(System.in);
        } while (flag);
    }

    private void editWarehouse(String name) {
        int capacity;
        int stock;
        boolean flag;

        do {
            try {
                System.out.println("Insert the new capacity: ");
                capacity = this.scanner.nextInt();
                System.out.println("Insert the current stock: ");
                stock = this.scanner.nextInt();

                if (capacity <= 0 || stock < 0 || stock > capacity) {
                    System.out.println("Changes are not possible");
                    flag = true;
                } else {
                    System.out.println("Changes saved!");
                    this.manageCompany.unloadWarehouseStock(name,
                            this.manageCompany.getWarehouseStock(name));

                    this.manageCompany.setWarehouseCapacity(name, capacity);
                    this.manageCompany.loadWarehouseStock(name, stock);
                    flag = false;
                }
            } catch (Exception e) {
                flag = true;
            }
            this.scanner = new Scanner(System.in);
        } while (flag);
    }

    private void printEditRoads() {
        System.out.println("**************************************");
        System.out.println("*             EDIT ROADS             *");
        System.out.println("**************************************");
    }

    private void editRoads() {
        String startLocal;
        String targetLocal;
        double distance;
        boolean flag;

        do {
            this.printEditRoads();
            System.out.println("Insert the starting local / 0 to Exit: ");
            startLocal = this.scanner.nextLine();

            if ("0".equals(startLocal)) {
                return;
            }
            System.out.println("Insert the target local / 0 to Exit: ");
            targetLocal = this.scanner.nextLine();

            if ("0".equals(targetLocal)) {
                return;
            }
            System.out.println("Insert the distance: ");
            try {
                distance = this.scanner.nextDouble();

                if (startLocal.isBlank() || targetLocal.isBlank()
                        || distance <= 0) {

                    flag = true;
                } else if (this.manageCompany.setRoadDistance(startLocal,
                        targetLocal, distance) == false
                        && this.manageCompany.setRoadDistance(targetLocal,
                                startLocal, distance) == false) {

                    System.out.println("Changes are not possible.");
                    flag = true;

                } else {
                    System.out.println("Changes saved!");
                    flag = false;
                }

            } catch (Exception e) {
                flag = true;
            }
            this.scanner = new Scanner(System.in);
        } while (flag);
    }

    private ISeller getSeller(String id) {
        Iterator<ISeller> sellers = this.manageCompany.getSellers();

        while (sellers.hasNext()) {
            ISeller currentSeller = sellers.next();
            if (currentSeller.getId().equals(id)) {
                return currentSeller;
            }
        }
        return null;
    }

    private void editSellers() {
        boolean flag;
        String id;

        do {
            System.out.println("Insert the Id of the Seller to edit "
                    + "/ 0 to Exit: ");

            id = this.scanner.nextLine();

            if (id.isBlank()) {
                flag = true;
            } else if (id.equals("0")) {
                return;
            } else {
                ISeller seller = this.getSeller(id);

                System.out.println("Insert the name:");
                String name = this.scanner.nextLine();

                try {
                    System.out.println("Insert the max weight: ");
                    int maxWeight = this.scanner.nextInt();
                    this.scanner = new Scanner(System.in);

                    System.out.println("Insert the markets (format: marketA,"
                            + "marketB) / 0 to Exit: ");
                    String markets = this.scanner.nextLine();

                    if (name.isBlank() || maxWeight <= 0 || seller == null
                            || markets.isBlank() || markets.contains(" ")) {

                        System.out.println("Changes are not possible.");
                        flag = true;
                    } else {
                        System.out.println("Changes saved!");
                        seller.setName(name);
                        seller.setMaxWeight(maxWeight);
                        System.out.println(this.addInputMarkets(markets, id));
                        flag = false;
                    }
                } catch (Exception e) {
                    flag = true;
                }
            }
        } while (flag);
    }

}
