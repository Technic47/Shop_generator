package ru.kuznetsov.shop.generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mockUserRecord")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MockUserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(name = "created")
    protected LocalDateTime created;
    @Column(name = "updated")
    protected LocalDateTime updated;
    @Column(name = "userName")
    protected String userName;
    @Column(name = "login")
    protected String login;
    @Column(name = "passWord")
    protected String passWord;
}
