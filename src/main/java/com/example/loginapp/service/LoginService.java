package com.example.loginapp.service;

import com.example.loginapp.model.NoteEntity;
import com.example.loginapp.model.UserEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public interface LoginService {
    boolean save(UserEntity userEntity);
    Date setDate();
    boolean checkEmail(String email);
    void addnote(NoteEntity noteEntity, UserEntity userEntity);
    void updateNote(NoteEntity noteEntity,Long id);
    void updateUser(UserEntity userEntity, String name);
}
