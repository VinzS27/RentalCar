package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.UserProfile;

import java.util.List;

public interface UserProfileDAO {

    UserProfile getUserProfileById(int id);

    List<UserProfile> getAllUserProfile();

    UserProfile getUserProfileByType(String type);
}
