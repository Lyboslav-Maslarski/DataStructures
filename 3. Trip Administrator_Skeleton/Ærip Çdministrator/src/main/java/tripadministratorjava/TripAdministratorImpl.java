package tripadministratorjava;

import java.util.*;
import java.util.stream.Collectors;

public class TripAdministratorImpl implements TripAdministrator {
    private Map<String, Company> companyMap;
    private Map<String, Trip> tripMap;
    private Map<String, List<Trip>> companyWithTrips;

    public TripAdministratorImpl() {
        this.companyMap = new LinkedHashMap<>();
        this.tripMap = new LinkedHashMap<>();
        this.companyWithTrips = new LinkedHashMap<>();
    }

    @Override
    public void addCompany(Company c) {
        String companyName = c.name;
        if (companyMap.containsKey(companyName)) {
            throw new IllegalArgumentException();
        }
        companyMap.put(companyName, c);
        companyWithTrips.put(companyName, new ArrayList<>());
    }

    @Override
    public void addTrip(Company c, Trip t) {
        if (!exist(c)) {
            throw new IllegalArgumentException();
        }
        String companyName = c.name;
        List<Trip> trips = companyWithTrips.get(companyName);
        if (trips.size() >= c.tripOrganizationLimit) {
            throw new IllegalArgumentException();
        }
        if (exist(t)) {
            throw new IllegalArgumentException();

        }
        trips.add(t);
        tripMap.put(t.id, t);

    }

    @Override
    public boolean exist(Company c) {
        return companyMap.containsKey(c.name);
    }

    @Override
    public boolean exist(Trip t) {
        return tripMap.containsKey(t.id);
    }

    @Override
    public void removeCompany(Company c) {
        if (!exist(c)) {
            throw new IllegalArgumentException();
        }
        for (Trip trip : companyWithTrips.get(c.name)) {
            tripMap.remove(trip.id);
        }
        companyMap.remove(c.name);
        companyWithTrips.remove(c.name);
    }

    @Override
    public Collection<Company> getCompanies() {
        return new ArrayList<>(companyMap.values());
    }

    @Override
    public Collection<Trip> getTrips() {
        return new ArrayList<>(tripMap.values());
    }

    @Override
    public void executeTrip(Company c, Trip t) {
        if (!exist(c) || !exist(t)) {
            throw new IllegalArgumentException();
        }

        List<Trip> trips = companyWithTrips.get(c.name);

        boolean tripNotFound = true;
        for (Trip trip : trips) {
            if (trip.id.equals(t.id)) {
                tripNotFound = false;
                break;
            }
        }

        if (tripNotFound) {
            throw new IllegalArgumentException();
        }else {
            trips.remove(t);
            tripMap.remove(t.id);
        }
    }

    @Override
    public Collection<Company> getCompaniesWithMoreThatNTrips(int n) {
        List<Company> companies = new ArrayList<>();

        for (Map.Entry<String, List<Trip>> entry : companyWithTrips.entrySet()) {
            if (entry.getValue().size() > n) {
                companies.add(companyMap.get(entry.getKey()));
            }
        }
        return companies;
    }

    @Override
    public Collection<Trip> getTripsWithTransportationType(Transportation t) {
        return tripMap.values().stream().filter(trip -> trip.transportation.ordinal() == t.ordinal()).collect(Collectors.toList());
    }

    @Override
    public Collection<Trip> getAllTripsInPriceRange(int lo, int hi) {
        return tripMap.values().stream().filter(trip -> trip.price >= lo && trip.price <= hi).collect(Collectors.toList());
    }

}
