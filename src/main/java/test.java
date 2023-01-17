public class test {
    public static void main(String[] args) {
        ShoppingCentre shoppingCentre = new ShoppingCentre();
        shoppingCentre.addProduct("product1",10,"producer1");
        shoppingCentre.addProduct("Product1",10,"producer1");
        shoppingCentre.addProduct("product2",15,"producer1");
        shoppingCentre.addProduct("product1",10,"producer1");
        shoppingCentre.addProduct("product4",10,"producer2");
        shoppingCentre.addProduct("product5",5,"producer2");
        String result1 = shoppingCentre.findProductsByName("product1");
        String result11 = shoppingCentre.findProductsByName("product11");
        String result2 = shoppingCentre.findProductsByProducer("producer1");
        String result22 = shoppingCentre.findProductsByProducer("producer11");
        String result3 = shoppingCentre.findProductsByPriceRange(6,14);
        String result33 = shoppingCentre.findProductsByPriceRange(5,15);
        String result333 = shoppingCentre.findProductsByPriceRange(1,3);
        String result4 = shoppingCentre.delete("producer11");
     //   String result44 = shoppingCentre.delete("producer1");
        String result5 = shoppingCentre.delete("product3","producer1");
        String result55 = shoppingCentre.delete("product1","producer11");
        String result555 = shoppingCentre.delete("product1","producer1");
    }
}
