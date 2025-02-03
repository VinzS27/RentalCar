package com.si2001.rentalcar.DAO;

import java.util.List;

import com.si2001.rentalcar.model.*;

public interface UserDAO {

    User getUserById(int id);

    User getUserByUsername(String username);

    void saveUser(User user);

    void deleteUserById(int id);

    void deleteUserByUsername(String username);

    List<User> getAllUsers();

    void updateUser(User u);
}
