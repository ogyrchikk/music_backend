package com.streaming.music.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.streaming.music.dto.AlbumData;
import com.streaming.music.service.AlbumService;
import com.streaming.music.service.ExecutorService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
@CrossOrigin
@RestController
public class AlbumController {

    @Resource(name="album_service")
    private AlbumService albumService;

    @Resource(name="executor_service")
    private ExecutorService executorService;

    @Value("${upload.path.album}")
    private String uploadPath;
    @PostMapping("/album")
    public ResponseEntity<Object> createAlbum(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws IOException, InvalidDataException, UnsupportedTagException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        AlbumData albumData = gson.fromJson(data, AlbumData.class);
        AlbumData a = new AlbumData();
        a.setDescriptionText(albumData.getDescriptionText());
        a.setDateOfCreate(albumData.getDateOfCreate());
        a.setNameOfAlbum(albumData.getNameOfAlbum());
        a.setGenre(albumData.getGenre());
        AlbumData album =albumService.saveAlbum(a);
        String image =albumService.unZipAndSavePicture(file,album.getId());
        album.setPathToAvatar(image);
        int i = albumService.saveAlbum(album).getId();
        System.out.println("id="+i);
        return ResponseEntity.ok().body(i);
    }

    @GetMapping("/album")
    public ResponseEntity<Object> getAlbum(@RequestParam("id") int id){
        AlbumData albumData = new AlbumData();
        try{
            albumData = albumService.getAlbumById(id);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(albumData);
    }

    @PatchMapping("/album")
    public ResponseEntity<Object> updateAlbum(@RequestParam("json") String data, @RequestParam("file") MultipartFile file)throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        AlbumData albumData = gson.fromJson(data, AlbumData.class);
        try{
            AlbumData a=albumService.getAlbumById(albumData.getId());
        }catch (EntityNotFoundException ee){

        }
        AlbumData a = new AlbumData();
        a.setId(albumData.getId());
        a.setNameOfAlbum(albumData.getNameOfAlbum());
        a.setDescriptionText(albumData.getDescriptionText());
        a.setDateOfCreate(albumData.getDateOfCreate());
        try{
            String resultFilename =albumService.savePicture(file);
            a.setPathToAvatar(uploadPath + "/" + resultFilename);
        }catch (IOException exx){
            return ResponseEntity.badRequest().body(exx.getMessage());
        }
        albumService.saveAlbum(a);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/album")
    public ResponseEntity<Object> deleteAlbum(@RequestParam("id") int id){
        AlbumData a;
        try{
            a = albumService.getAlbumById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.ok().body(ex.getMessage());
        }
        albumService.deleteAlbumById(id);
        return ResponseEntity.ok().body("Album deleted");
    }

    @GetMapping("/albumExecutor")
    public ResponseEntity<Object> albumsExecutor(@RequestParam("idExecutor") int id){
        List<AlbumData> albumData =albumService.getAlbumByExecutor(id);
        return ResponseEntity.ok().body(albumData);
    }

    @GetMapping(value = "/imageAlbum")
    public ResponseEntity<Object> getImage(@RequestParam ("id") int id) throws IOException {
        AlbumData albumData = new AlbumData();
        try {
            albumData = albumService.getAlbumById(id);
        }catch (EntityNotFoundException e){
            ResponseEntity.ok().body(e.getMessage());
        }
        byte[] fileContent = FileUtils.readFileToByteArray(new File(albumData.getPathToAvatar()));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return ResponseEntity.ok().body(encodedString);
    }

    @GetMapping("/allalbum")
    public ResponseEntity<Object> getAll(){
        List<AlbumData> albumData = albumService.getAll();
        return ResponseEntity.ok().body(albumData);
    }

    @PostMapping("/addAlbumExecutor")
    public ResponseEntity<Object> addAlbumExecutor(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfExecutor") int idOfExecutor){
        return ResponseEntity.ok().body(albumService.addAlbumToExecutor(idOfAlbum,idOfExecutor));
    }

    @DeleteMapping("/removeAlbumExecutor")
    public ResponseEntity<Object> removeAlbumExecutor(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfExecutor") int idOfExecutor){
        return ResponseEntity.ok().body(albumService.removeAlbumFromExecutor(idOfAlbum,idOfExecutor));
    }

    @PostMapping("/addAlbumUser")
    public ResponseEntity<Object> addAlbumUser(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(albumService.addAlbumToUser(idOfAlbum,idOfUser));
    }
    @DeleteMapping("/removeAlbumUser")
    public ResponseEntity<Object> removeAlbumUser(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(albumService.removeAlbumFromUser(idOfAlbum,idOfUser));
    }


}
