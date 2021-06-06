package com.example.loginapp.DAO;

import com.example.loginapp.model.NoteEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface CrudNoteEntity extends CrudRepository<NoteEntity,Long> {
    @Transactional
    @Modifying
    @Query("select n from NoteEntity n inner join UserEntity u on n.userEntity.id = u.id where u.id =?1")
    List<NoteEntity> findByUserEntity(Long id);

    @Transactional
    @Modifying
    @Query("select n from NoteEntity n where n.title like ?1 and n.userEntity.id = ?2")
    List<NoteEntity> searchNameandID(String name, Long id);

    @Transactional
    @Modifying
    @Query("select n from NoteEntity n where n.title like ?1")
    List<NoteEntity> search(String name);

    @Transactional
    @Modifying
    @Query("update NoteEntity n set n.title = ?1, n.content = ?2, n.date= ?3 where n.id = ?4")
    void updateAll(String title, String content, Date date, Long id);

}
