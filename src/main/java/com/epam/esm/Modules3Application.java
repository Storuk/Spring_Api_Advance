package com.epam.esm;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateRepo;
import com.epam.esm.orders.Order;
import com.epam.esm.orders.OrderRepo;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagRepo;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
public class Modules3Application {

    public static void main(String[] args) {
        SpringApplication.run(Modules3Application.class, args);
    }

    @Bean
    public CommandLineRunner generateData(GiftCertificateRepo giftCertificateRepo,
                                          TagRepo tagRepo, UserRepo userRepo, OrderRepo orderRepo){
        return args -> {
            Faker faker = new Faker();
            for(int i = 0; i < 1000; i++){
                User user = new User();
                user.setLogin(faker.name().fullName());
                userRepo.save(user);
            }

            for(int i = 0; i < 1000; i++){
                String name = faker.name().fullName();
                if(tagRepo.tagExists(name)) {
                    i--;
                }else{
                    tagRepo.save(Tag.builder().name(name).build());
                }
            }

            for(int i = 0; i < 10000; i++){
                String name = faker.name().fullName();
                if(giftCertificateRepo.giftCertificateExists(name)) {
                    i--;
                }else{
                    long firstTagId = faker.number().numberBetween(1L, 1000L);
                    long secondTagId = faker.number().numberBetween(1L, 1000L);
                    if(firstTagId != secondTagId) {
                        GiftCertificate giftCertificate = new GiftCertificate();
                        giftCertificate.setName(name);
                        giftCertificate.setDescription(faker.name().fullName());
                        giftCertificate.setPrice(faker.number().randomDigitNotZero());
                        giftCertificate.setDuration(faker.number().randomDigitNotZero());
                        giftCertificate.setTags(Set.of
                                (Tag.builder().id(firstTagId).build(),
                                        Tag.builder().id(secondTagId).build()));
                        giftCertificateRepo.save(giftCertificate);
                    }else{
                        i--;
                    }
                }
            }

            for(int i = 0; i < 1000; i++){
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
}
