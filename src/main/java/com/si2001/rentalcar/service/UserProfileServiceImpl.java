package com.si2001.rentalcar.service;

import com.si2001.rentalcar.DAO.UserProfileDAOImpl;
import com.si2001.rentalcar.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("UserProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    final UserProfileDAOImpl dao;

    @Autowired
    public UserProfileServiceImpl(UserProfileDAOImpl dao) {
        this.dao = dao;
    }

    public UserProfile getUserProfileById(int id) {
        return dao.getUserProfileById(id);
    }

    public UserProfile getUserProfileByType(String type) {
        return dao.getUserProfileByType(type);
    }

    public List<UserProfile> getAllUserProfiles() {
        return dao.getAllUserProfile();
    }
}