import java.util.*;
import java.util.stream.Collectors;

public class OlympicsImpl implements Olympics {
    private Map<Integer, Competitor> competitorMap;
    private Map<Integer, Competition> competitionMap;

    public OlympicsImpl() {
        competitorMap = new HashMap<>();
        competitionMap = new HashMap<>();
    }


    @Override
    public void addCompetitor(int id, String name) {
        if (competitorMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        competitorMap.put(id, new Competitor(id, name));
    }

    @Override
    public void addCompetition(int id, String name, int score) {
        if (competitionMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        competitionMap.put(id, new Competition(name, id, score));
    }

    @Override
    public void compete(int competitorId, int competitionId) {
        if (!competitionMap.containsKey(competitionId) || !competitorMap.containsKey(competitorId)) {
            throw new IllegalArgumentException();
        }
        Competitor competitor = competitorMap.get(competitorId);
        Competition competition = competitionMap.get(competitionId);
        competitor.setTotalScore(competitor.getTotalScore() + competition.getScore());
        competition.getCompetitors().add(competitor);
    }

    @Override
    public void disqualify(int competitionId, int competitorId) {
        if (!competitionMap.containsKey(competitionId) || !competitorMap.containsKey(competitorId)) {
            throw new IllegalArgumentException();
        }
        Competitor competitor = competitorMap.get(competitorId);
        Competition competition = competitionMap.get(competitionId);
        if (!competition.getCompetitors().contains(competitor)) {
            throw new IllegalArgumentException();
        }
        competitor.setTotalScore(competitor.getTotalScore() - competition.getScore());
        competition.getCompetitors().remove(competitor);
    }

    @Override
    public Iterable<Competitor> findCompetitorsInRange(long min, long max) {
        return competitorMap.values().stream()
                .filter(competitor -> competitor.getTotalScore() > min && competitor.getTotalScore() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Competitor> getByName(String name) {
        boolean nameDoesNotExist = true;
        for (Competitor competitor : competitorMap.values()) {
            if (competitor.getName().equals(name)) {
                nameDoesNotExist = false;
                break;
            }
        }
        if (nameDoesNotExist) {
            throw new IllegalArgumentException();
        }

        return competitorMap.values()
                .stream()
                .filter(competitor -> competitor.getName().equals(name))
                .sorted(Comparator.comparingInt(Competitor::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Competitor> searchWithNameLength(int minLength, int maxLength) {
        return competitorMap.values().stream()
                .filter(competitor -> competitor.getName().length() >= minLength && competitor.getName().length() <= maxLength)
                .sorted(Comparator.comparingInt(Competitor::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean contains(int competitionId, Competitor comp) {
        if (!competitionMap.containsKey(competitionId)) {
            throw new IllegalArgumentException();
        }
        Collection<Competitor> competitors = competitionMap.get(competitionId).getCompetitors();
        for (Competitor competitor : competitors) {
            if (competitor.getId() == comp.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int competitionsCount() {
        return competitionMap.size();
    }

    @Override
    public int competitorsCount() {
        return competitorMap.size();
    }

    @Override
    public Competition getCompetition(int id) {
        if (!competitionMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        return competitionMap.get(id);
    }
}
