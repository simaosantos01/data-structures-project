package program;

import CompanyManagement.ManageCompany;
import Locals.IMarket;
import Locals.IWarehouse;
import Locals.Local;
import Locals.Market;
import Locals.Warehouse;
import SellersManagement.ISeller;
import SellersManagement.Seller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Simão
 */
public class JsonImporter {

    private String filePath;

    /**
     * JsonImporter class constructor.
     *
     * @param filePath the filepath
     */
    public JsonImporter(String filePath) {
        this.filePath = filePath;
    }

    private int importSellers(JSONArray jSellers, ManageCompany manageCompany) {
        Iterator it = jSellers.iterator();
        int counter = 0;
        while (it.hasNext()) {
            try {
                JSONObject jSeller = (JSONObject) it.next();
                String id = String.valueOf((long) jSeller.get("id"));
                String name = (String) jSeller.get("nome");
                int capacity = (int) (long) jSeller.get("capacidade");
                JSONArray jMarkets = (JSONArray) jSeller.get("mercados_a_visitar");
                ISeller seller = new Seller(id, name, capacity);

                Iterator itMarkets = jMarkets.iterator();
                while (itMarkets.hasNext()) {
                    String market = (String) itMarkets.next();
                    seller.addMarketToVisit(market);
                }
                if (manageCompany.addSeller(seller)) {
                    counter++;
                }
            } catch (Exception e) {
                System.out.println("There are non valid Sellers!");
            }
        }
        return counter;
    }

    private int importLocals(JSONArray jLocals, ManageCompany manageCompany) {
        Iterator it = jLocals.iterator();
        int counter = 0;
        while (it.hasNext()) {
            try {
                JSONObject jLocal = (JSONObject) it.next();
                String name = (String) jLocal.get("nome");
                String type = (String) jLocal.get("tipo");
                if (type.equals("Mercado")) {
                    IMarket market = new Market(name);
                    JSONArray jClients = (JSONArray) jLocal.get("clientes");

                    Iterator itClients = jClients.iterator();
                    while (itClients.hasNext()) {
                        int client = (int) (long) itClients.next();
                        market.addClient(client);
                    }
                    if (manageCompany.addLocal(market)) {
                        counter++;
                    }
                } else if (type.equals("Armazém")) {
                    int capacity = (int) (long) jLocal.get("capacidade");
                    int stock = (int) (long) jLocal.get("stock");
                    IWarehouse warehouse = new Warehouse(name, capacity);
                    warehouse.loadStock(stock);

                    if (manageCompany.addLocal(warehouse)) {
                        counter++;
                    }
                } else if (type.equals("Sede")) {
                    manageCompany.setCompany(new Local(name));
                }
            } catch (Exception e) {
                System.out.println("There are non valid Locals!");
            }
        }
        return counter;
    }

    private int importRoad(JSONArray jRoads, ManageCompany manageCompany) {
        Iterator it = jRoads.iterator();
        int counter = 0;
        while (it.hasNext()) {
            try {
                JSONObject jRoad = (JSONObject) it.next();
                String from = (String) jRoad.get("de");
                String to = (String) jRoad.get("para");
                double distance = (double) (long) jRoad.get("distancia");

                if (manageCompany.addRoad(from, to, distance)) {
                    counter++;
                }
            } catch (Exception e) {
                System.out.println("There are non valid Roads!");
            }
        }
        return counter;
    }

    /**
     * Imports the .json file data.
     *
     * @param manageCompany the company
     * @throws FileNotFoundException if the file was not found
     * @throws IOException if the file was not found
     * @throws ParseException if the file cannot be parsed
     */
    public void importData(ManageCompany manageCompany) throws
            FileNotFoundException, IOException, ParseException {

        try {
            Object obj = new JSONParser().parse(new FileReader(this.filePath));
            JSONObject jObject = (JSONObject) obj;
            JSONArray jSellers = (JSONArray) jObject.get("vendedores");
            JSONArray jLocais = (JSONArray) jObject.get("locais");
            JSONArray jRoads = (JSONArray) jObject.get("caminhos");

            System.out.println("New Sellers: " + this.importSellers(jSellers,
                    manageCompany));
            System.out.println("New Locals: " + this.importLocals(jLocais,
                    manageCompany));
            System.out.println("New Roads: " + this.importRoad(jRoads,
                    manageCompany));
        } catch (IOException | ParseException e) {
            System.out.println("The Json is invalid.");
        }
    }
}
