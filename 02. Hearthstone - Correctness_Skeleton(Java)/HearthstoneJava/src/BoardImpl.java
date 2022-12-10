import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardImpl implements Board {
    private Map<String, Card> cardMap;

    public BoardImpl() {
        cardMap = new HashMap<>();
    }

    @Override
    public void draw(Card card) {
        String name = card.getName();
        if (contains(name)) {
            throw new IllegalArgumentException();
        }
        cardMap.put(name, card);
    }

    @Override
    public Boolean contains(String name) {
        return cardMap.containsKey(name);
    }

    @Override
    public int count() {
        return cardMap.size();
    }

    @Override
    public void play(String attackerCardName, String attackedCardName) {
        if (!cardMap.containsKey(attackerCardName) || !cardMap.containsKey(attackedCardName)) {
            throw new IllegalArgumentException();
        }
        Card attacker = cardMap.get(attackerCardName);
        Card attacked = cardMap.get(attackedCardName);
        if (attacker.getLevel() != attacked.getLevel()) {
            throw new IllegalArgumentException();
        }
        int initialHealth = attacked.getHealth();
        if (initialHealth <= 0) {
            return;
        }
        int currentHealth = initialHealth - attacker.getDamage();
        if (currentHealth <= 0) {
            attacker.setScore(attacker.getScore() + attacked.getLevel());
        }
        attacked.setHealth(currentHealth);
    }

    @Override
    public void remove(String name) {
        if (!cardMap.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        cardMap.remove(name);
    }

    @Override
    public void removeDeath() {
        cardMap.values().removeIf(card -> card.getHealth() <= 0);
    }

    @Override
    public Iterable<Card> getBestInRange(int start, int end) {
        return cardMap.values().stream()
                .filter(card -> card.getScore() >= start && card.getScore() <= end)
                .sorted(Comparator.comparingInt(Card::getLevel).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Card> listCardsByPrefix(String prefix) {
        return cardMap.values().stream()
                .filter(card -> card.getName().startsWith(prefix))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Card> searchByLevel(int level) {
        return cardMap.values().stream()
                .filter(card -> card.getLevel() == level)
                .sorted(Comparator.comparingInt(Card::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void heal(int health) {
        int lowestHealth = Integer.MAX_VALUE;
        Card lowestHPCard = null;
        for (Card card : cardMap.values()) {
            int currentCardHealth = card.getHealth();
            if (currentCardHealth < lowestHealth) {
                lowestHealth = currentCardHealth;
                lowestHPCard = card;
            }
        }
        assert lowestHPCard != null;
        lowestHPCard.setHealth(lowestHealth + health);
    }
}
