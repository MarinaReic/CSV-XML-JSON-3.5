import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String loadBasket;
    static String loadBasketFileName;
    static String loadBasketFileFormat;
    static String saveBasket;
    static String saveBasketFileName;
    static String saveBasketFileFormat;
    static String saveLog;
    static String saveLogFileName;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        config(new File("shop.xml"));

        String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
        int[] prices = {50, 14, 80};
        Basket basket = new Basket(products, prices);

        if (loadBasket.equals("true")) {
            File file = new File(loadBasketFileName);
            if (file.exists()) {
                try {
                    if (loadBasketFileFormat.equals("json")) {
                        basket = Basket.loadFromJsonFile(file);
                    } else {
                        basket = Basket.loadFromTxtFile(file);
                    }
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
            log.log(productNum + 1, amount);
            if (saveBasket.equals("true")) {
                File file = new File(saveBasketFileName);
                try {
                    if (saveBasketFileFormat.equals("json")) {
                        basket.saveJson(file);
                    } else {
                        basket.saveTxt(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (saveLog.equals("true")) {
            File file = new File(saveLogFileName);
            try {
                log.exportAsCSV(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        basket.printCart();
        scanner.close();
    }

    public static void config(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList nodeListLoad = doc.getElementsByTagName("load");
        for (int i = 0; i < nodeListLoad.getLength(); i++) {
            Node node = nodeListLoad.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                loadBasket = element.getElementsByTagName("enabled").item(0).getTextContent();
                loadBasketFileName = element.getElementsByTagName("fileName").item(0).getTextContent();
                loadBasketFileFormat = element.getElementsByTagName("format").item(0).getTextContent();
            }
        }
        NodeList nodeListSave = doc.getElementsByTagName("save");
        for (int i = 0; i < nodeListSave.getLength(); i++) {
            Node node = nodeListSave.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                saveBasket = element.getElementsByTagName("enabled").item(0).getTextContent();
                saveBasketFileName = element.getElementsByTagName("fileName").item(0).getTextContent();
                saveBasketFileFormat = element.getElementsByTagName("format").item(0).getTextContent();
            }
        }
        NodeList nodeListLog = doc.getElementsByTagName("log");
        for (int i = 0; i < nodeListLog.getLength(); i++) {
            Node node = nodeListLog.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                saveLog = element.getElementsByTagName("enabled").item(0).getTextContent();
                saveLogFileName = element.getElementsByTagName("fileName").item(0).getTextContent();
            }
        }
    }
}
