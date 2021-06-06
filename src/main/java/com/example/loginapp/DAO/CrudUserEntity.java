package com.example.loginapp.DAO;

import com.example.loginapp.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CrudUserEntity extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.email = ?1, u.phoneNumber = ?2, u.address = ?3 where u.username = ?4")
    void updateUser(String email, String phoneNumber, String address, String username);
}
