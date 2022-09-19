import implementations.ArrayDeque;

public class Main {
    public static void main(String[] args) {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.add(13);
        arrayDeque.add(14);
        arrayDeque.add(15);
        arrayDeque.add(16);
        arrayDeque.add(17);
        arrayDeque.insert(1,111);
        System.out.println(arrayDeque.get(0));


    }
}
