package com.si2001.rentalcar.config;

import com.si2001.rentalcar.converter.CarConverter;
import com.si2001.rentalcar.converter.ReservationConverter;
import com.si2001.rentalcar.converter.RoleToUserProfileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.si2001.rentalcar"})
public class AppConfig {

    final RoleToUserProfileConverter roleToUserProfileConverter;
    final CarConverter carConverter;
    final ReservationConverter reservationConverter;

    public AppConfig(RoleToUserProfileConverter roleToUserProfileConverter, CarConverter carConverter, RoleToUserProfileConverter roleConverter, ReservationConverter reservationConverter) {
        this.roleToUserProfileConverter = roleToUserProfileConverter;
        this.carConverter = carConverter;
        this.reservationConverter = reservationConverter;
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".html");
        registry.viewResolver(viewResolver);
    }

    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(roleToUserProfileConverter);
        registry.addConverter(carConverter);
        registry.addConverter(reservationConverter);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
}