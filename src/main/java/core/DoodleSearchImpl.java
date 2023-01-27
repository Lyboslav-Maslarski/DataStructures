package core;

import models.Doodle;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DoodleSearchImpl implements DoodleSearch {
    Map<String, Doodle> doodles;
    Map<String, Doodle> doodlesByTitle;

    public DoodleSearchImpl() {
        this.doodles = new LinkedHashMap<>();
        this.doodlesByTitle = new HashMap<>();
    }

    @Override
    public void addDoodle(Doodle doodle) {
        doodles.put(doodle.getId(), doodle);
        doodlesByTitle.put(doodle.getTitle(), doodle);
    }

    @Override
    public void removeDoodle(String doodleId) {
        Doodle result = doodles.remove(doodleId);
        if (result == null) {
            throw new IllegalArgumentException();
        }
        doodlesByTitle.remove(result.getTitle());
    }

    @Override
    public int size() {
        return doodles.size();
    }

    @Override
    public boolean contains(Doodle doodle) {
        return doodles.containsKey(doodle.getId());
    }

    @Override
    public Doodle getDoodle(String id) {
        Doodle result = doodles.get(id);
        if (result == null) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public double getTotalRevenueFromDoodleAds() {
        return doodles.values().stream()
                .filter(Doodle::getIsAd)
                .mapToDouble(doodle -> doodle.getVisits() * doodle.getRevenue())
                .sum();
    }

    @Override
    public void visitDoodle(String title) {
        Doodle result = doodlesByTitle.get(title);
        if (result == null) {
            throw new IllegalArgumentException();
        }
        int visits = result.getVisits();
        result.setVisits(visits + 1);
    }

    @Override
    public Iterable<Doodle> searchDoodles(String searchQuery) {
        return doodles.values().stream()
                .filter(doodle -> doodle.getTitle().contains(searchQuery))
                .sorted((l, r) -> {
                    if (l.getIsAd() && !r.getIsAd()) {
                        return -1;
                    }
                    if (!l.getIsAd() && r.getIsAd()) {
                        return 1;
                    }
                    int lIndex = l.getTitle().indexOf(searchQuery);
                    int rIndex = r.getTitle().indexOf(searchQuery);
                    if (lIndex == rIndex) {
                        return r.getVisits() - l.getVisits();
                    }
                    return lIndex - rIndex;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getDoodleAds() {
        return doodles.values().stream()
                .filter(Doodle::getIsAd)
                .sorted((l, r) -> {
                    if (l.getRevenue() == r.getRevenue()) {
                        return r.getVisits() - l.getVisits();
                    }
                    return Double.compare(r.getRevenue(), l.getRevenue());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getTop3DoodlesByRevenueThenByVisits() {
        return doodles.values().stream()
                .sorted((l, r) -> {
                    if (l.getRevenue() == r.getRevenue()) {
                        return r.getVisits() - l.getVisits();
                    }
                    return Double.compare(r.getRevenue(), l.getRevenue());
                })
                .limit(3)
                .collect(Collectors.toList());
    }
}
