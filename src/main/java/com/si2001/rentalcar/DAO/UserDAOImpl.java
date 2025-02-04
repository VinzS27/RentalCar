package com.si2001.rentalcar.DAO;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.where(cb.equal(root.get("username"), username));
		List<User> users = entityManager.createQuery(cq).getResultList();
		return (users.isEmpty()) ? null : users.get(0);
	}

	public List<User> getAllUsers() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.select(root);
		return entityManager.createQuery(cq).getResultList();
	}


	public void updateUser(User u) {
		update(u);
	}

	public void saveUser(User user) {
		persist(user);
	}

	public void deleteUserById(int id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.where(cb.equal(root.get("id"), id));
		User user = entityManager.createQuery(cq).getSingleResult();
		delete(user);
	}

	public void deleteUserByUsername(String username) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.where(cb.like(root.get("username"), username));
		User user = entityManager.createQuery(cq).getSingleResult();
		delete(user);
	}

}
