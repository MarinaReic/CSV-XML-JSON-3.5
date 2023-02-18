import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class Basket implements Serializable {

    private String[] products;
    private int[] prices;
    private long[] amountProduct;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.amountProduct = new long[products.length];
    }

    public void addToCart(int productNum, int amount) {
        amountProduct[productNum] += amount;
    }

    public void printCart() {
        long sum = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (amountProduct[i] != 0) {
                long sumProduct = amountProduct[i] * prices[i];
                System.out.println((i + 1) + ". " + products[i] + " " + amountProduct[i] + " шт по " + prices[i] + " руб/шт - " + sumProduct + " руб в сумме");
                sum += sumProduct;
            }
        }
        System.out.println("Итого " + sum + " руб");
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (int i = 0; i < amountProduct.length; i++) {
                out.println(products[i] + " - " + amountProduct[i] + " - " + prices[i]);
            }
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile));
             BufferedReader reader1 = new BufferedReader(new FileReader(textFile))) {

            int size = 0;
            while (reader.readLine() != null) {
                size++;
            }

            String[] products = new String[size];
            long[] amountProduct = new long[size];
            int[] prices = new int[size];
            for (int i = 0; i < size; i++) {
                String[] parts = reader1.readLine().split(" - ");
                products[i] = parts[0];
                amountProduct[i] = Long.parseLong(parts[1]);
                prices[i] = Integer.parseInt(parts[2]);
            }
            Basket basket = new Basket(products, prices);
            basket.amountProduct = amountProduct;
            return basket;
        }
    }

    public long size() {
        return amountProduct.length;
    }

    public String getProduct(int num) {
        return products[num - 1];
    }

    public int getPrice(int num) {
        return prices[num - 1];
    }

    public void saveJson(File fileJson) throws IOException {
        Basket basket = new Basket(products, prices);
        basket.amountProduct = amountProduct;

        JSONObject basketJson = new JSONObject();
        JSONArray listProduct = new JSONArray();
        JSONArray listAmount = new JSONArray();
        JSONArray listPrice = new JSONArray();
        for (int i = 0; i < products.length; i++) {
            listProduct.add(products[i]);
            listAmount.add(amountProduct[i]);
            listPrice.add(prices[i]);
        }
        basketJson.put("products", listProduct);
        basketJson.put("amountProduct", listAmount);
        basketJson.put("prices", listPrice);

        try (FileWriter file = new FileWriter(fileJson)) {
            file.write(basketJson.toJSONString());
            file.flush();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        gson.toJson(basket);
    }

    public static Basket loadFromJsonFile(File fileJson) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileJson));
        JSONObject jsonObject = (JSONObject) obj;
        return gson.fromJson(jsonObject.toJSONString(), Basket.class);
    }
}
