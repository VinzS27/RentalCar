package com.si2001.rentalcar.controller;

import com.si2001.rentalcar.model.*;
import com.si2001.rentalcar.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
public class RentalCarController {

    final UserService userService;
    final UserProfileService userProfileService;
    final CarService carService;
    final ReservationService reservationService;
    final MessageSource messageSource;
    final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    final AuthenticationTrustResolver authenticationTrustResolver;

    @Autowired
    public RentalCarController(UserService userService, UserProfileService userProfileService, CarService carService, ReservationService reservationService, MessageSource messageSource, PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, AuthenticationTrustResolver authenticationTrustResolver) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.carService = carService;
        this.reservationService = reservationService;
        this.messageSource = messageSource;
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.authenticationTrustResolver = authenticationTrustResolver;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = {"/","/homepage"}, method = RequestMethod.GET)
    public String homepage(ModelMap model) {
        List<User> customers = userService.getAllUsers();
        model.addAttribute("customers", customers);
        List<Reservation> reservationList = reservationService.getAllReservations();
        model.addAttribute("reservationList", reservationList);
        return "homepage";
    }

    @GetMapping("/registration")
    public String registrationForm(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userProfileService.getAllUserProfiles());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveNewUser(@ModelAttribute("user") @Valid User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "registration";
        }
        /*if (!userService.isUsernameUnique(user.getId(), user.getUsername())) {
            FieldError ssoError = new FieldError("user", "username",
                    messageSource.getMessage("non.unique.username", new String[]{user.getUsername()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }*/
        userService.saveUser(user);
        return "redirect:/homepage";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @RequestMapping(value = {"/cars"}, method = RequestMethod.GET)
    public String viewCars(ModelMap model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("loggedinuser", getPrincipal());
        return "cars";
    }

    @PostMapping("/approve-reservation/{id}")
    public String approveReservation(@PathVariable int id) {
        reservationService.approveReservation(id);
        return "redirect:/homepage";
    }

    @PostMapping("/decline-reservation/{id}")
    public String declineReservation(@PathVariable int id) {
        reservationService.declineReservation(id);
        return "redirect:/homepage";
    }

    @RequestMapping(value = "/reservations", method = RequestMethod.GET)
    public String reservationList(ModelMap model) {
        model.addAttribute("activePage", "reservations");
        model.addAttribute("users", userProfileService.getAllUserProfiles());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations";
    }

    @GetMapping("/userslist")
    public String listUsers(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
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