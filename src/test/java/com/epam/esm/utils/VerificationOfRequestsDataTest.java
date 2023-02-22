package com.epam.esm.utils;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import com.epam.esm.tag.TagDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @ValueSource(strings = {"  ", "123"})
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
        Set<String> value = new HashSet<>(Arrays.asList(null, "", "   ", "123"));
        assertFalse(VerificationOfRequestsData.isSetOfStringsCorrect(value));
    }

    @Test
    void isTagCorrectTest_True() {
        assertTrue(VerificationOfRequestsData.isTagCorrect(TagDTO.builder().name("exp12").build()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "123"})
    void isTagCorrectTest_FalseWhenEmptyOrBlankOrNullOrNumeric(String value) {
        assertFalse(VerificationOfRequestsData.isTagCorrect(TagDTO.builder().name(value).build()));
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
        assertEquals(expected, VerificationOfRequestsData.isSortingTypeCorrect(value));
    }

    @Test
    void isListOfSortingTypesCorrectTrue() {
        List<String> sortingTypesList = List.of("AsC", "DesC", "asc", "desc", "ASC", "DESC");
        assertTrue(VerificationOfRequestsData.isListOfSortingTypesCorrect(sortingTypesList));
    }

    @Test
    void isListOfSortingTypesCorrectFalse() {
        List<String> sortingTypesList = List.of("AsC1", "Des1C", "123", "");
        assertFalse(VerificationOfRequestsData.isListOfSortingTypesCorrect(sortingTypesList));
    }

    @Test
    void isNewCertificateCorrectTestTrue() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        assertTrue(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectName() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().
                name("123").description("certificate").price(1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectDescription() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().
                name("certificate").description("   ").price(1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectPrice() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().
                name("certificate").description("certificate").price(-1).duration(1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isNewCertificateCorrectTestFalse_WhenIncorrectDuration() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().
                name("certificate").description("certificate").price(1).duration(-1).build();
        assertFalse(VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate));
    }

    @Test
    void isSetOfTagsCorrectTestTrue() {
        Set<TagDTO> tagSet = Set.of(TagDTO.builder().name("2ha2rd1").build(),
                TagDTO.builder().name("exp12").build(),
                TagDTO.builder().name("yep37").build());
        assertTrue(VerificationOfRequestsData.isSetOfTagsCorrect(tagSet));
    }

    @Test
    void isSetOfTagsCorrectTestFalse_EmptyNullAndNumeric() {
        Set<TagDTO> tagSet = Set.of(TagDTO.builder().name(" ").build(),
                TagDTO.builder().name(null).build(),
                TagDTO.builder().name("356").build());
        assertFalse(VerificationOfRequestsData.isSetOfTagsCorrect(tagSet));
    }

    @Test
    void isGiftCertificateValidForUpdateTestTrue() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder()
                .name("certificate").description("certificate").price(1).duration(1)
                .tags(Set.of(TagDTO.builder().name("2ha2rd1").build(),
                        TagDTO.builder().name("exp12").build(),
                        TagDTO.builder().name("yep37").build())).build();
        assertTrue(VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenNull() {
        assertFalse(VerificationOfRequestsData.isGiftCertificateValidForUpdate(null));
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidName() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().name("123").build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input name: " + giftCertificate.getName(), message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidDescription() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().description("  ").build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input description: " + giftCertificate.getDescription(), message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidPrice() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().price(-1).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Price should be > 0. Your value " + giftCertificate.getPrice(), message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidDuration() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder().duration(-1).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Duration should be > 0. Your value " + giftCertificate.getDuration(), message.getMessage());
    }

    @Test
    void isGiftCertificateValidForUpdateTestFalse_WhenInvalidTags() {
        GiftCertificateDTO giftCertificate = GiftCertificateDTO.builder()
                .tags(Set.of(TagDTO.builder().name(" ").build(),
                        TagDTO.builder().name(null).build(),
                        TagDTO.builder().name("356").build())).build();
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate));
        assertEquals("Invalid input tags." + giftCertificate.getTags(), message.getMessage());
    }

}