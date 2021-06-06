package com.example.loginapp.service;

import com.example.loginapp.DAO.CrudNoteEntity;
import com.example.loginapp.DAO.CrudUserEntity;
import com.example.loginapp.model.NoteEntity;
import com.example.loginapp.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    CrudUserEntity userRepo;

    @Autowired
    CrudNoteEntity noteRepo;

    @Override
    public boolean save(UserEntity userEntity) {
        BCryptPasswordEncoder endcode = new BCryptPasswordEncoder();
        if (userRepo.findByUsername(userEntity.getUsername()) == null) {
            userEntity.setRole("ROLE_USER");
            userEntity.setEnable(true);
            userEntity.setPassword(endcode.encode(userEntity.getPassword()));
            userRepo.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(UserEntity userEntity, String name) {
        userRepo.updateUser(userEntity.getEmail(), userEntity.getPhoneNumber(), userEntity.getAddress(), name);
    }

    @Override
    public void addnote(NoteEntity noteEntity, UserEntity userEntity) {
        noteEntity.setDate(setDate());
        noteEntity.setUserEntity(userEntity);
        noteRepo.save(noteEntity);
    }

    @Override
    public void updateNote(NoteEntity noteEntity, Long id) {
        noteEntity.setDate(setDate());
        noteRepo.updateAll(noteEntity.getTitle(), noteEntity.getContent(), noteEntity.getDate(), id);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepo.findByEmail(email) == null;
    }

    @Override
    public Date setDate() {
        Calendar date = Calendar.getInstance();
        Date dateSql = new Date(0);
        dateSql.setDate(date.get(Calendar.DATE));
        dateSql.setMonth(date.get(Calendar.MONTH));
        dateSql.setYear(date.get(Calendar.YEAR) - 1900);
        return dateSql;
    }
}
