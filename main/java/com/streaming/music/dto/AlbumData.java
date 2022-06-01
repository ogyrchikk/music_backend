package com.streaming.music.dto;

import com.streaming.music.model.Genre;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumData {
    private Integer id;
    private String nameOfAlbum;
    private LocalDate dateOfCreate;
    private String pathToAvatar;
    private String descriptionText;
    private Genre genre;
}
