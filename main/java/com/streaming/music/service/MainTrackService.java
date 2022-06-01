package com.streaming.music.service;

import com.streaming.music.dto.TrackData;
import com.streaming.music.model.*;
import com.streaming.music.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("trackService")
@Transactional
public class MainTrackService implements TrackService{

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final ExecutorRepository executorRepository;
    private final AlbumRepository albumRepository;

    private final UserRepository userRepository;
    @Value("${upload.path.track}")
    private String uploadPath;

    public MainTrackService(PlaylistRepository playlistRepository, TrackRepository trackRepository, ExecutorRepository executorRepository, AlbumRepository albumRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
        this.executorRepository = executorRepository;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TrackData getTrackByName(String name) {
        return populateTrackData(trackRepository.getTrackByNameOfTrack(name).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackById(int id) {
        return populateTrackData(trackRepository.getTrackById(id));
    }

    @Override
    public TrackData addTrack(TrackData trackdata) {
        Track track = populateTrackEntity(trackdata);
        return populateTrackData(trackRepository.save(track));
    }

    @Override
    public TrackData updateTrack(TrackData trackData) {
        Track track = populateTrackEntity(trackData);
        return populateTrackData(trackRepository.save(track));
    }

    @Override
    public int deleteTrackById(int id) {
        trackRepository.deleteById(id);
        return  1;
    }

    private TrackData populateTrackData(Track track){
        TrackData trackData = new TrackData();
        trackData.setNameOfTrack(track.getNameOfTrack());
        trackData.setPathToTrack(track.getPathToTrack());
        trackData.setId(track.getId());
        trackData.setGenre(track.getGenreOfTrack());
        return trackData;
    }
    private Track populateTrackEntity(TrackData trackData){
        Track track = new Track();
        track.setNameOfTrack(trackData.getNameOfTrack());
        track.setPathToTrack(trackData.getPathToTrack());
        track.setGenreOfTrack(trackData.getGenre());
        try{
            track.setId(trackData.getId());
        }catch (Exception ignored){

        }
        return track;
    }
    public String saveTrack(MultipartFile file) throws IOException {
        String resultFilename = null;
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            return resultFilename;
        }
        return resultFilename;
    }

    @Override
    public TrackData getTrackByIdAndTrackOfAlbum(Integer id, List<Album> album) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfAlbumIn(id, album).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfAlbum(String name, List<Album> album) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfAlbumIn(name, album).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByIdAndTrackOfPlaylist(Integer id, List<Playlist> playlist) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfPlaylistIn(id, playlist).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfPlaylist(String name, List<Playlist> playlist) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfPlaylistIn(name, playlist).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByIdAndTrackOfExecutorsIn(Integer id, List<Executor> trackOfExecutors) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfExecutorsIn(id, trackOfExecutors).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfExecutorsIn(String nameOfTrack, List<Executor> trackOfExecutors) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfExecutorsIn(nameOfTrack, trackOfExecutors).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public  List<TrackData> getAll(Pageable pageable) {
        Page<Track> tracks=trackRepository.findAll( pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }
    @Override
    public  List<TrackData> getAllTrack() {
        List<Track> track= trackRepository.findAll();
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:track ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByGenreOfTrack(Genre genre, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByGenreOfTrack( genre, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByTrackOfExecutors(Executor executor, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByTrackOfExecutors( executor, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByTrackOfPlaylist(Playlist playlist, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByTrackOfPlaylist( playlist, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> getAllByTrackOfAlbum(int id) {
        List<TrackData> trackData = new ArrayList<>();
        List<Track> tracks =trackRepository.getAllByTrackOfAlbum(albumRepository.getAlbumById(id));
        for (Track w:tracks) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> getAllByTrackOfPlaylist( int id) {
        List<TrackData> trackData = new ArrayList<>();
        List<Track> tracks =trackRepository.getAllByTrackOfPlaylist(playlistRepository.getById(id));
        for (Track w:tracks) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public int addTrackToExecutor(int idOfTrack, int idOfExecutor) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Track> tracks = trackRepository.getAllByTrackOfExecutors(executor);
        tracks.add(trackRepository.getById(idOfTrack));
        executor.setTracksOfExecutors(tracks);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int removeTrackFromExecutor(int idOfTrack, int idOfExecutor) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Track> tracks = trackRepository.getAllByTrackOfExecutors(executor);
        tracks.remove(trackRepository.getById(idOfTrack));
        executor.setTracksOfExecutors(tracks);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int addTrackToUser(int idOfTrack, int idOfUser) {
        User user = userRepository.getById(idOfUser);
        List<Track> tracks = trackRepository.getAllByUser(user);
        tracks.add(trackRepository.getTrackById(idOfTrack));
        user.setTracks(tracks);
        userRepository.save(user);
        return 1;
    }

    @Override
    public int removeTrackFromUser(int idOfTrack, int idOfUser) {
        User user = userRepository.getById(idOfUser);
        List<Track> tracks = trackRepository.getAllByUser(user);
        tracks.remove(trackRepository.getTrackById(idOfTrack));
        user.setTracks(tracks);
        userRepository.save(user);
        return 1;
    }


}
