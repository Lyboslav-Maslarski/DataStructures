import java.util.*;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private Map<Integer, Battlecard> cardsWithIds;
    private Map<CardType, Set<Battlecard>> cardsByType;

    public RoyaleArena() {
        this.cardsWithIds = new LinkedHashMap<>();
        this.cardsByType = new HashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        cardsWithIds.putIfAbsent(card.getId(), card);
        cardsByType.putIfAbsent(card.getType(), new TreeSet<>(Battlecard::compareTo));
        cardsByType.get(card.getType()).add(card);
    }

    @Override
    public boolean contains(Battlecard card) {
        return cardsWithIds.containsKey(card.getId());
    }

    @Override
    public int count() {
        return cardsWithIds.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        Battlecard battlecard = cardsWithIds.get(id);

        if (battlecard == null) {
            throw new IllegalArgumentException();
        }

        battlecard.setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        Battlecard battlecard = cardsWithIds.get(id);

        if (battlecard == null) {
            throw new UnsupportedOperationException();
        }

        return battlecard;
    }

    @Override
    public void removeById(int id) {
        Battlecard battlecard = cardsWithIds.remove(id);

        if (battlecard == null) {
            throw new UnsupportedOperationException();
        }

        cardsByType.get(battlecard.getType()).remove(battlecard);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        Set<Battlecard> battlecards = cardsByType.get(type);

        if (battlecards == null) {
            throw new UnsupportedOperationException();
        }

        return battlecards;
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> battlecards = cardsByType.get(type);

        if (battlecards == null) {
            throw new UnsupportedOperationException();
        }

        List<Battlecard> collect = battlecards.stream()
                .filter(battlecard -> battlecard.getDamage() > lo && battlecard.getDamage() < hi)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return collect;
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battlecards = cardsByType.get(type);

        if (battlecards == null || battlecards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        List<Battlecard> collect = battlecards.stream()
                .filter(battlecard -> battlecard.getDamage() <= damage)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return collect;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        List<Battlecard> collect = cardsWithIds.values().stream()
                .filter(battlecard -> battlecard.getName().equals(name))
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).reversed().thenComparingInt(Battlecard::getId))
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return collect;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        List<Battlecard> collect = cardsWithIds.values().stream()
                .filter(battlecard -> battlecard.getName().equals(name) && battlecard.getSwag() >= lo && battlecard.getSwag() < hi)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).reversed().thenComparingInt(Battlecard::getId))
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return collect;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Battlecard> battlecardMap = new LinkedHashMap<>();
        for (Battlecard battlecard : cardsWithIds.values()) {
            String name = battlecard.getName();
            if (battlecardMap.containsKey(name)) {
                double oldSwag = battlecardMap.get(name).getSwag();
                double newSwag = battlecard.getSwag();
                if (newSwag >= oldSwag) {
                    battlecardMap.put(name, battlecard);
                }
            } else {
                battlecardMap.put(name, battlecard);
            }
        }
        return battlecardMap.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.count()) {
            throw new UnsupportedOperationException();
        }
        List<Battlecard> collect = cardsWithIds.values().stream()
                .sorted(Comparator.comparingDouble(Battlecard::getSwag)).collect(Collectors.toList());

        return collect.stream()
                .limit(n)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).thenComparingInt(Battlecard::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        return cardsWithIds.values().stream()
                .filter(battlecard -> battlecard.getSwag() >= lo && battlecard.getSwag() <= hi)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return cardsWithIds.values().iterator();
    }
}
