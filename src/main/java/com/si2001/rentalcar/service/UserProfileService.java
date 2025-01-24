package com.si2001.rentalcar.service;

import com.si2001.rentalcar.model.UserProfile;

import java.util.List;

public interface UserProfileService {

    UserProfile getUserProfileById(int id);

    UserProfile getUserProfileByType(String type);

    List<UserProfile> getAllUserProfiles();
}
