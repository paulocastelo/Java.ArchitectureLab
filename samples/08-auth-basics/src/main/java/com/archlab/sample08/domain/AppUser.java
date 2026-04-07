package com.archlab.sample08.domain;

import java.util.UUID;

public class AppUser {
    private String id = UUID.randomUUID().toString();
    private String email;
    private String fullName;
    private String passwordHash;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
