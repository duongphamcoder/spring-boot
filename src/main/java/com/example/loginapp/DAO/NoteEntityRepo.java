package com.example.loginapp.DAO;

import com.example.loginapp.model.NoteEntity;
import com.example.loginapp.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteEntityRepo extends JpaRepository<NoteEntity,Long> {
    List<NoteEntity> findByUserEntityAndTitleContaining(UserEntity userEntity, String title);
}
