package com.example.UserMgmtApi.controller;

import com.example.UserMgmtApi.binding.ActivateAccount;
import com.example.UserMgmtApi.binding.Login;
import com.example.UserMgmtApi.binding.User;
import com.example.UserMgmtApi.service.UserMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMgmtService service;

    @PostMapping("/user")
    public ResponseEntity<String> userReg(@RequestBody User user) {
        boolean isSaved = service.saveUser(user);
        if(isSaved) {
            return new ResponseEntity<>("Registration Successful", HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount activateAccount) {
        boolean isActivated = service.activateUserAccount(activateAccount);
        if(isActivated) {
            return new ResponseEntity<>("Account activated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid temporary password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUser = service.getAllUser();
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User userById = service.getUserById(userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<String> deleteByUserId(@PathVariable Integer userId) {
        boolean isDeleted = service.deleteUserById(userId);
        if(isDeleted) {
            return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid UserId", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{id}/{status}")
    public ResponseEntity<String> changeStatus(@PathVariable Integer id, @PathVariable String status) {
        boolean isChanged = service.changeAccountStatus(id, status);
        if(isChanged) {
            return new ResponseEntity<>("Status Chnaged", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to change the status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        String loginStatus = service.login(login);
        return new ResponseEntity<>(loginStatus,HttpStatus.OK);
    }

    @GetMapping("/forgotpwd/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        String status = service.forgotPassword(email);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
