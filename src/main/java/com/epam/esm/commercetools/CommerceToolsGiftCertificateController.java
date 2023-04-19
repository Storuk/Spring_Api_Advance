package com.epam.esm.commercetools;

import com.epam.esm.commercetools.model.SortingTypesRequest;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import com.epam.esm.utils.VerificationOfRequestsData;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/commerce-tools/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommerceToolsGiftCertificateController {
    private final CommerceToolsGiftCertificateService commerceToolsGiftCertificateService;

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificateDTO giftCertificate) {
        if (VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate)) {
            return new ResponseEntity<>(Map.of("certificate",
                    commerceToolsGiftCertificateService.createGiftCertificate(giftCertificate)), HttpStatus.CREATED);
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getGiftCertificateById(@PathVariable String id) {
        if (VerificationOfRequestsData.isStringValuesCorrect(id)) {
            return ResponseEntity.ok(Map.of("giftCertificate",
                    commerceToolsGiftCertificateService.getGiftCertificateById(id)));
        }
        throw new InvalidDataException("Invalid Id format. Check your input");
    }

    @GetMapping
    public ResponseEntity<?> getAllCertificates(@RequestParam(value = "page", defaultValue = "0")
                                                @Min(value = 0, message = "Page index should be >= 0") int page,
                                                @RequestParam(value = "size", defaultValue = "20")
                                                @Min(value = 1, message = "Size should be should be >= 1") int size) {
        return ResponseEntity.ok(Map.of("giftCertificates",
                commerceToolsGiftCertificateService.getAllGiftCertificates(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable String id) {
        commerceToolsGiftCertificateService.deleteGiftCertificate(id);
        return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
    }

    @GetMapping("get-gift-certificate-description")
    public ResponseEntity<?> getGiftCertificatesByPartOfDescription(@RequestParam("description") String partOfDescription,
                                                                    @RequestParam(value = "page", defaultValue = "0")
                                                                    @Min(value = 0, message = "Page index should be >= 0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "20")
                                                                    @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(partOfDescription)) {
            return ResponseEntity.ok(Map.of("giftCertificates",
                    commerceToolsGiftCertificateService.getGiftCertificateByPartOfDescription(partOfDescription, page, size)));
        }
        throw new InvalidDataException("GiftCertificate name is not valid. Check your inputs.");
    }

    @GetMapping("get-sort-name")
    public ResponseEntity<?> getGiftCertificatesSortedByName(@RequestParam(value = "sortingType", defaultValue = "ASC") String sortingTypeName,
                                                             @RequestParam(value = "page", defaultValue = "0")
                                                             @Min(value = 0, message = "Page index should be >= 0") int page,
                                                             @RequestParam(value = "size", defaultValue = "20")
                                                             @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(sortingTypeName)) {
            if (VerificationOfRequestsData.isSortingTypeCorrect(sortingTypeName)) {
                return ResponseEntity.ok(Map.of("giftCertificates",
                        commerceToolsGiftCertificateService.getGiftCertificatesSortedByName(sortingTypeName, page, size)));
            }
            throw new InvalidDataException("ASC or DESC value allowed only");
        }
        throw new InvalidDataException("Incorrect data");
    }

    @GetMapping("get-sort-name-date")
    public ResponseEntity<?> getGiftCertificatesSortedByNameByDate(@RequestBody SortingTypesRequest sortingTypes,
                                                                   @RequestParam(value = "page", defaultValue = "0")
                                                                   @Min(value = 0, message = "Page index should be >= 0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "20")
                                                                   @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(sortingTypes.getSortingTypeName())
            && VerificationOfRequestsData.isStringValuesCorrect(sortingTypes.getSortingTypeDate())) {
            if (VerificationOfRequestsData.isListOfSortingTypesCorrect(List.of(sortingTypes.getSortingTypeName(), sortingTypes.getSortingTypeDate()))) {
                return ResponseEntity.ok(Map.of("giftCertificates",
                        commerceToolsGiftCertificateService.getGiftCertificatesSortedByNameByDate(sortingTypes, page, size)));
            }
            throw new InvalidDataException("ASC or DESC value allowed only");
        }
        throw new InvalidDataException("Incorrect data");
    }

    @GetMapping("get-by-tag-names")
    public ResponseEntity<?> getGiftCertificatesByTags(@RequestParam Set<String> tagNamesSet,
                                                       @RequestParam(value = "page", defaultValue = "0")
                                                       @Min(value = 0, message = "Page index should be >= 0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20")
                                                       @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isSetOfStringsCorrect(tagNamesSet)) {
            return ResponseEntity.ok(Map.of("giftCertificates",
                    commerceToolsGiftCertificateService.getGiftCertificatesByTags(tagNamesSet, page, size)));
        }
        throw new InvalidDataException("Some tag names is not valid: " + tagNamesSet);
    }

    @GetMapping("get-by-tag-names-graphql")
    public ResponseEntity<?> getGiftCertificatesByTagsGraphQl(@RequestParam Set<String> tagNamesSet,
                                                              @RequestParam(value = "page", defaultValue = "0")
                                                              @Min(value = 0, message = "Page index should be >= 0") int page,
                                                              @RequestParam(value = "size", defaultValue = "20")
                                                              @Min(value = 1, message = "Size should be should be >= 1") int size,
                                                              @RequestParam String operator) {
        if (VerificationOfRequestsData.isSetOfStringsCorrect(tagNamesSet)) {
            return ResponseEntity.ok(Map.of("giftCertificates",
                    commerceToolsGiftCertificateService.getGiftCertificatesByTagsName(tagNamesSet, page, size, operator)));
        }
        throw new InvalidDataException("Some tag names is not valid: " + tagNamesSet);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGiftCertificate(@PathVariable("id") String id,
                                                   @RequestBody GiftCertificateDTO giftCertificateDTO) {
        if (VerificationOfRequestsData.isStringValuesCorrect(id)) {
            if (VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificateDTO)) {
                return ResponseEntity.ok(Map.of("giftCertificate",
                        commerceToolsGiftCertificateService.updateGiftCertificate(id, giftCertificateDTO)));
            }
            throw new InvalidDataException("Nothing to update. All fields are empty");
        }
        throw new InvalidDataException("Invalid Id format. Check your input");
    }
}
