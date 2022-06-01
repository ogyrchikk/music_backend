package com.streaming.music.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "executors")
@Entity
public class Executor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name_of_executor", length = 100)
    private String nameOfExecutor;

    @Column(name = "path_to_avatar", length = 100)
    private String pathToAvatar;

    @Column(name = "date_of_formation")
    private LocalDate dateOfFormation;

    @Column(name = "disbandment_date")
    private LocalDate disbandmentDate;

    @Lob
    @Column(name = "description_ext")
    private String descriptionExt;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tracks_of_executors",
            joinColumns = @JoinColumn(name = "id_of_executor"),
            inverseJoinColumns = @JoinColumn(name = "id_of_track")
    )
    @ToString.Exclude
    private List<Track> tracksOfExecutors = new ArrayList<>();

    @ManyToMany(mappedBy = "albumsOFExecutor")
    @ToString.Exclude
    private List<Album> executorsAlbums = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "genres_of_executors",
            joinColumns = @JoinColumn(name = "id_of_executor"),
            inverseJoinColumns = @JoinColumn(name = "id_of_genre")
    )
    @ToString.Exclude
    private List<Genre> genresOfExecutors = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "users_executors",
            joinColumns = @JoinColumn(name = "id_of_executor"),
            inverseJoinColumns = @JoinColumn(name = "id_of_user"))
    private List<User> usersExecutors =new ArrayList<>();

}