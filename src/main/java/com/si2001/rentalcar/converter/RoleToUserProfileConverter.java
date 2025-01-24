package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.UserProfile;
import com.si2001.rentalcar.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class RoleToUserProfileConverter implements Converter<Object, UserProfile>{

    static final Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);

    UserProfileService userProfileService;

    public UserProfile convert(Object element) {
        Integer id = Integer.parseInt((String)element);
        UserProfile profile= userProfileService.getUserProfileById(id);
        logger.info("Profile : {}",profile);
        return profile;
    }

}