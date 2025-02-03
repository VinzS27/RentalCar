package com.si2001.rentalcar.service;

import java.util.List;

import com.si2001.rentalcar.model.User;

public interface UserService {

	User getById(int id);

	User getByUsername(String username);

	void saveUser(User user);

	void updateUser(User user);

	void deleteUserById(int id);

	List<User> getAllUsers();

	boolean isUsernameUnique(int id, String username);

	void deleteUserByUsername(String ssoId);

}