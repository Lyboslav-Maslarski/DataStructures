package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    Map<String, Vehicle> vehiclesByID;

    Map<String, Set<Vehicle>> vehiclesByBrand;
    Map<String, Set<Vehicle>> sellersWithVehicles;
    Map<String, String> vehicleAndSeller;

    public VehicleRepositoryImpl() {
        vehiclesByID = new LinkedHashMap<>();
        vehiclesByBrand = new LinkedHashMap<>();
        sellersWithVehicles = new LinkedHashMap<>();
        vehicleAndSeller = new LinkedHashMap<>();
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        vehiclesByID.put(vehicle.getId(), vehicle);
        String brand = vehicle.getBrand();
        vehiclesByBrand.putIfAbsent(brand, new LinkedHashSet<>());
        vehiclesByBrand.get(brand).add(vehicle);
        sellersWithVehicles.putIfAbsent(sellerName, new LinkedHashSet<>());
        sellersWithVehicles.get(sellerName).add(vehicle);
        vehicleAndSeller.put(vehicle.getId(), sellerName);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if (!vehiclesByID.containsKey(vehicleId)) {
            throw new IllegalArgumentException();
        }
        Vehicle vehicle = vehiclesByID.remove(vehicleId);
        String brand = vehicle.getBrand();
        if (vehiclesByBrand.containsKey(brand)) {
            vehiclesByBrand.get(brand).remove(vehicle);
        }
        String seller = vehicleAndSeller.get(vehicleId);
        if (sellersWithVehicles.containsKey(seller)) {
            sellersWithVehicles.get(seller).remove(vehicle);
        }
        vehicleAndSeller.remove(vehicle.getId());
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
                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        if (!sellersWithVehicles.containsKey(sellerName)) {
            throw new IllegalArgumentException();
        }
        Set<Vehicle> vehicles = sellersWithVehicles.get(sellerName);
        if (vehicles.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return vehicles;
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return vehiclesByID.values().stream()
                .filter(vehicle -> vehicle.getPrice() >= lowerBound && vehicle.getPrice() <= upperBound)
                .sorted((l, r) -> Integer.compare(r.getHorsepower(), l.getHorsepower()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (size() == 0) {
            throw new IllegalArgumentException();
        }
        Map<String, List<Vehicle>> result = new HashMap<>();
        for (Map.Entry<String, Set<Vehicle>> entry : vehiclesByBrand.entrySet()) {
            Set<Vehicle> list = entry.getValue();
            if (!list.isEmpty()) {
                List<Vehicle> collect = list.stream().sorted(Comparator.comparingDouble(Vehicle::getPrice)).collect(Collectors.toList());
                result.put(entry.getKey(), collect);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return vehiclesByID.values().stream().sorted((l, r) -> {
            if (l.getHorsepower() == r.getHorsepower()) {
                if (l.getPrice() == r.getPrice()) {
                    return vehicleAndSeller.get(l.getId()).compareTo(vehicleAndSeller.get(r.getId()));
                }
                return Double.compare(l.getPrice(), r.getPrice());
            }
            return Integer.compare(r.getHorsepower(), l.getHorsepower());
        }).collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        if (!sellersWithVehicles.containsKey(sellerName)) {
            throw new IllegalArgumentException();
        }
        Set<Vehicle> vehicles = sellersWithVehicles.get(sellerName);
        if (vehicles.isEmpty()) {
            throw new IllegalArgumentException();
        }
        List<Vehicle> collect = vehicles.stream().sorted(Comparator.comparing(Vehicle::getPrice)).collect(Collectors.toList());
        Vehicle vehicle = collect.get(0);
        if (vehicle == null) {
            throw new IllegalArgumentException();
        }
        removeVehicle(vehicle.getId());
        return vehicle;
    }
}
