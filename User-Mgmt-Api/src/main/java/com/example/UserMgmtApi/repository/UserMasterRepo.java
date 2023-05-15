package com.example.UserMgmtApi.repository;

import com.example.UserMgmtApi.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {

    public UserMaster findByEmailAndPassword(String email, String password);

    public UserMaster findByEmail(String email);
}
