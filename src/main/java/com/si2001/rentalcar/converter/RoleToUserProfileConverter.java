package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.UserProfile;
import com.si2001.rentalcar.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class RoleToUserProfileConverter implements Converter<Object, UserProfile> {

    final UserProfileService userProfileService;

    public RoleToUserProfileConverter(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public UserProfile convert(Object element) {
        return userProfileService.getUserProfileByType((String) element);
    }

}