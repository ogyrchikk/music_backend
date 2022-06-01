package com.streaming.music.service;


import com.streaming.music.dto.AlbumData;
import com.streaming.music.dto.ExecutorData;
import com.streaming.music.model.*;
import com.streaming.music.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.streaming.music.repository.UserRepository;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Log
@Service("executor_service")
@Transactional
public class MainExecutorService implements ExecutorService  {
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    @Value("${upload.path.executor}")
    private String uploadPath;
    @Resource(name="genre_service")
    private GenreService genreService;
    @Resource(name="album_service")
    private AlbumService albumService;
    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final ExecutorRepository executorRepository;

    public MainExecutorService(UserRepository userRepository, TrackRepository trackRepository, GenreService genreService, GenreRepository genreRepository, AlbumRepository albumRepository, ExecutorRepository executorRepository) {
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.genreService = genreService;
        this.genreRepository = genreRepository;
        this.albumRepository = albumRepository;
        this.executorRepository = executorRepository;
    }

    @Override
    public ExecutorData saveExecutor (ExecutorData executorData) {
        Executor executor = populateExecutorEntity(executorData);
        return populateExecutorData(executorRepository.save(executor));
    }

    @Override
    public int deleteExecutor(Integer id) {
        return executorRepository.deleteExecutorById(id).orElseThrow(() -> new EntityNotFoundException("Executor not found"));
    }

    @Override
    public ExecutorData getExecutorById(Integer id) {
        return populateExecutorData(executorRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Executor not found")));
    }
    @Override
    public ExecutorData getExecutorByName(String name) {
        return populateExecutorData(executorRepository.getByNameOfExecutor(name).orElseThrow(()->new EntityNotFoundException("Executor not found")));
    }

    @Override
    public int addExecutorToUser(int idExecutor, int idUser) {
        Executor executor = executorRepository.getById(idExecutor);
        List<User> users = userRepository.getAllByUsersExecutors(executor);
        users.add(userRepository.getById(idUser));
        executor.setUsersExecutors(users);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int removeExecutorFromUser(int idExecutor, int idUser) {
        Executor executor = executorRepository.getById(idExecutor);
        List<User> users = userRepository.getAllByUsersExecutors(executor);
        users.remove(userRepository.getById(idUser));
        executor.setUsersExecutors(users);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public ExecutorData updateExecutor(ExecutorData executorData) {
        Executor executor = populateExecutorEntity(executorData);
        return populateExecutorData(executorRepository.save(executor));
    }


    private ExecutorData populateExecutorData(final Executor executor){
        ExecutorData executorData = new ExecutorData();
        executorData.setNameOfExecutor(executor.getNameOfExecutor());
        executorData.setId(executor.getId());
        executorData.setPathToAvatar(executor.getPathToAvatar());
        executorData.setDescriptionExt(executor.getDescriptionExt());
        executorData.setDisbandmentDate(executor.getDisbandmentDate());
        executorData.setDateOfFormation(executor.getDateOfFormation());
        return executorData;
    }

    public Executor populateExecutorEntity(ExecutorData executorData){
        Executor executor = new Executor();
        executor.setNameOfExecutor(executorData.getNameOfExecutor());
        try{
            executor.setId(executorData.getId());
        }catch (Exception ignored){

        }
        executor.setPathToAvatar(executorData.getPathToAvatar());
        executor.setDescriptionExt(executorData.getDescriptionExt());
        executor.setDisbandmentDate(executorData.getDisbandmentDate());
        executor.setDateOfFormation(executorData.getDateOfFormation());
        return executor;
    }

    @Override
    public List<ExecutorData> getAll(Pageable pageable) {
        Page<Executor> executors=executorRepository.findAll(pageable);
        List<ExecutorData> executorData = new ArrayList<>();
        for(Executor w:executors){
            executorData.add(populateExecutorData(w));
        }
        return executorData;
    }

    @Override
    public List<ExecutorData> getAllExecutor() {
        List<ExecutorData> executorData= new ArrayList<>();
        List<Executor> executors=executorRepository.findAll();
        for (Executor w:
             executors) {
            executorData.add(populateExecutorData(w));
        }
        return executorData;
    }

    @Override
    public int getCountPages(Pageable pageable) {
        Page<Executor> executors=executorRepository.findAll(pageable);
        return executors.getTotalPages();
    }

    @Override
    public int addAlbumToExecutor(int idOfAlbum, int idOfExecutor) {
        Album album=albumRepository.getAlbumById(idOfAlbum);
        List<Executor> executors = executorRepository.getAllByExecutorsAlbums(albumRepository.getAlbumById(idOfAlbum));
        executors.add(executorRepository.getById(idOfExecutor));
        album.setAlbumsOFExecutor((List<Executor>) executors);
        albumRepository.save(album);
        return 1;
    }

    @Override
    public int addGenresToExecutor(int idOfExecutor, int... idOfGenres) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Genre> genres = genreRepository.getAllByGenresExecutor(idOfExecutor);
        for (int id:idOfGenres) {
            genres.add(genreRepository.getById(id));
        }
        executor.setGenresOfExecutors(genres);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int removeGenresFromExecutor(int idOfExecutor, int... idOfGenres) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Genre> genres = genreRepository.getAllByGenresExecutor(idOfExecutor);
        for (int id:idOfGenres) {
            genres.remove(genreRepository.getById(id));
        }
        executor.setGenresOfExecutors(genres);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public long getCount() {
        return executorRepository.count();
    }



//    @Override
//    public int addGenreToExecutor(int idOfExecutor, int... idOfGenre) {
//        Executor executor = executorRepository.getById(idOfExecutor);
//        List<Genre> genres= genreRepository.getAllByGenresExecutor(executorRepository.getById(idOfExecutor));
//        for (int w:
//             idOfGenre) {
//            genres.add(genreRepository.getGenreById(w).orElseThrow());
//        }
//        executor.setGenresOfExecutors(genres);
//        executorRepository.save(executor);
//        return 1;
//    }


    public String savePicture(MultipartFile file) throws IOException {
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
    public ExecutorData findExecutorByNameOfExecutor(String name) {
        return populateExecutorData(executorRepository.getExecutorByNameOfExecutor(name).orElseThrow(()->new EntityNotFoundException("Executor not found")));
    }

    @Override
    public int addTrackToExecutor(int idOfExecutor, int idOfTrack) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Track> list = new ArrayList<>();
        list.add(trackRepository.getTrackById(idOfTrack));
        executor.setTracksOfExecutors((List<Track>) list);
        executorRepository.save(executor);
        return 1;
    }

    private AlbumData populateAlbumData(final Album album){
        AlbumData albumData = new AlbumData();
        albumData.setId(album.getId());
        albumData.setNameOfAlbum(album.getNameOfAlbum());
        albumData.setPathToAvatar(album.getPathToAvatar());
        albumData.setDateOfCreate(album.getDateOfCreate());
        albumData.setDescriptionText(album.getDescriptionText());
        return albumData;
    }

    private Album populateAlbumEntity(AlbumData albumData){
        Album album = new Album();
        album.setNameOfAlbum(albumData.getNameOfAlbum());
        try{
            album.setId(albumData.getId());
        }catch (Exception ignored){

        }
        album.setDateOfCreate(albumData.getDateOfCreate());
        album.setDescriptionText(albumData.getDescriptionText());
        album.setPathToAvatar(albumData.getPathToAvatar());
        return album;
    }
}
