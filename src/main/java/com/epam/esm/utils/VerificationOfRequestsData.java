package com.epam.esm.utils;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import com.epam.esm.tag.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vlad Storoshchuk
 */
public class VerificationOfRequestsData {
    /**
     * Method for checking if GiftCertificate for updating is correct
     *
     * @return boolean
     */
    public static boolean isGiftCertificateValidForUpdate(GiftCertificateDTO giftCertificate) {
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
    public static boolean isNewCertificateCorrect(GiftCertificateDTO giftCertificateDTO) {
        return giftCertificateDTO != null && giftCertificateDTO.getPrice() != null && giftCertificateDTO.getPrice() > 0
                && giftCertificateDTO.getDuration() != null && giftCertificateDTO.getDuration() > 0
                && isStringValuesCorrect(giftCertificateDTO.getName()) && isStringValuesCorrect(giftCertificateDTO.getDescription());
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
    public static boolean isListOfSortingTypesCorrect(List<String> sortingTypesList) {
        for (String sortingType : sortingTypesList) {
            if (!isSortingTypeCorrect(sortingType)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isChangeUserPasswordRequestCorrect(ChangeUserPasswordRequest request){
        return request.getEmail() != null && isEmailCorrect(request.getEmail())
                && request.getPassword() != null && isPasswordCorrect(request.getPassword())
                && request.getRepeatPassword() != null && request.getPassword().equals(request.getRepeatPassword())
                && request.getVerificationCode() != null;
    }

    public static boolean isRegistrationRequestCorrect(RegistrationRequest request){
        return request.getEmail() != null && isEmailCorrect(request.getEmail())
                && request.getPassword() != null && isPasswordCorrect(request.getPassword())
                && request.getRepeatPassword() != null && request.getPassword().equals(request.getRepeatPassword());
    }

    public static boolean isAuthenticationRequestCorrect(AuthenticationRequest request){
        return request.getEmail() != null && isEmailCorrect(request.getEmail())
                && request.getPassword() != null && isPasswordCorrect(request.getPassword());
    }

    public static boolean isEmailCorrect(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordCorrect(String password)
    {
        if (password != null) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+-=])(?=\\S+$).{8,20}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(password);
            return m.matches();
        }
        return false;
    }

    /**
     * Method for checking if Tag correct
     *
     * @return boolean
     */
    public static boolean isTagCorrect(TagDTO tag) {
        return tag != null && isStringValuesCorrect(tag.getName());
    }

    /**
     * Method for checking if set of Tags correct
     *
     * @return boolean
     */
    public static boolean isSetOfTagsCorrect(Set<TagDTO> tags) {
        if (tags != null) {
            for (TagDTO tag : tags) {
                if (!isTagCorrect(tag)) {
                    return false;
                }
            }
        }
        return true;
    }
}