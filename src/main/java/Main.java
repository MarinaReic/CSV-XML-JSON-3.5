import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
        int[] prices = {50, 14, 80};
        Basket basket = new Basket(products, prices);

        File file = new File("basket.json");
        if (file.exists()) {
            try {
                basket = Basket.loadFromJsonFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Список возможных товаров для покупки");
        for (int i = 1; i <= products.length; i++) {
            System.out.println(i + ". " + basket.getProduct(i) + " " + basket.getPrice(i) + " руб/шт");
        }

        Scanner scanner = new Scanner(System.in);

        ClientLog log = new ClientLog();

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();

            if ("end".equals(input)) {
                break;
            }

            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            basket.addToCart(productNum, amount);
            try {
                log.log(productNum + 1, amount);
                basket.saveJson(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File txtFile = new File("log.csv");
        try {
            log.exportAsCSV(txtFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        basket.printCart();
        scanner.close();
    }
}
