package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserProfileDao")
public class UserProfileDAOImpl extends AbstractDao<Integer, UserProfile> implements UserProfileDAO {

    public UserProfile getUserProfileById(int id) {
        return getByKey(id);
    }

    public List<UserProfile> getAllUserProfile() {
        return getEntityManager()
                .createQuery("SELECT u FROM UserProfile u", UserProfile.class)
                .getResultList();
    }

    public UserProfile getUserProfileByType(String type) {
        return (UserProfile) getEntityManager()
                .createQuery("SELECT e FROM UserProfile e WHERE e.type LIKE :type")
                .setParameter("type", type)
                .getSingleResult();
    }
}