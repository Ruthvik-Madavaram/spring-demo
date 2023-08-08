package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody UserDTO user){
        try{
            // use model-mapper
            User newUser = userService.createUser(user);
            return ResponseEntity.ok().body(newUser);
        }catch (UserException e){
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try{
            User updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        }catch (UserException e){
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }
}
