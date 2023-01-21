package core;

import models.Route;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {
    Map<String, Route> routeById;
    Set<Route> routes;

    public MoovItImpl() {
        routeById = new LinkedHashMap<>();
        routes = new HashSet<>();
    }

    @Override
    public void addRoute(Route route) {
        String id = route.getId();
        if (routes.contains(route)) {
            throw new IllegalArgumentException();
        }
        routeById.put(id, route);
        routes.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        if (!routeById.containsKey(routeId)) {
            throw new IllegalArgumentException();
        }
        Route remove = routeById.remove(routeId);
        routes.remove(remove);
    }

    @Override
    public boolean contains(Route route) {
        return routes.contains(route);
    }

    @Override
    public int size() {
        return routeById.size();
    }

    @Override
    public Route getRoute(String routeId) {
        if (!routeById.containsKey(routeId)) {
            throw new IllegalArgumentException();
        }
        return routeById.get(routeId);
    }

    @Override
    public void chooseRoute(String routeId) {
        if (!routeById.containsKey(routeId)) {
            throw new IllegalArgumentException();
        }
        Route route = routeById.get(routeId);
        route.setPopularity(route.getPopularity() + 1);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        return routeById.values().stream()
                .filter(r -> {
                    int startIndex = r.getLocationPoints().indexOf(startPoint);
                    int endIndex = r.getLocationPoints().indexOf(endPoint);
                    return startIndex >= 0 && endIndex >= 0 && startIndex < endIndex;
                })
                .sorted((l, r) -> {
                    if (l.getIsFavorite() && !r.getIsFavorite()) {
                        return -1;
                    }
                    if (!l.getIsFavorite() && r.getIsFavorite()) {
                        return 1;
                    }
                    int lDistance = l.getLocationPoints().indexOf(endPoint) - l.getLocationPoints().indexOf(startPoint);
                    int rDistance = r.getLocationPoints().indexOf(endPoint) - r.getLocationPoints().indexOf(startPoint);
                    if (lDistance != rDistance) {
                        return Integer.compare(lDistance, rDistance);
                    }

                    return Integer.compare(r.getPopularity(), l.getPopularity());
                })
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return routeById.values().stream()
                .filter(r -> r.getIsFavorite() && r.getLocationPoints().indexOf(destinationPoint) > 0)
                .sorted((l, r) -> {
                    if (!l.getDistance().equals(r.getDistance())) {
                        return Double.compare(l.getDistance(), r.getDistance());
                    }
                    return Integer.compare(r.getPopularity(), l.getPopularity());
                })
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        return routeById.values().stream()
                .sorted((l, r) -> {
                    if (!l.getPopularity().equals(r.getPopularity())) {
                        return Integer.compare(r.getPopularity(), l.getPopularity());
                    }
                    if (!l.getDistance().equals(r.getDistance())) {
                        return Double.compare(l.getDistance(), r.getDistance());
                    }
                    return l.getLocationPoints().size() - r.getLocationPoints().size();
                })
                .limit(5)
                .collect(Collectors.toList());
    }
}
