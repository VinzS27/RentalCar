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
        String normalizedType = type.trim().toLowerCase();
        System.out.println("Searching for UserProfile with type: " + normalizedType); // Debug log
        List<UserProfile> profiles = getEntityManager()
                .createQuery("SELECT u FROM UserProfile u WHERE u.type = :type", UserProfile.class)
                .setParameter("type", normalizedType)
                .getResultList();

        return profiles.get(0); // Return the first result (assuming type is unique).
    }
}