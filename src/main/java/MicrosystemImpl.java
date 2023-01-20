import java.util.*;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {
    Map<Integer, Computer> computersByNumber;
    Map<Brand, Set<Computer>> computerByBrand;

    public MicrosystemImpl() {
        this.computersByNumber = new HashMap<>();
        this.computerByBrand = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        int number = computer.getNumber();
        if (computersByNumber.containsKey(number)) {
            throw new IllegalArgumentException();
        }
        computersByNumber.put(number, computer);
        computerByBrand.putIfAbsent(computer.getBrand(), new HashSet<>());
        computerByBrand.get(computer.getBrand()).add(computer);
    }

    @Override
    public boolean contains(int number) {
        return computersByNumber.containsKey(number);
    }

    @Override
    public int count() {
        return computersByNumber.size();
    }

    @Override
    public Computer getComputer(int number) {
        if (!computersByNumber.containsKey(number)) {
            throw new IllegalArgumentException();
        }
        return computersByNumber.get(number);
    }

    @Override
    public void remove(int number) {
        if (!computersByNumber.containsKey(number)) {
            throw new IllegalArgumentException();
        }
        Computer computer = computersByNumber.remove(number);
        computerByBrand.get(computer.getBrand()).remove(computer);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        if (!computerByBrand.containsKey(brand)) {
            throw new IllegalArgumentException();
        }
        computerByBrand.get(brand).forEach(computer -> computersByNumber.remove(computer.getNumber()));
        computerByBrand.remove(brand);
    }

    @Override
    public void upgradeRam(int ram, int number) {
        if (!computersByNumber.containsKey(number)) {
            throw new IllegalArgumentException();
        }
        Computer computer = computersByNumber.get(number);
        if (computer.getRAM() < ram) {
            computer.setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        if (computerByBrand.containsKey(brand)) {
            return computerByBrand.get(brand).stream()
                    .sorted((l, r) -> Double.compare(r.getPrice(), l.getPrice()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return computersByNumber.values().stream()
                .filter(computer -> computer.getScreenSize() == screenSize)
                .sorted((l, r) -> r.getNumber() - l.getNumber())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return computersByNumber.values().stream()
                .filter(computer -> computer.getColor().equals(color))
                .sorted((l, r) -> Double.compare(r.getPrice(), l.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return computersByNumber.values().stream()
                .filter(computer -> computer.getPrice() >= minPrice && computer.getPrice() <= maxPrice)
                .sorted((l, r) -> Double.compare(r.getPrice(), l.getPrice()))
                .collect(Collectors.toList());
    }
}
