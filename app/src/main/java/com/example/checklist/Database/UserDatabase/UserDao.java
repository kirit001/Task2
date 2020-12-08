package com.example.checklist.Database.UserDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void registerUser (UserEntity userEntity);

    @Query("SELECT * FROM User where email=(:email) and password=(:password)")
    UserEntity login(String email, String password);

    @Query("SELECT * FROM User")
    UserEntity getall();

    @Query("SELECT * FROM User where email= :email")
    UserEntity getuser(String email);

    @Query("UPDATE User SET firstname = :firstname WHERE email  = :email")
    void editfirstname(String email, String firstname);

    @Query("UPDATE User SET lastname = :lastname WHERE email  = :email")
    void editlastname(String email, String lastname);

    @Query("UPDATE User SET email = :email WHERE id = :id")
    void editemail(Integer id, String email);

    @Query("UPDATE User SET address = :address WHERE email  = :email")
    void editaddress(String email, String address);
}
