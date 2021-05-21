package com.jc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    @GetMapping("/")
    public List<User> getUsers() {
        return List.of(new User());
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return new User();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }
}
