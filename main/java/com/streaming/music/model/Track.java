package com.streaming.music.model;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@ToString
@Data
@Table(name = "tracks", indexes = {
        @Index(name = "genre_of_track", columnList = "genre_of_track")
})
@Entity
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name_of_track", length = 100)
    private String nameOfTrack;

    @Column(name = "path_to_track", length = 100)
    private String pathToTrack;

    @ManyToOne
    @JoinColumn(name = "genre_of_track")
    private Genre genreOfTrack;

    @ManyToMany(mappedBy = "tracksOfPlaylists")
    @ToString.Exclude
    private List<Playlist> trackOfPlaylist = new ArrayList<>();

    @ManyToMany(mappedBy = "tracksOfAlbums")
    private Set<Album> trackOfAlbum ;

    @ManyToMany(mappedBy = "tracksOfExecutors")
    @ToString.Exclude
    private List<Executor> trackOfExecutors = new ArrayList<>();


   @ManyToMany(mappedBy = "tracks")
   @ToString.Exclude
   private List<User> user = new ArrayList<>();

}