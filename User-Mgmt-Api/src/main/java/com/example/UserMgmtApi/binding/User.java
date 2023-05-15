package com.example.UserMgmtApi.binding;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private String fullName;
    private String email;
    private Long mobileNo;
    private String gender;
    private LocalDate dob;
    private Long ssn;
}
