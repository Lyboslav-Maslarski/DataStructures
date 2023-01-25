package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    Map<String, Vehicle> vehiclesByID;

    Map<String, List<Vehicle>> vehiclesByBrand;
    Set<String> sellers;

    public VehicleRepositoryImpl() {
        vehiclesByID = new LinkedHashMap<>();
        vehiclesByBrand = new HashMap<>();
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        if (contains(vehicle)) {
            String id = vehicle.getId();
            vehicle.setSeller(sellerName);
            vehiclesByID.put(vehicle.getId(), vehicle);
            String brand = vehicle.getBrand();
            vehiclesByBrand.putIfAbsent(brand, new ArrayList<>());
            vehiclesByBrand.get(brand).add(vehicle);
            sellers.add(sellerName);
        }
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if (vehiclesByID.containsKey(vehicleId)) {
            Vehicle vehicle = vehiclesByID.remove(vehicleId);
            vehiclesByBrand.get(vehicle.getBrand()).remove(vehicle);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int size() {
        return vehiclesByID.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return vehiclesByID.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return vehiclesByID.values().stream()
                .filter(v -> keywords.contains(v.getBrand()) || keywords.contains(v.getModel())
                             || keywords.contains(v.getLocation()) || keywords.contains(v.getColor()))
                .sorted((l, r) -> {
                    if (l.getIsVIP() && !r.getIsVIP()) {
                        return -1;
                    }
                    if (!l.getIsVIP() && r.getIsVIP()) {
                        return 1;
                    }
                    return Double.compare(l.getPrice(), r.getPrice());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        if (!sellers.contains(sellerName)) {
            throw new IllegalArgumentException();
        }
        return vehiclesByID.values().stream()
                .filter(vehicle -> vehicle.getSeller().equals(sellerName))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return vehiclesByID.values().stream()
                .filter(vehicle -> vehicle.getPrice() >= lowerBound && vehicle.getPrice() <= upperBound)
                .sorted(Comparator.comparing(Vehicle::getHorsepower).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (size() == 0) {
            throw new IllegalArgumentException();
        }
        vehiclesByBrand.values().forEach(vehicles -> vehicles.sort(Comparator.comparing(Vehicle::getPrice)));
        return vehiclesByBrand;
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return vehiclesByID.values().stream()
                .sorted((l, r) -> {
                    if (l.getHorsepower() == r.getHorsepower()) {
                        if (l.getPrice() == r.getPrice()) {
                            return l.getSeller().compareTo(r.getSeller());
                        }
                        return Double.compare(l.getPrice(), r.getPrice());
                    }
                    return Integer.compare(r.getHorsepower(), l.getHorsepower());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        if (!sellers.contains(sellerName)) {
            throw new IllegalArgumentException();
        }
        List<Vehicle> vehicles = vehiclesByID.values().stream()
                .filter(vehicle -> vehicle.getSeller().equals(sellerName))
                .sorted(Comparator.comparing(Vehicle::getPrice).reversed())
                .collect(Collectors.toList());
        if (vehicles.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Vehicle vehicle = vehicles.get(0);
        removeVehicle(vehicle.getId());
        return vehicle;
    }
}
