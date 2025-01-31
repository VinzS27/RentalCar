package com.si2001.rentalcar.controller;

import com.si2001.rentalcar.model.*;
import com.si2001.rentalcar.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/login")
    public String loginPage() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(value = {"/", "/homepage"})
    public String homepage(ModelMap model) {
        model.addAttribute("customers", userService.getAllUsers());
        model.addAttribute("reservations", reservationService.getReservationsByUsername(getPrincipal()));
        model.addAttribute("cars", carService.getAllCars());
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

    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable("id") int id, ModelMap model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userProfileService.getAllUserProfiles());
        return "editUser";
    }

    @PostMapping("/editUser")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "editUser";
        }

        userService.updateUser(user);
        return "redirect:/homepage";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id, ModelMap model) {
        userService.deleteUserById(id);
        model.addAttribute("users", getUsers());
        return "homepage";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, ModelMap model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "registration"; // Return to the registration form with error
    }

    @GetMapping(value = "/reservations")
    public String reservationForm(@RequestParam(value = "userId", required = false) Integer userId, ModelMap model) {
        List<Reservation> reservations;

        if (userId != null) {
            User user = userService.getById(userId);
            reservations = reservationService.getReservationsByUsername(user.getUsername());
        } else {
            reservations = reservationService.getAllReservations();
        }

        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("reservations", reservations);
        model.addAttribute("users", userService.getAllUsers());
        return "reservations";
    }

    @PostMapping("/saveReservations")
    public String saveReservation(@RequestParam("carId") int carId,
                                     @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                     @RequestParam("reservationId") int reservationId) {
        Car car = carService.getCarById(carId);
        car.setAvailability(false);
        carService.updateCar(car);

        String username = getPrincipal();
        User user = userService.getByUsername(username);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus("in approvazione");
        reservation.setCar(car);

        if (reservationId != 0) {
            Reservation r = reservationService.getReservationById(reservationId);
            r.setStartDate(startDate);
            r.setEndDate(endDate);
            reservationService.updateReservation(r);
        }else {
            reservationService.saveReservation(reservation);
        }

        return "redirect:/homepage";
    }

    @GetMapping("/editReservations/{id}")
    public String editReservation(@PathVariable("id") int id, ModelMap model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/homepage";
        }

        List<Car> cars = carService.getAllCars();
        model.addAttribute("reservation", reservation);
        model.addAttribute("cars", cars);
        model.addAttribute("mode", "edit");

        return "reservations";
    }

    @GetMapping("/deleteReservations/{id}")
    public String deleteReservation(@PathVariable("id") int id) {
        Reservation reservation = reservationService.getReservationById(id);
        Car car = reservation.getCar();
        car.setAvailability(true);
        carService.updateCar(car);

        reservationService.deleteReservationById(id);
        return "redirect:/homepage";
    }

    @PostMapping("/approve-reservation")
    public String approveReservation(@RequestParam("reservationId") int id) {
        reservationService.approveReservation(id);
        return "redirect:/homepage";
    }

    @PostMapping("/decline-reservation")
    public String declineReservation(@RequestParam("reservationId") int id) {
        reservationService.declineReservation(id);
        return "redirect:/homepage";
    }



    @GetMapping(value = {"/cars"})
    public String viewCars(ModelMap model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @PostMapping("/addCar")
    public String addCar(@RequestParam String brand,
                         @RequestParam String model,
                         @RequestParam int year,
                         @RequestParam String licensePlate,
                         @RequestParam boolean available) {
        Car car = new Car(model, brand, year, licensePlate, available);
        carService.saveCar(car);
        return "redirect:/cars";
    }

    @GetMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable int id) {
        carService.deleteCarById(id);
        return "redirect:/cars";
    }
    @GetMapping("/editCar/{id}")
    public String editCar(@PathVariable int id) {
        Car car = carService.getCarById(id);
        carService.updateCar(car);
        return "redirect:/cars";
    }

    @GetMapping("/profile")
    public String userProfile(ModelMap model) {
        model.addAttribute("users", userService.getByUsername(getPrincipal()));
        return "profile";
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