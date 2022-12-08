package tripadministratorjava;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        TripAdministrator tripAdministrator = new TripAdministratorImpl();
        Company company1 = new Company("PeshoTravel", 2);
        Company company2 = new Company("GoshoTravel", 1);
        Trip trip1 = new Trip("1",1,Transportation.NONE,1);
        Trip trip2 = new Trip("2",1,Transportation.NONE,10);
        Trip trip3 = new Trip("3",1,Transportation.BUS,15);
        Trip trip4 = new Trip("4",1,Transportation.PLANE,20);
        Trip trip5 = new Trip("5",1,Transportation.PLANE,25);
        tripAdministrator.addCompany(company1);
        tripAdministrator.addTrip(company1,trip1);
        System.out.println(tripAdministrator.exist(trip1));
        tripAdministrator.executeTrip(company1,trip1);
        System.out.println(tripAdministrator.exist(trip1));
    }
}
