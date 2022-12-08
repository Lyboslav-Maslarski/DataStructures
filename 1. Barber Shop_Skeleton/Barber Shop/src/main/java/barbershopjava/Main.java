package barbershopjava;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShopImpl();

        Barber pesho = new Barber("Pesho", 100, 10);
        Barber gosho = new Barber("Gosho", 50, 10);
        Barber tosho = new Barber("Tosho", 40, 10);
        Barber viko = new Barber("Viko", 200, 10);
        Barber marti = new Barber("Marti", 10, 10);

        barberShop.addBarber(pesho);
        barberShop.addBarber(gosho);
        barberShop.addBarber(tosho);
        barberShop.addBarber(viko);
        barberShop.addBarber(marti);

        Client maria = new Client("Maria", 20, Gender.FEMALE);
        Client ganka = new Client("Ganka", 50, Gender.FEMALE);
        Client penka = new Client("Penka", 14, Gender.FEMALE);
        Client monica = new Client("Monica", 33, Gender.FEMALE);
        Client petya = new Client("Petya", 45, Gender.FEMALE);

        barberShop.addClient(maria);
        barberShop.addClient(ganka);
        barberShop.addClient(penka);
        barberShop.addClient(monica);
        barberShop.addClient(petya);
        barberShop.addClient(new Client("Maca",25,Gender.FEMALE));

        System.out.println(barberShop.exist(viko));
        System.out.println(barberShop.exist(marti));
        System.out.println(barberShop.exist(new Barber("sdsd",1,1)));
        System.out.println(barberShop.exist(penka));
        System.out.println(barberShop.exist(ganka));
        System.out.println(barberShop.exist(new Client("sadsa",1,Gender.OTHER)));
        System.out.println("============================");
        Collection<Barber> barbers = barberShop.getBarbers();
        printB(barbers);
        System.out.println("============================");
        Collection<Client> clients = barberShop.getClients();
        printC(clients);
        System.out.println("============================");
        barberShop.assignClient(gosho, ganka);
        barberShop.assignClient(gosho, penka);
        barberShop.assignClient(marti, maria);
        barberShop.assignClient(marti, monica);
        barberShop.assignClient(marti, petya);
        barberShop.assignClient(viko, ganka);
        barberShop.assignClient(viko, penka);
        barberShop.assignClient(viko, maria);
        barberShop.assignClient(viko, monica);
        barberShop.deleteAllClientsFrom(viko);

        Collection<Client> clientsWithNoBarber = barberShop.getClientsWithNoBarber();
        printC(clientsWithNoBarber);
        System.out.println("============================");
        printC(barberShop.getClientsSortedByAgeDescAndBarbersStarsDesc());
        System.out.println("============================");
        printB(barberShop.getAllBarbersSortedWithClientsCountDesc());
        System.out.println("============================");
        printB(barberShop.getAllBarbersSortedWithStarsDescendingAndHaircutPriceAsc());

    }

    public static void printC(Collection<Client> collection) {
        for (Client c : collection) {
            System.out.println(c.name + " " + c.age);
        }
    }

    public static void printB(Collection<Barber> collection) {
        for (Barber b : collection) {
            System.out.println(b.name + " " + b.stars);
        }
    }
}
