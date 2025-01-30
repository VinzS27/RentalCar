package com.si2001.rentalcar.service;

import java.util.List;

import com.si2001.rentalcar.DAO.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.si2001.rentalcar.model.User;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAOImpl dao;

    @Autowired
    public UserServiceImpl(UserDAOImpl dao) {
        this.dao = dao;
    }

    public User getById(int id) {
        return dao.getUserById(id);
    }

    public User getByUsername(String username) {
        return dao.getUserByUsername(username);
    }

	public List<User> getAllUsers() {
		return dao.getAllUsers();
	}

    public void saveUser(User user) {
        dao.saveUser(user);
    }

    public void updateUser(User user) {
        User u = dao.getUserById(user.getId());

        if (u != null) {
			u.setUsername(user.getUsername());
			u.setPassword(user.getPassword());
            u.setEmail(user.getEmail());
            if (user.getUserProfiles() != null) {
                u.setUserProfiles(user.getUserProfiles());
            }
            dao.updateUser(u);
        }
    }

    public void deleteUserById(int id) {
        dao.deleteUserById(id);
    }

    public void deleteUserByUsername(String username) {
        dao.deleteUserByUsername(username);
    }

    public boolean isUsernameUnique(int id, String username) {
        User user = getByUsername(username);
        return (user == null || ((id != 0) && (user.getId() == id)));
    }
}
