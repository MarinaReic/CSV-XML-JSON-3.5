import java.io.*;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private long[] cart;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.cart = new long[products.length];
    }

    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
    }

    public void printCart() {
        long sum = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (cart[i] != 0) {
                long sumProduct = cart[i] * prices[i];
                System.out.println((i + 1) + ". " + products[i] + " " + cart[i] + " шт по " + prices[i] + " руб/шт - " + sumProduct + " руб в сумме");
                sum += sumProduct;
            }
        }
        System.out.println("Итого " + sum + " руб");
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (int i = 0; i < cart.length; i++) {
                out.println(products[i] + " - " + cart[i] + " - " + prices[i]);
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
            long[] cart = new long[size];
            int[] prices = new int[size];
            for (int i = 0; i < size; i++) {
                String[] parts = reader1.readLine().split(" - ");
                products[i] = parts[0];
                cart[i] = Long.parseLong(parts[1]);
                prices[i] = Integer.parseInt(parts[2]);
            }
            Basket basket = new Basket(products, prices);
            basket.cart = cart;
            return basket;
        }
    }

    public long size() {
        return cart.length;
    }

    public String getProduct(int num) {
        return products[num - 1];
    }

    public int getPrice(int num) {
        return prices[num - 1];
    }

    public void saveBin(File file) throws IOException {
        Basket basket = new Basket(products, prices);
        basket.cart = cart;
        try (FileOutputStream out = new FileOutputStream(file);
             ObjectOutputStream obj = new ObjectOutputStream(out)) {
            obj.writeObject(basket);
        }
    }

    public static Basket loadFromBinFile(File file) throws Exception {
        try (FileInputStream input = new FileInputStream(file);
             ObjectInputStream obj = new ObjectInputStream(input)) {
            return (Basket) obj.readObject();
        }
    }
}
