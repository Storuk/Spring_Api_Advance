package com.epam.esm.orders;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateRepo;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagRepo;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class OrderRepoTest {
    private final TagRepo tagRepo;
    private final GiftCertificateRepo giftCertificateRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    @Autowired
    OrderRepoTest(TagRepo tagRepo, GiftCertificateRepo giftCertificateRepo, UserRepo userRepo, OrderRepo orderRepo) {
        this.tagRepo = tagRepo;
        this.giftCertificateRepo = giftCertificateRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @Test
    void getOrderByUserId() {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        Tag thirdTag = Tag.builder().name("thirdTag").build();
        Tag fourthTag = Tag.builder().name("fourthTag").build();
        Tag fifthTag = Tag.builder().name("fifthTag").build();
        Tag sixthTag = Tag.builder().name("sixthTag").build();
        tagRepo.saveAll(List.of(firstTag,secondTag,thirdTag,fourthTag,fifthTag,sixthTag));

        Set<Tag> firstTagSet = Set.of(firstTag, secondTag, thirdTag);
        Set<Tag> secondTagSet = Set.of(fourthTag, fifthTag, sixthTag);
        Set<Tag> thirdTagSet = Set.of(thirdTag, fourthTag, fifthTag);

        GiftCertificate firstGiftCertificate = GiftCertificate.builder()
                .name("first").description("first").price(1).duration(1).tags(firstTagSet).build();
        GiftCertificate secondGiftCertificate = GiftCertificate.builder()
                .name("second").description("second").price(1).duration(1).tags(secondTagSet).build();
        GiftCertificate thirdGiftCertificate = GiftCertificate.builder()
                .name("third").description("third").price(1).duration(1).tags(thirdTagSet).build();
        giftCertificateRepo.saveAll(List.of(firstGiftCertificate,secondGiftCertificate,thirdGiftCertificate));

        User firstUser = User.builder().login("firstUser").build();
        userRepo.save(firstUser);

        Order firstOrder = Order.builder().user(firstUser).giftCertificate(firstGiftCertificate).cost(firstGiftCertificate.getPrice()).build();
        Order secondOrder = Order.builder().user(firstUser).giftCertificate(secondGiftCertificate).cost(secondGiftCertificate.getPrice()).build();
        Order thirdOrder = Order.builder().user(firstUser).giftCertificate(thirdGiftCertificate).cost(thirdGiftCertificate.getPrice()).build();
        orderRepo.saveAll(List.of(firstOrder,secondOrder,thirdOrder));

        List<Order> ordersList = List.of(firstOrder,secondOrder,thirdOrder);
        assertEquals(ordersList,
                orderRepo.getOrdersByUserId(firstUser.getId(), PageRequest.of(0,5)).stream().toList());
    }
}