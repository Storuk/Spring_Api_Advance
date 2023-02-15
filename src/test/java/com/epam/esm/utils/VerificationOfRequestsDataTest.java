package com.epam.esm.utils;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VerificationOfRequestsDataTest {
    @ParameterizedTest
    @CsvSource({
            "string, true",
            "string123, true"

    })
    void isStringCorrectTest_True(String value, boolean expected) {
        assertTrue(VerificationOfRequestsData.isStringValuesCorrect("string"));
        assertEquals(expected, VerificationOfRequestsData.isStringValuesCorrect(value));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ","123"})
    void isStringCorrectTest_False(String value) {
        assertFalse(VerificationOfRequestsData.isStringValuesCorrect(value));
    }

    @Test
    void isSetOfStringsCorrectTest_True() {
        Set<String> stringSet = Set.of("2ha2rd1", "exp12", "yep37");
        assertTrue(VerificationOfRequestsData.isSetOfStringsCorrect(stringSet));
    }

    @Test
    void isSetOfStringsCorrectTest_False() {
        Set<String> value = new HashSet<>(Arrays.asList(null,"", "   ", "123"));
        assertFalse(VerificationOfRequestsData.isSetOfStringsCorrect(value));
    }

    @Test
    void isTagCorrectTest_True() {
        assertTrue(VerificationOfRequestsData.isTagCorrect(Tag.builder().name("exp12").build()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ","123"})
    void isTagCorrectTest_FalseWhenEmptyOrBlankOrNullOrNumeric(String value) {
        assertFalse(VerificationOfRequestsData.isTagCorrect(Tag.builder().name(value).build()));
    }

    @ParameterizedTest
    @CsvSource({
            "ASC, true",
            "AsC, true",
            "Desc, true",
            "desc, true",
            "123, false",
    })
    void isSortingTypeCorrectTest(String value, boolean expected) {
        assertEquals(expected,VerificationOfRequestsData.isSortingTypeCorrect(value));
    }

    @Test
    void isSetOfSortingTypesCorrectTrue() {
        Set<String> sortingTypesSet = Set.of("AsC", "DesC","asc","desc","ASC","DESC");
        assertTrue(VerificationOfRequestsData.isSetOfSortingTypesCorrect(sortingTypesSet));
    }

    @Test
    void isSetOfSortingTypesCorrectFalse() {
        Set<String> sortingTypesSet = Set.of("AsC1", "Des1C","123","");
        assertFalse(VerificationOfRequestsData.isSetOfSortingTypesCorrect(sortingTypesSet));
    }

    @Test
    void isNewCertificateCorrectTestTrue() {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        assertTrue(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectName() {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("123").description("certificate").price(1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectDescription() {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("   ").price(1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectPrice() {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(-1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectDuration() {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(-1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isSetOfTagsCorrectTestTrue() {
        Set<Tag> tagSet = Set.of(Tag.builder().name("2ha2rd1").build(),
                Tag.builder().name("exp12").build(),
                Tag.builder().name("yep37").build());
        assertTrue(VerificationOfRequestsData.isSetOfTagsCorrect(tagSet));
    }

    @Test
    void isSetOfTagsCorrectTestFalse_EmptyNullAndNumeric() {
        Set<Tag> tagSet = Set.of(Tag.builder().name(" ").build(),
                Tag.builder().name(null).build(),
                Tag.builder().name("356").build());
        assertFalse(VerificationOfRequestsData.isSetOfTagsCorrect(tagSet));
    }

    @Test
    void isGiftCertificateValidForUpdateTestTrue() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("certificate").description("certificate").price(1).duration(1)
                .tags(Set.of(Tag.builder().name("2ha2rd1").build(),
                        Tag.builder().name("exp12").build(),
                        Tag.builder().name("yep37").build())).build();
        assertTrue(VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenNull() {
        assertFalse(VerificationOfRequestsData.isGiftCertificateValidForUpdate(null));
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidName() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("123").build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input name: " + giftCertificate.getName(),message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidDescription() {
        GiftCertificate giftCertificate = GiftCertificate.builder().description("  ").build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input description: " + giftCertificate.getDescription(),message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidPrice() {
        GiftCertificate giftCertificate = GiftCertificate.builder().price(-1).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Price should be > 0. Your value " + giftCertificate.getPrice(),message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidDuration() {
        GiftCertificate giftCertificate = GiftCertificate.builder().duration(-1).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Duration should be > 0. Your value " + giftCertificate.getDuration(),message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidTags() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .tags(Set.of(Tag.builder().name(" ").build(),
                Tag.builder().name(null).build(),
                Tag.builder().name("356").build())).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input tags." + giftCertificate.getTags(),message.getMessage());
    }

}