package com.si2001.rentalcar.controller;

import com.si2001.rentalcar.model.*;
import com.si2001.rentalcar.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
public class UserController {

    final UserService userService;
    final UserProfileService userProfileService;
    final CarService carService;
    final ReservationService reservationService;
    final MessageSource messageSource;
    final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    final AuthenticationTrustResolver authenticationTrustResolver;

    public UserController(UserService userService, UserProfileService userProfileService, CarService carService, ReservationService reservationService, MessageSource messageSource, PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, AuthenticationTrustResolver authenticationTrustResolver) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.carService = carService;
        this.reservationService = reservationService;
        this.messageSource = messageSource;
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.authenticationTrustResolver = authenticationTrustResolver;
    }

    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("loggedinuser", getPrincipal());
        return "userslist";
    }

    @RequestMapping(value = {"/newuser"}, method = RequestMethod.GET)
    public String newUser(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
        return "registration";
    }

    @RequestMapping(value = {"/newuser"}, method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "registration";
        }

        if (!userService.isUsernameUnique(user.getId(), user.getUsername())) {
            FieldError error = new FieldError("user", "username", messageSource.getMessage("non.unique.username", new String[]{user.getUsername()}, Locale.getDefault()));
            result.addError(error);
            return "registration";
        }

        userService.saveUser(user);

        model.addAttribute("success", "User " + user.getUsername() + " registered successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/edit-user-{username}"}, method = RequestMethod.GET)
    public String editUser(@PathVariable String username, ModelMap model) {
        User user = userService.getByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "registration";
    }

    @RequestMapping(value = {"/edit-user-{username}"}, method = RequestMethod.POST)
    public String updateUser(@Valid User user, BindingResult result, ModelMap model, @PathVariable String username) {
        if (result.hasErrors()) {
            return "registration";
        }

        userService.updateUser(user);

        model.addAttribute("success", "User " + user.getUsername() + " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }
    
    @RequestMapping(value = {"/delete-user-{username}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return "redirect:/list";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/list";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    @ModelAttribute("roles")
    public List<UserProfile> initializeProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @ModelAttribute("cars")
    public List<Car> initializeCars() {
        return carService.getAllCars();
    }

    @ModelAttribute("reservations")
    public List<Reservation> initializeReservations() {
        return reservationService.getAllReservations();
    }

    @ModelAttribute("loggedIN")
    public String getLoggedIN() {
        if (isCurrentAuthenticationAnonymous()) {
            return null;
        }
        return getPrincipal();
    }

    @ModelAttribute("users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public boolean check(Authentication authentication, String username) {
        return this.getPrincipal().equals(username);
    }

    private String getPrincipal() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }
}