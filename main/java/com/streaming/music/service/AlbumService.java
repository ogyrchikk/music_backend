package com.streaming.music.service;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.streaming.music.dto.AlbumData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AlbumService {
    AlbumData saveAlbum(AlbumData albumData);
    int deleteAlbumById(Integer id);
    AlbumData getAlbumById(Integer id);
    AlbumData updateAlbum(AlbumData albumData);
    String savePicture(MultipartFile file) throws IOException;
    String unZipAndSavePicture(MultipartFile file,int idAlbum) throws IOException, InvalidDataException, UnsupportedTagException;
    AlbumData getAlbumByName(String name);
    int addTrackToAlbum(int idOfOAlbum, int... idOftrack);
    int removeTrackOfAlbum(int idOfOAlbum, int... idOftrack);
    List<AlbumData> getAlbumByExecutor(int id);
    List<AlbumData> getAll();
    int addAlbumToUser(int idAlbum,int idUser);
    int removeAlbumFromUser(int idAlbum,int idUser);
    int addAlbumToExecutor(int idAlbum, int idOfExecutor);
    int removeAlbumFromExecutor(int idAlbum, int idOfExecutor);


}
