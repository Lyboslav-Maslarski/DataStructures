package core;

import models.Movie;

import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabaseImpl implements MovieDatabase {
    Map<String, Movie> moviesByID;
    Map<String, Integer> actorsAndMovieCount;

    public MovieDatabaseImpl() {
        this.moviesByID = new LinkedHashMap<>();
        actorsAndMovieCount = new HashMap<>();
    }

    @Override
    public void addMovie(Movie movie) {
        moviesByID.put(movie.getId(), movie);
        for (String actor : movie.getActors()) {
            actorsAndMovieCount.putIfAbsent(actor, 0);
            actorsAndMovieCount.put(actor, actorsAndMovieCount.get(actor) + 1);
        }
    }

    @Override
    public void removeMovie(String movieId) {
        if (!moviesByID.containsKey(movieId)) {
            throw new IllegalArgumentException();
        }
        Movie movie = moviesByID.remove(movieId);
        for (String actor : movie.getActors()) {
            actorsAndMovieCount.put(actor, actorsAndMovieCount.get(actor) - 1);
        }
    }

    @Override
    public int size() {
        return moviesByID.size();
    }

    @Override
    public boolean contains(Movie movie) {
        return moviesByID.containsKey(movie.getId());
    }

    @Override
    public Iterable<Movie> getMoviesByActor(String actorName) {
        List<Movie> result = moviesByID.values().stream()
                .filter(m -> m.getActors().contains(actorName))
                .sorted((l, r) -> {
                    if (l.getRating() == r.getRating()) {
                        return Integer.compare(r.getReleaseYear(), l.getReleaseYear());
                    }
                    return Double.compare(r.getRating(), l.getRating());
                })
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Movie> getMoviesByActors(List<String> actors) {
        List<Movie> result = moviesByID.values().stream()
                .filter(m -> new HashSet<>(m.getActors()).containsAll(actors))
                .sorted((l, r) -> {
                    if (l.getRating() == r.getRating()) {
                        return Integer.compare(r.getReleaseYear(), l.getReleaseYear());
                    }
                    return Double.compare(r.getRating(), l.getRating());
                })
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Movie> getMoviesByYear(Integer releaseYear) {
        return moviesByID.values().stream()
                .filter(m -> m.getReleaseYear() == releaseYear)
                .sorted((l, r) -> Double.compare(r.getRating(), l.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getMoviesInRatingRange(double lowerBound, double upperBound) {
        return moviesByID.values().stream()
                .filter(m -> m.getRating() >= lowerBound && m.getRating() <= upperBound)
                .sorted((l, r) -> Double.compare(r.getRating(), l.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getAllMoviesOrderedByActorPopularityThenByRatingThenByYear() {
        return moviesByID.values().stream()
                .sorted((l, r) -> {
                    int lActorsMovieCount = l.getActors().stream().mapToInt(a -> actorsAndMovieCount.get(a)).sum();
                    int rActorsMovieCount = r.getActors().stream().mapToInt(a -> actorsAndMovieCount.get(a)).sum();
                    if (lActorsMovieCount == rActorsMovieCount) {
                        if (l.getRating() == r.getRating()) {
                            return Integer.compare(r.getReleaseYear(), l.getReleaseYear());
                        }
                        return Double.compare(r.getRating(), l.getRating());
                    }
                    return Integer.compare(rActorsMovieCount, lActorsMovieCount);
                })
                .collect(Collectors.toList());
    }
}
