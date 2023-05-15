package com.example.UserMgmtApi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "User_Master")
@Data
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String fullName;
    private String email;
    private Long mobileNo;
    private String gender;
    private LocalDate dob;
    private Long ssn;
    private String password;
    private String activeSw;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false)
    private LocalDate updatedDate;
    private String createdBy;
    private String updatedBy;
}
