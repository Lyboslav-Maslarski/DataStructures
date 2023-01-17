import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCentre {
    private final Map<String, List<Product>> productsByName;
    private final Map<String, List<Product>> productsByProducer;

    public ShoppingCentre() {
        this.productsByName = new HashMap<>();
        this.productsByProducer = new HashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        productsByName.putIfAbsent(name, new ArrayList<>());
        productsByName.get(name).add(product);
        productsByProducer.putIfAbsent(producer, new ArrayList<>());
        productsByProducer.get(producer).add(product);
        return "Product added" + System.lineSeparator();
    }

    public String delete(String name, String producer) {
        if (!productsByName.containsKey(name)) {
            return "No products found" + System.lineSeparator();
        }
        if (!productsByProducer.containsKey(producer)) {
            return "No products found" + System.lineSeparator();
        }
        int initialSize = productsByName.get(name).size();
        if (initialSize == 0 || productsByProducer.get(producer).size() == 0) {
            return "No products found" + System.lineSeparator();
        }
        productsByName.get(name).removeIf(p -> p.getProducer().equals(producer));
        productsByProducer.get(producer).removeIf(p -> p.getName().equals(name));
        int endSize = productsByName.get(name).size();
        return (initialSize - endSize) + " products deleted" + System.lineSeparator();
    }

    public String delete(String producer) {
        if (!productsByProducer.containsKey(producer)) {
            return "No products found" + System.lineSeparator();
        }
        List<Product> products = productsByProducer.get(producer);
        if (products.isEmpty()) {
            return "No products found" + System.lineSeparator();
        }
        int initialSize = products.size();
        for (Product product : products) {
            String name = product.getName();
            if (productsByName.containsKey(name)) {
                productsByName.get(name).removeIf(product1 -> product1.getProducer().equals(producer));
            }
        }
        productsByProducer.get(producer).clear();
        return initialSize + " products deleted" + System.lineSeparator();
    }

    public String findProductsByName(String name) {
        if (!productsByName.containsKey(name)) {
            return "No products found" + System.lineSeparator();
        }
        List<Product> list = productsByName.get(name);
        if (list.size() == 0) {
            return "No products found" + System.lineSeparator();
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (Product product : list) {
            sb.append('{')
                    .append(product.getName())
                    .append(';')
                    .append(product.getProducer())
                    .append(';')
                    .append(String.format("%.2f", product.getPrice()))
                    .append('}')
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String findProductsByProducer(String producer) {
        if (!productsByProducer.containsKey(producer)) {
            return "No products found" + System.lineSeparator();
        }
        List<Product> list = productsByProducer.get(producer);
        if (list.size() == 0) {
            return "No products found" + System.lineSeparator();
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (Product product : list) {
            sb.append('{')
                    .append(product.getName())
                    .append(';')
                    .append(product.getProducer())
                    .append(';')
                    .append(String.format("%.2f", product.getPrice()))
                    .append('}')
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        List<Product> list = new ArrayList<>();
        for (List<Product> products : productsByName.values()) {
            products.stream().filter(p -> p.getPrice() >= priceFrom && p.getPrice() <= priceTo).forEach(list::add);
        }
        if (list.size() == 0) {
            return "No products found" + System.lineSeparator();
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (Product product : list) {
            sb.append('{')
                    .append(product.getName())
                    .append(';')
                    .append(product.getProducer())
                    .append(';')
                    .append(String.format("%.2f", product.getPrice()))
                    .append('}')
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
