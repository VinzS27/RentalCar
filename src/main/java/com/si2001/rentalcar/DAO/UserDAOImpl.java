package com.si2001.rentalcar.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.si2001.rentalcar.model.User;

@Repository("UserDao")
public class UserDAOImpl extends AbstractDao<Integer, User> implements UserDAO {

    public User getUserById(int id) {
		return getByKey(id);
	}

	public User getUserByUsername(String username) {
        return (User) getEntityManager()
                .createQuery("SELECT u FROM User u WHERE u.username LIKE :username")
                .setParameter("username", username)
                .getSingleResult();
	}

	public List<User> getAllUsers() {
		return getEntityManager()
				.createQuery("SELECT u FROM User u", User.class)
				.getResultList();
	}

	public List<User> getAllUserByUsername(String username) {
		return getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", username)
				.getResultList();
	}

	public void saveUser(User user) {
		this.persist(user);
	}

	public void deleteUserById(int id) {
		User user = (User) getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		delete(user);
	}

	public void deleteUserByUsername(String username) {
		User user = (User) getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.username LIKE :username")
				.setParameter("username", username)
				.getSingleResult();
		delete(user);
	}
}
