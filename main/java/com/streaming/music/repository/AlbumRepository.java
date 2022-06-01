package com.streaming.music.repository;

import com.streaming.music.dto.AlbumData;
import com.streaming.music.model.Album;
import com.streaming.music.model.Executor;
import com.streaming.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Album getAlbumById(int id);
    Optional<Album> getAlbumByNameOfAlbum(String name);
    Optional<Integer> deleteAlbumById(int id);
    List<Album> getAllByAlbumsOFExecutor(Executor executor);

    List<Album> getAllByUsersAlbums(User user);


}