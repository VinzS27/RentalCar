package com.si2001.rentalcar.controller;

import com.si2001.rentalcar.model.*;
import com.si2001.rentalcar.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
public class RentalCarController {
    final UserService userService;
    final UserProfileService userProfileService;
    final CarService carService;
    final ReservationService reservationService;
    final MessageSource messageSource;
    final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    final AuthenticationTrustResolver authenticationTrustResolver;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public RentalCarController(UserService userService, UserProfileService userProfileService, CarService carService, ReservationService reservationService, MessageSource messageSource, PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, AuthenticationTrustResolver authenticationTrustResolver, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.carService = carService;
        this.reservationService = reservationService;
        this.messageSource = messageSource;
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.authenticationTrustResolver = authenticationTrustResolver;
        this.passwordEncoder = passwordEncoder;
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
        model.addAttribute("reservations", reservationService.getReservationsByUsername(getPrincipal()));
        return "homepage";
    }

    @GetMapping("/registration")
    public String registrationForm(ModelMap model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/saveRegistration")
    public String saveNewUser(@RequestParam("userId") int userId,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam("email") String email,
                              @RequestParam("userProfile") int userProfileId, ModelMap model) {
        User user;

        if (!userService.isUsernameUnique(userId, username)) {
            model.addAttribute("user", (userId != 0) ? userService.getById(userId) : new User());
            model.addAttribute("mode", (userId != 0) ? "edit" : "");
            model.addAttribute("usernameError", "Il nome utente è già in uso");
            return "registration";
        }

        if (userId != 0) {
            user = userService.getById(userId);
            setData(username, password, email, userProfileId, user);
            userService.updateUser(user);
        } else {
            user = new User();
            setData(username, password, email, userProfileId, user);
            userService.saveUser(user);
        }

        return "redirect:/homepage";
    }

    private void setData(String username, String password, String email, int userProfileId, User user) {
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setUserProfiles(Set.of(userProfileService.getUserProfileById(userProfileId)));
    }

    @GetMapping("/editRegistration/{id}")
    public String editRegistration(@PathVariable("id") int id, ModelMap model) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/homepage";
        }
        model.addAttribute("user", user);
        model.addAttribute("mode", "edit");
        return "registration";
    }

    @GetMapping("/deleteRegistration/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/homepage";
    }

    @GetMapping(value = "/reservations")
    public String reservationForm(@RequestParam(value = "userId", required = false) Integer userId, ModelMap model) {
        List<Reservation> reservations;

        //for filter
        if (userId != null) {
            User user = userService.getById(userId);
            reservations = reservationService.getReservationsByUsername(user.getUsername());
        } else {
            reservations = reservationService.getAllReservations();
        }

        model.addAttribute("reservations", reservations);
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
        } else {
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
        model.addAttribute("reservation", reservation);
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
    public String viewCars() {
        return "cars";
    }

    @GetMapping(value = {"/addCar"})
    public String addCarForm() {
        return "addCar";
    }

    @PostMapping("/saveCar")
    public String addCar(@RequestParam("carId") int carId,
                         @RequestParam("brand") String brand,
                         @RequestParam("model") String model,
                         @RequestParam("year") int year,
                         @RequestParam("licensePlate") String licensePlate,
                         @RequestParam("available") boolean available) {
        if (carId != 0) {
            Car car = carService.getCarById(carId);
            car.setBrand(brand);
            car.setModel(model);
            car.setYear(year);
            car.setLicensePlate(licensePlate);
            car.setAvailability(available);
            carService.updateCar(car);
        } else {
            Car car = new Car(model, brand, year, licensePlate, available);
            carService.saveCar(car);
        }

        return "redirect:/cars";
    }

    @GetMapping("/editCar/{id}")
    public String editCar(@PathVariable("id") int id, ModelMap model) {
        Car car = carService.getCarById(id);
        if (car == null) {
            return "redirect:/cars";
        }
        model.addAttribute("mode", "edit");
        model.addAttribute("car", car);

        return "addCar";
    }

    @GetMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        carService.deleteCarById(id);
        return "redirect:/cars";
    }

    @GetMapping("/profile")
    public String userProfile(ModelMap model) {
        model.addAttribute("users", userService.getByUsername(getPrincipal()));
        return "profile";
    }

    @GetMapping(value = "/accessDenied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    @ModelAttribute("loggedIN")
    public String getLoggedIN() {
        if (isCurrentAuthenticationAnonymous()) {
            return null;
        }
        return getPrincipal();
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

    @ModelAttribute("users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    private String getPrincipal() {
        String username;
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