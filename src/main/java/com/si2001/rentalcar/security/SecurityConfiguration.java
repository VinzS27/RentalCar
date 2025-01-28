package com.si2001.rentalcar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfiguration {

    final UserDetailsService userDetailsService;
    final PersistentTokenRepository tokenRepository;

    @Autowired
    public SecurityConfiguration(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Autowired
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/").hasAnyRole( "CUSTOMER","ADMIN")
                                .requestMatchers("/newuser/**", "/delete-user-*").hasRole("ADMIN")
                                .requestMatchers("/edit-user-*").hasRole("ADMIN")
                                .requestMatchers("/profile/**").hasAnyRole("CUSTOMER", "ADMIN")
                                .anyRequest().authenticated())

                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .permitAll())

                .rememberMe(remember -> remember.rememberMeParameter("remember-me")
                        .tokenRepository(tokenRepository)
                        .tokenValiditySeconds(86400)) // 24h

                .exceptionHandling(exception ->
                        exception.accessDeniedPage("/accessDenied"))

                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("remember-me", userDetailsService, tokenRepository);
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

}
