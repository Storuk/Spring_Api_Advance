package com.epam.esm.giftcertificate;

import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GiftCertificateRepoTest {
    private final TagRepo tagRepo;
    private final GiftCertificateRepo giftCertificateRepo;

    @Autowired
    GiftCertificateRepoTest(TagRepo tagRepo, GiftCertificateRepo giftCertificateRepo) {
        this.tagRepo = tagRepo;
        this.giftCertificateRepo = giftCertificateRepo;
    }

    @ParameterizedTest
    @CsvSource({
            "giftCertificate, true",
            "abc, false"
    })
    void giftCertificateExistsTests(String name, boolean expected) {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        tagRepo.saveAll(List.of(firstTag, secondTag));

        Set<Tag> tagSet = Set.of(firstTag, secondTag);

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("giftCertificate").description("giftCertificate").price(1).duration(1).tags(tagSet).build();
        giftCertificateRepo.save(giftCertificate);
        assertEquals(expected, giftCertificateRepo.giftCertificateExists(name));
    }

    @Test
    void getGiftCertificatesByTagName() {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        Tag thirdTag = Tag.builder().name("thirdTag").build();
        Tag fourthTag = Tag.builder().name("fourthTag").build();
        tagRepo.saveAll(List.of(firstTag, secondTag, thirdTag, fourthTag));

        Set<Tag> firstTagSet = Set.of(firstTag, secondTag);
        Set<Tag> secondTagSet = Set.of(thirdTag, fourthTag);
        Set<Tag> thirdTagSet = Set.of(firstTag, fourthTag);

        GiftCertificate firstGiftCertificate = GiftCertificate.builder()
                .name("first").description("first").price(1).duration(1).tags(firstTagSet).build();
        GiftCertificate secondGiftCertificate = GiftCertificate.builder()
                .name("second").description("second").price(1).duration(1).tags(secondTagSet).build();
        GiftCertificate thirdGiftCertificate = GiftCertificate.builder()
                .name("third").description("third").price(1).duration(1).tags(thirdTagSet).build();
        giftCertificateRepo.saveAll(List.of(firstGiftCertificate, secondGiftCertificate, thirdGiftCertificate));

        assertEquals(List.of(firstGiftCertificate, thirdGiftCertificate),
                giftCertificateRepo.getGiftCertificatesByTagName("firstTag", 0, 3));
    }

    @Test
    void getGiftCertificateByPartOfDescription() {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        tagRepo.saveAll(List.of(firstTag, secondTag));

        Set<Tag> firstTagSet = Set.of(firstTag, secondTag);
        Set<Tag> secondTagSet = Set.of(firstTag, secondTag);

        GiftCertificate firstGiftCertificate = GiftCertificate.builder()
                .name("first").description("description").price(1).duration(1).tags(firstTagSet).build();
        GiftCertificate secondGiftCertificate = GiftCertificate.builder()
                .name("second").description("descr").price(1).duration(1).tags(secondTagSet).build();

        giftCertificateRepo.saveAll(List.of(firstGiftCertificate, secondGiftCertificate));

        assertEquals(List.of(firstGiftCertificate, secondGiftCertificate),
                giftCertificateRepo.getGiftCertificateByPartOfDescription("des%", 0, 3));
    }

    @Test
    void getGiftCertificatesByTags() {
        Tag firstTag = Tag.builder().name("firstTag").build();
        Tag secondTag = Tag.builder().name("secondTag").build();
        tagRepo.saveAll(List.of(firstTag, secondTag));

        Set<Tag> firstTagSet = Set.of(firstTag, secondTag);
        Set<Tag> secondTagSet = Set.of(firstTag, secondTag);

        GiftCertificate firstGiftCertificate = GiftCertificate.builder()
                .name("first").description("description").price(1).duration(1).tags(firstTagSet).build();
        GiftCertificate secondGiftCertificate = GiftCertificate.builder()
                .name("second").description("second").price(1).duration(1).tags(secondTagSet).build();

        giftCertificateRepo.saveAll(List.of(firstGiftCertificate, secondGiftCertificate));

        Set<String> tagNames = Set.of(firstTag.getName(), secondTag.getName());
        assertEquals(List.of(firstGiftCertificate, secondGiftCertificate),
                giftCertificateRepo.getByTags(tagNames, tagNames.size(), 0, 3));
    }
}