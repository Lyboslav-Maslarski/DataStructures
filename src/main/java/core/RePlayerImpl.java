package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {
    Map<String, Track> allTracksByID;
    Map<String, Map<String, Track>> albumsWithTracksByTitle;
    Map<String, Map<String, List<Track>>> artistWithAlbumsAndTracks;
    ArrayDeque<Track> listeningQueue;

    public RePlayerImpl() {
        this.allTracksByID = new HashMap<>();
        this.albumsWithTracksByTitle = new TreeMap<>();
        this.listeningQueue = new ArrayDeque<>();
        this.artistWithAlbumsAndTracks = new HashMap<>();
    }

    @Override
    public void addTrack(Track track, String album) {
        String id = track.getId();
        allTracksByID.put(id, track);
        albumsWithTracksByTitle.putIfAbsent(album, new HashMap<>());
        String title = track.getTitle();
        albumsWithTracksByTitle.get(album).put(title, track);
        String artist = track.getArtist();
        artistWithAlbumsAndTracks.putIfAbsent(artist, new HashMap<>());
        artistWithAlbumsAndTracks.get(artist).putIfAbsent(album, new ArrayList<>());
        artistWithAlbumsAndTracks.get(artist).get(album).add(track);
    }


    @Override
    public void removeTrack(String trackTitle, String albumName) {
        if (!albumsWithTracksByTitle.containsKey(albumName)) {
            throw new IllegalArgumentException();
        }
        Track track = albumsWithTracksByTitle.get(albumName).remove(trackTitle);
        if (track == null) {
            throw new IllegalArgumentException();
        }
        allTracksByID.remove(track.getId());
        listeningQueue.remove(track);
        String artist = track.getArtist();
        if (artistWithAlbumsAndTracks.containsKey(artist)) {
            Map<String, List<Track>> map = artistWithAlbumsAndTracks.get(artist);
            if (map.containsKey(albumName)) {
                map.get(albumName).remove(track);
            }
        }
    }

    @Override
    public boolean contains(Track track) {
        return allTracksByID.containsKey(track.getId());
    }

    @Override
    public int size() {
        return allTracksByID.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        if (!albumsWithTracksByTitle.containsKey(albumName)) {
            throw new IllegalArgumentException();
        }

        Track track = albumsWithTracksByTitle.get(albumName).get(title);
        if (track == null) {
            throw new IllegalArgumentException();
        }

        return track;
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        if (!albumsWithTracksByTitle.containsKey(albumName)) {
            throw new IllegalArgumentException();
        }
        List<Track> result = albumsWithTracksByTitle.get(albumName).values().stream()
                .sorted((l, r) -> Integer.compare(r.getPlays(), l.getPlays()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        Track track = getTrack(trackName, albumName);
        listeningQueue.offer(track);
    }

    @Override
    public Track play() {
        if (listeningQueue.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Track track = listeningQueue.poll();
        int currentPlays = track.getPlays();
        track.setPlays(currentPlays + 1);
        return track;
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        return allTracksByID.values().stream()
                .filter(t -> t.getDurationInSeconds() >= lowerBound && t.getDurationInSeconds() <= upperBound)
                .sorted((l, r) -> {
                    if (l.getDurationInSeconds() == r.getDurationInSeconds()) {
                        return Integer.compare(r.getPlays(), l.getPlays());
                    }
                    return Integer.compare(l.getDurationInSeconds(), r.getDurationInSeconds());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        ArrayList<Track> result = new ArrayList<>();
        for (Map<String, Track> map : albumsWithTracksByTitle.values()) {
            List<Track> collect = map.values().stream().sorted((l, r) -> {
                if (l.getPlays() == r.getPlays()) {
                    return Integer.compare(r.getDurationInSeconds(), l.getDurationInSeconds());
                }
                return Integer.compare(r.getPlays(), l.getPlays());
            }).collect(Collectors.toList());
            result.addAll(collect);
        }
        return result;
    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {

        Map<String, List<Track>> result = new LinkedHashMap<>();
        Map<String, List<Track>> map = artistWithAlbumsAndTracks.get(artistName);
        if (map == null) {
            throw new IllegalArgumentException();
        }
        for (Map.Entry<String, List<Track>> entry : map.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
