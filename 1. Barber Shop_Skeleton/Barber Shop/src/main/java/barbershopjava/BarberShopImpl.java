package barbershopjava;

import java.util.*;
import java.util.stream.Collectors;

public class BarberShopImpl implements BarberShop {
    private Map<String, Barber> barberMap;
    private Map<String, Client> clientMap;
    private Map<Barber, List<String>> barberWithClients;
    private Map<String, Client> clientWithBarberMap;
    private Map<String, Client> clientWithNoBarberMap;

    public BarberShopImpl() {
        this.barberMap = new HashMap<>();
        this.clientMap = new HashMap<>();
        this.barberWithClients = new HashMap<>();
        this.clientWithBarberMap = new HashMap<>();
        this.clientWithNoBarberMap = new HashMap<>();
    }

    @Override
    public void addBarber(Barber b) {
        String barberName = b.name;
        if (exist(b)) {
            throw new IllegalArgumentException();
        }
        barberMap.put(barberName, b);
        barberWithClients.put(b, new ArrayList<>());
    }

    @Override
    public void addClient(Client c) {
        String clientName = c.name;
        if (exist(c)) {
            throw new IllegalArgumentException();
        }
        clientMap.put(clientName, c);
        clientWithNoBarberMap.put(clientName, c);
    }

    @Override
    public boolean exist(Barber b) {
        return barberMap.containsKey(b.name);
    }

    @Override
    public boolean exist(Client c) {
        return clientMap.containsKey(c.name);
    }

    @Override
    public Collection<Barber> getBarbers() {
        return new ArrayList<>(barberMap.values());
    }

    @Override
    public Collection<Client> getClients() {
        return new ArrayList<>(clientMap.values());
    }

    @Override
    public void assignClient(Barber b, Client c) {
        if (!exist(b) || !exist(c)) {
            throw new IllegalArgumentException();
        }
        c.barber=b;
        barberWithClients.get(b).add(c.name);
        clientWithNoBarberMap.remove(c.name);
        clientWithBarberMap.put(c.name, c);
    }

    @Override
    public void deleteAllClientsFrom(Barber b) {
        if (!exist(b)) {
            throw new IllegalArgumentException();
        }
        barberWithClients.get(b).clear();
    }

    @Override
    public Collection<Client> getClientsWithNoBarber() {
        return new ArrayList<>(clientWithNoBarberMap.values());
    }

    @Override
    public Collection<Barber> getAllBarbersSortedWithClientsCountDesc() {
        return barberWithClients.keySet().stream()
                .sorted(Comparator.comparing(b -> barberWithClients.get(b).size()).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Barber> getAllBarbersSortedWithStarsDescendingAndHaircutPriceAsc() {
        return barberMap.values().stream().sorted((f, s) -> {
            int result = s.stars - f.stars;
            if (result == 0) {
                result = f.haircutPrice - s.haircutPrice;
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Collection<Client> getClientsSortedByAgeDescAndBarbersStarsDesc() {
        return clientWithBarberMap.values().stream().sorted((f,s)->{
            int result = s.age - f.age;
            if (result == 0) {
                result = s.barber.stars - f.barber.stars;
            }
            return result;
        }).collect(Collectors.toList());
    }
}
