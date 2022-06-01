package com.streaming.music.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.streaming.music.dto.AlbumData;
import com.streaming.music.dto.ExecutorData;
import com.streaming.music.service.AlbumService;
import com.streaming.music.service.ExecutorService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@CrossOrigin
@RestController
public class ExecutorController {

    @Resource(name = "executor_service")
    private ExecutorService executorService;
    @Resource(name="album_service")
    private AlbumService albumService;
    @Value("${upload.path.executor}")
    private String uploadPath;

    @GetMapping("/executor")
    public ResponseEntity<Object> getExecutor(@RequestParam("id") int id){
        ExecutorData executor = new ExecutorData();
        try {
            executor = executorService.getExecutorById(id);
        }catch (EntityNotFoundException e){
            ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body(executor);
    }

    @GetMapping("/getExecutorByName")
    public ResponseEntity<Object> getExecutorByName(@RequestParam("name") String name){
        ExecutorData executor = new ExecutorData();
        try {
            executor = executorService.getExecutorByName(name);
        }catch (EntityNotFoundException e){
            ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body(executor);
    }


    @PostMapping("/executor")
    public ResponseEntity<Object> createExecutor(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ExecutorData executorData=gson.fromJson(data, ExecutorData.class);
        System.out.println(executorData);
        try{
           ExecutorData e=executorService.findExecutorByNameOfExecutor(executorData.getNameOfExecutor());
        }catch (EntityNotFoundException ee){
            ExecutorData e = new ExecutorData();
            e.setNameOfExecutor(executorData.getNameOfExecutor());
            e.setDescriptionExt(executorData.getDescriptionExt());
            e.setDateOfFormation(executorData.getDateOfFormation());
            e.setDisbandmentDate(executorData.getDisbandmentDate());
            try{
                String resultFilename =executorService.savePicture(file);
                e.setPathToAvatar(uploadPath + "/" + resultFilename);
            }catch (IOException ex){
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
            int i =executorService.saveExecutor(e).getId();
            System.out.println(i);
            return ResponseEntity.ok(i);
        }
        return ResponseEntity.badRequest().body("Executor exist");
    }

    @PatchMapping("/executor")
    public ResponseEntity<Object> updateExecutor(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ExecutorData executorData=gson.fromJson(data, ExecutorData.class);
        ExecutorData e;
        try{
            e = executorService.getExecutorById(executorData.getId());
        }catch (EntityNotFoundException ex){
            return ResponseEntity.ok().body("Executor does not exist");
        }
        e.setId(executorData.getId());
        e.setNameOfExecutor(executorData.getNameOfExecutor());
        e.setDescriptionExt(executorData.getDescriptionExt());
        e.setDateOfFormation(executorData.getDateOfFormation());
        e.setDisbandmentDate(executorData.getDisbandmentDate());
        try{
            String resultFilename =executorService.savePicture(file);
            e.setPathToAvatar(uploadPath + "/" + resultFilename);
        }catch (IOException exx){
            return ResponseEntity.badRequest().body(exx.getMessage());
        }
        executorService.updateExecutor(e);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/executor")
    public ResponseEntity<Object> deleteUser(@RequestParam("id")int id) {
        ExecutorData e;
        try{
            e = executorService.getExecutorById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.ok().body(ex.getMessage());
        }
        executorService.deleteExecutor(id);
        return ResponseEntity.ok().body("User deleted");
    }
    @GetMapping("/allExecutorsPage")
    public ResponseEntity<Object> getAllExecutors(@RequestParam("pageNumber") int pageNumber,@RequestParam("pageSize") int pageSize){
        Pageable pageable = (Pageable) PageRequest.of(pageNumber,pageSize);
        List<ExecutorData> executorData;
        long totalPages;
        try{
            executorData = executorService.getAll(pageable);
            totalPages = executorService.getCount();
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return  ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalPages)).body(executorData);
    }

    @GetMapping("/allExecutors")
    public ResponseEntity<Object> getAll(){
        List<ExecutorData> executorData = executorService.getAllExecutor();
        return ResponseEntity.ok().body(executorData);
    }

    @GetMapping(value = "/imageExecutor")
    public ResponseEntity<Object> getImage(@RequestParam ("id") int id) throws IOException {
        ExecutorData executor = new ExecutorData();
        try {
            executor = executorService.getExecutorById(id);
        }catch (EntityNotFoundException e){
            ResponseEntity.ok().body(e.getMessage());
        }
        byte[] fileContent = FileUtils.readFileToByteArray(new File(executor.getPathToAvatar()));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(executor.getPathToAvatar())));
        return ResponseEntity.ok().body(encodedString);
    }

    @PostMapping("/addGenreToExecutor")
    public ResponseEntity<Object> addGenreToExecutor(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfGenres") int idOfGenres){
        return ResponseEntity.ok().body(executorService.addGenresToExecutor(idOfExecutor,idOfGenres));
    }

    @PostMapping("/removeGenreFromExecutor")
    public ResponseEntity<Object> removeGenreFromExecutor(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfGenres") int idOfGenres){
        return ResponseEntity.ok().body(executorService.removeGenresFromExecutor(idOfExecutor,idOfGenres));
    }

//    @GetMapping(value = "/albumExecutor")
//    public ResponseEntity<Object> getAlbum(@RequestParam("idExecutor") int id){
//        List<AlbumData> albumData = albumService.getAlbumByExecutor(id);
//        return ResponseEntity.ok().body(albumData);
//    }



    @PostMapping("/addExecutorToUser")
    public ResponseEntity<Object> addExecutorToUser(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(executorService.addExecutorToUser(idOfExecutor,idOfUser));
    }
    @DeleteMapping("/removeExecutorFromUser")
    public ResponseEntity<Object> removeExecutorFromUser(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(executorService.removeExecutorFromUser(idOfExecutor,idOfUser));
    }
}
