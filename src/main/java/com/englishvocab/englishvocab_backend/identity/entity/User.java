package com.englishvocab.englishvocab_backend.identity.entity;

import com.englishvocab.englishvocab_backend.identity.enums.Role;
import com.englishvocab.englishvocab_backend.identity.enums.UserStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false,length = 50)
    private String username;

    @Column(name = "email",nullable = false,length = 255)
    private String email;

    @Column(name = "password_hash",nullable = false,length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void onCreate(){
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = OffsetDateTime.now();
    }

    protected User(){}

    public User(String username, String email, String passwordHash){
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.status = UserStatus.ACTIVE;
        this.roles.add(Role.USER);
    }

    // GETTER & SETTER

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus(){
        return status;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}


