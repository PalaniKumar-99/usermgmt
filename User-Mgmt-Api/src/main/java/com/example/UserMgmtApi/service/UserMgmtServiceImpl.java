package com.example.UserMgmtApi.service;

import com.example.UserMgmtApi.binding.ActivateAccount;
import com.example.UserMgmtApi.binding.Login;
import com.example.UserMgmtApi.binding.User;
import com.example.UserMgmtApi.entity.UserMaster;
import com.example.UserMgmtApi.repository.UserMasterRepo;
import com.example.UserMgmtApi.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

    private Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);

    @Autowired
    private UserMasterRepo repo;
    @Autowired
    private EmailUtils emailUtils;

    private Random random = new Random();

    public String getRandomPassword() {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAplhabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAplhabet + numbers;
        StringBuilder sb = new StringBuilder();
        int length = 6;

        for(int i=0; i<length;i++) {
            int index = this.random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    @Override
    public boolean saveUser(User user) {
        UserMaster userMaster = new UserMaster();
        BeanUtils.copyProperties(user, userMaster);
        userMaster.setPassword(getRandomPassword());
        userMaster.setActiveSw("InActive");
        UserMaster save = repo.save(userMaster);

        String fileName = "Recover-Password-Body.txt";
        String subject = "your registration successful";

        String body = readEmailBody(userMaster.getFullName(), userMaster.getPassword(), fileName);
        emailUtils.sendEmail(user.getEmail(), subject, body);
        return save.getUserId() != null;
    }

    @Override
    public boolean activateUserAccount(ActivateAccount activateAccount) {
        UserMaster entity = new UserMaster();
        entity.setEmail(activateAccount.getEmail());
        entity.setPassword(activateAccount.getTempPwd());
        Example<UserMaster> of = Example.of(entity);
        List<UserMaster> all = repo.findAll();

        if(all.isEmpty()) {
            return false;
        } else {
            UserMaster userMaster = all.get(0);
            userMaster.setPassword(activateAccount.getNewPwd());
            userMaster.setActiveSw("Active");
            repo.save(userMaster);
            return true;
        }
    }

    @Override
    public List<User> getAllUser() {
        List<UserMaster> allUsers = repo.findAll();
        List<User> users = new ArrayList<>();
        for(UserMaster entity : allUsers) {
            User user = new User();
            BeanUtils.copyProperties(entity, user);
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        Optional<UserMaster> byId = repo.findById(id);
        if(byId.isPresent()) {
            User user = new User();
            BeanUtils.copyProperties(byId.get(), user);
            return user;
        }
        return null;
    }

    @Override
    public boolean deleteUserById(Integer id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("Exception Occured", e);
        }
        return false;
    }

    @Override
    public boolean changeAccountStatus(Integer userId, String status) {
        Optional<UserMaster> byId = repo.findById(userId);
        if(byId.isPresent()) {
            UserMaster userMaster = byId.get();
            userMaster.setActiveSw(status);
            repo.save(userMaster);
            return true;
        }
        return false;
    }

    @Override
    public String login(Login login) {
        UserMaster userMaster = repo.findByEmailAndPassword(login.getEmail(), login.getPassword());
        if(userMaster == null) {
            return "Invalid Credentials";
        } else {
            if(userMaster.getActiveSw().equals("Active")) {
                return "You have successfully logged in";
            }
            else {
                return "Account is not activated";
            }
        }
    }

    @Override
    public String forgotPassword(String email) {
        UserMaster entity = repo.findByEmail(email);

        if (entity == null) {
            return "Invalid Email";
        }
        String subject ="Forgot Password Recovery";
        String fileName ="RECOVER-PWD-BODY.txt";
        String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
        boolean sendEmail = emailUtils.sendEmail(email, subject, body);
        if (sendEmail) {

            return "Password Sent Successfully To Your Registered Email-Id";

        }
        return null;
    }

    public String readEmailBody(String fullName, String password, String fileName) {
        String url = "";
        String mailBody = null;
        try (FileReader fr = new FileReader(fileName);
             BufferedReader br = new BufferedReader(fr);) {
            StringBuilder builder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                builder.append(line);
                line = br.readLine();
            }
            mailBody = builder.toString();
            mailBody = mailBody.replace("{fullName}", fullName);
            mailBody = mailBody.replace("{temp-pwd}", password);
            mailBody = mailBody.replace("{url}", url);
            mailBody = mailBody.replace("{password}", password);

        } catch (Exception e) {
            logger.error("Exception Occured", e);
        }
        return mailBody;
    }
}
