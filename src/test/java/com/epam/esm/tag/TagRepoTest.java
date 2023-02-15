package com.epam.esm.tag;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateRepo;
import com.epam.esm.orders.Order;
import com.epam.esm.orders.OrderRepo;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TagRepoTest {
    private final TagRepo tagRepo;
    private final GiftCertificateRepo giftCertificateRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    @Autowired
    TagRepoTest(TagRepo tagRepo, GiftCertificateRepo giftCertificateRepo, UserRepo userRepo, OrderRepo orderRepo) {
        this.tagRepo = tagRepo;
        this.giftCertificateRepo = giftCertificateRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @Test
    void getTagIdByTag_Test() {
        Tag tag = Tag.builder().name("cheap").build();
        tagRepo.save(tag);
        assertEquals(tag.getId(), tagRepo.getTagIdByTagName(tag.getName()));
    }

    @ParameterizedTest
    @CsvSource({
            "cheap, true",
            "abc, false"
    })
    void tagExists_Tests(String name, boolean expected) {
        Tag tag = Tag.builder().name("cheap").build();
        tagRepo.save(tag);
        assertEquals(expected,tagRepo.tagExists(name));
    }

    @Test
    void getTheMostlyUsedTagTest() {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        Tag thirdTag = Tag.builder().name("thirdTag").build();
        Tag fourthTag = Tag.builder().name("fourthTag").build();
        Tag fifthTag = Tag.builder().name("fifthTag").build();
        Tag sixthTag = Tag.builder().name("sixthTag").build();
        tagRepo.saveAll(Set.of(firstTag,secondTag,thirdTag,fourthTag,fifthTag,sixthTag));

        Set<Tag> firstTagSet = Set.of(firstTag, secondTag, thirdTag);
        Set<Tag> secondTagSet = Set.of(fourthTag, fifthTag, sixthTag);
        Set<Tag> thirdTagSet = Set.of(thirdTag, fourthTag, fifthTag);

        GiftCertificate firstGiftCertificate = GiftCertificate.builder()
                .name("first").description("first").price(1).duration(1).tags(firstTagSet).build();
        GiftCertificate secondGiftCertificate = GiftCertificate.builder()
                .name("second").description("second").price(1).duration(1).tags(secondTagSet).build();
        GiftCertificate thirdGiftCertificate = GiftCertificate.builder()
                .name("third").description("third").price(1).duration(1).tags(thirdTagSet).build();
        giftCertificateRepo.saveAll(Set.of(firstGiftCertificate,secondGiftCertificate,thirdGiftCertificate));

        User firstUser = User.builder().login("firstUser").build();
        User secondUser = User.builder().login("secondUser").build();
        userRepo.saveAll(Set.of(firstUser,secondUser));

        Order firstOrder = Order.builder().user(firstUser).giftCertificate(firstGiftCertificate).cost(firstGiftCertificate.getPrice()).build();
        Order secondOrder = Order.builder().user(secondUser).giftCertificate(secondGiftCertificate).cost(secondGiftCertificate.getPrice()).build();
        Order thirdOrder = Order.builder().user(firstUser).giftCertificate(thirdGiftCertificate).cost(thirdGiftCertificate.getPrice()).build();
        orderRepo.saveAll(Set.of(firstOrder,secondOrder,thirdOrder));

        assertEquals(thirdTag, tagRepo.getTheMostlyUsedTag());
    }
}