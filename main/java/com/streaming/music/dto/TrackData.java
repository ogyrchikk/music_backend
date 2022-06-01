package com.streaming.music.dto;

import com.streaming.music.model.Genre;
import lombok.Data;

@Data
public class TrackData {
    private int id;
    public String nameOfTrack;
    public String pathToTrack;
    public Genre genre;
}
