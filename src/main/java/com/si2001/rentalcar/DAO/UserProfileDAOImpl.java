package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.UserProfile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserProfileDao")
public class UserProfileDAOImpl extends AbstractDao<Integer, UserProfile> implements UserProfileDAO {

    public UserProfile getUserProfileById(int id) {
        return getByKey(id);
    }

    public List<UserProfile> getAllUserProfile() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
        Root<UserProfile> root = cq.from(UserProfile.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    public UserProfile getUserProfileByType(String type) {
        String normalizedType = type.trim().toLowerCase();
        System.out.println("Searching for UserProfile with type: " + normalizedType); // Debug log
        List<UserProfile> profiles = getEntityManager()
                .createQuery("SELECT u FROM UserProfile u WHERE u.type = :type", UserProfile.class)
                .setParameter("type", normalizedType)
                .getResultList();

        return (profiles.isEmpty())? null:profiles.get(0);
    }
}