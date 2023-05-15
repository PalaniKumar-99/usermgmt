package com.example.UserMgmtApi.service;

import com.example.UserMgmtApi.binding.ActivateAccount;
import com.example.UserMgmtApi.binding.Login;
import com.example.UserMgmtApi.binding.User;
import com.example.UserMgmtApi.entity.UserMaster;

import java.util.List;

public interface UserMgmtService {
    public boolean saveUser(User user);

    public boolean activateUserAccount(ActivateAccount activateAccount);
    public List<User> getAllUser();
    public User getUserById(Integer id);
    public boolean deleteUserById(Integer id);
    public boolean changeAccountStatus(Integer userId, String status);
    public String login(Login login);
    public String forgotPassword(String email);
}
