package com.epam.esm.utils;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.tag.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @author Vlad Storoshchuk
 */
public class VerificationOfRequestsData {
    /**
     * Method for checking if GiftCertificate for updating is correct
     *
     * @return boolean
     */
    public static boolean isGiftCertificateValidForUpdate(GiftCertificate giftCertificate) {
        if (giftCertificate != null) {
            if (giftCertificate.getName() != null && !isStringValuesCorrect(giftCertificate.getName())) {
                throw new InvalidDataException("Invalid input name: " + giftCertificate.getName());
            }
            if (giftCertificate.getDescription() != null && !isStringValuesCorrect(giftCertificate.getDescription())) {
                throw new InvalidDataException("Invalid input description: " + giftCertificate.getDescription());
            }
            if (giftCertificate.getPrice() != null && giftCertificate.getPrice() <= 0) {
                throw new InvalidDataException("Price should be > 0. Your value " + giftCertificate.getPrice());
            }
            if (giftCertificate.getDuration() != null && giftCertificate.getDuration() <= 0) {
                throw new InvalidDataException("Duration should be > 0. Your value " + giftCertificate.getDuration());
            }
            if (giftCertificate.getTags() != null && !isSetOfTagsCorrect(giftCertificate.getTags())) {
                throw new InvalidDataException("Invalid input tags." + giftCertificate.getTags());
            }
            return true;
        }
        return false;
    }

    /**
     * Method for checking if GiftCertificate for creating is correct
     *
     * @return boolean
     */
    public static boolean isNewCertificateCorrect(GiftCertificate giftCertificate) {
        return giftCertificate != null && giftCertificate.getPrice() != null && giftCertificate.getPrice() > 0
                && giftCertificate.getDuration() != null && giftCertificate.getDuration() > 0
                && isStringValuesCorrect(giftCertificate.getName()) && isStringValuesCorrect(giftCertificate.getDescription());
    }

    /**
     * Method for checking if string is correct
     *
     * @return boolean
     */
    public static boolean isStringValuesCorrect(String s) {
        return s != null && !StringUtils.isBlank(s) && !StringUtils.isNumeric(s);
    }

    /**
     * Method for checking if set of strings is correct
     *
     * @return boolean
     */
    public static boolean isSetOfStringsCorrect(Set<String> stringSet) {
        if (stringSet != null) {
            for (String string : stringSet) {
                if (!isStringValuesCorrect(string)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method for checking if string equalsIgnoreCase ASC or DESC
     *
     * @return boolean
     */
    public static boolean isSortingTypeCorrect(String method) {
        return method.equalsIgnoreCase("ASC") || method.equalsIgnoreCase("DESC");
    }

    /**
     * Method for checking if set of strings equalsIgnoreCase ASC or DESC
     *
     * @return boolean
     */
    public static boolean isSetOfSortingTypesCorrect(Set<String> sortingTypesSet) {
        for (String sortingType : sortingTypesSet) {
            if (!isSortingTypeCorrect(sortingType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method for checking if Tag correct
     *
     * @return boolean
     */
    public static boolean isTagCorrect(Tag tag) {
        return tag != null && isStringValuesCorrect(tag.getName());
    }

    /**
     * Method for checking if set of Tags correct
     *
     * @return boolean
     */
    public static boolean isSetOfTagsCorrect(Set<Tag> tags) {
        if (tags != null) {
            for (Tag tag : tags) {
                if (!isTagCorrect(tag)) {
                    return false;
                }
            }
        }
        return true;
    }
}