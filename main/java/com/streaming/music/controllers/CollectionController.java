package com.streaming.music.controllers;

import com.streaming.music.dto.TrackData;
import com.streaming.music.dto.UserData;
import com.streaming.music.service.TrackService;
import com.streaming.music.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

public class CollectionController {
    @Resource(name = "userService")
    private UserService userService ;
    @Resource(name="trackService")
    private TrackService trackService;


    @RequestMapping(value = "/addTrack", method = RequestMethod.POST)
    public ResponseEntity<Object> addTrackToUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        try{
            UserData userData = userService.getUserById(idOfUser);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(trackService.addTrackToUser(idOfTrack,idOfUser));
    }
    @DeleteMapping("/removeTrack")
    public ResponseEntity<Object> removeTrackFromUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        try{
            UserData userData = userService.getUserById(idOfUser);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(trackService.removeTrackFromUser(idOfTrack,idOfUser));
    }
}
