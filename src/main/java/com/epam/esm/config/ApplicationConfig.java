package com.epam.esm.config;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateRepo;
import com.epam.esm.orders.Order;
import com.epam.esm.orders.OrderRepo;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagRepo;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepo userRepo;

    @Profile("!test")
    @Bean
    public CommandLineRunner generateData(GiftCertificateRepo giftCertificateRepo,
                                          TagRepo tagRepo, UserRepo userRepo, OrderRepo orderRepo) {
        return args -> {
            Faker faker = new Faker();
            for (int i = 0; i < 1000; i++) {
                User user = new User();
                user.setEmail(faker.name().fullName());
                user.setFirstName(faker.name().firstName());
                user.setLastName(faker.name().lastName());
                userRepo.save(user);
            }

            for (int i = 0; i < 1000; i++) {
                String name = faker.name().fullName();
                if (tagRepo.existsByName(name)) {
                    i--;
                } else {
                    tagRepo.save(Tag.builder().name(name).build());
                }
            }

            for (int i = 0; i < 10000; i++) {
                String name = faker.name().fullName();
                if (giftCertificateRepo.existsByName(name)) {
                    i--;
                } else {
                    long firstTagId = faker.number().numberBetween(1L, 1000L);
                    long secondTagId = faker.number().numberBetween(1L, 1000L);
                    if (firstTagId != secondTagId) {
                        GiftCertificate giftCertificate = new GiftCertificate();
                        giftCertificate.setName(name);
                        giftCertificate.setDescription(faker.name().fullName());
                        giftCertificate.setPrice(faker.number().randomDigitNotZero());
                        giftCertificate.setDuration(faker.number().randomDigitNotZero());
                        giftCertificate.setTags(Set.of
                                (Tag.builder().id(firstTagId).build(),
                                        Tag.builder().id(secondTagId).build()));
                        giftCertificateRepo.save(giftCertificate);
                    } else {
                        i--;
                    }
                }
            }

            for (int i = 0; i < 1000; i++) {
                long id = faker.number().numberBetween(1L, 10000L);
                GiftCertificate giftCertificate = giftCertificateRepo.findById(id).orElse(new GiftCertificate());
                Order order = new Order();
                order.setCost(giftCertificate.getPrice());
                order.setGiftCertificate(giftCertificate);
                order.setUser(User.builder().id(faker.number().numberBetween(1L, 1000L)).build());
                orderRepo.save(order);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}