package com.epam.esm.giftcertificate;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.utils.VerificationOfRequestsData;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class GiftCertificateController which contain method related with GiftCertificate
 *
 * @author Vlad Storoshchuk
 */
@Validated
@RestController
@RequestMapping(value = "/gift-certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateHateoasMapper hateoasMapper;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateHateoasMapper hateoasMapper) {
        this.giftCertificateService = giftCertificateService;
        this.hateoasMapper = hateoasMapper;
    }

    /**
     * A controller get method for getting GiftCertificate
     *
     * @param id - id of GiftCertificate (min value 1)
     * @return CollectionModel of GiftCertificate with links
     * @see GiftCertificateService#getGiftCertificateById(long)
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getGiftCertificateById(@PathVariable @Min(value = 1, message = "Id should be >= 1") long id) {
        GiftCertificate giftCertificate = giftCertificateService.getGiftCertificateById(id);
        return ResponseEntity.ok(Map.of("giftCertificate",
                hateoasMapper.getGiftCertificateByIdHateoasMapper(giftCertificate)));
    }

    /**
     * A controller delete method for deleting GiftCertificate
     *
     * @param id - id of GiftCertificate for deleting (min value 1)
     * @return HttpStatus OK
     * @see GiftCertificateService#deleteGiftCertificate(long)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable @Min(value = 1, message = "Id should be >= 1") long id) {
        giftCertificateService.deleteGiftCertificate(id);
        return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
    }

    /**
     * A controller post method for creating GiftCertificate
     *
     * @param giftCertificate the GiftCertificate object that will be created in database
     * @return CollectionModel of GiftCertificate with links
     * @see GiftCertificateService#createGiftCertificate(GiftCertificateDTO)
     */
    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificateDTO giftCertificate) {
        if (VerificationOfRequestsData.isNewCertificateCorrect(giftCertificate)) {
            GiftCertificate createdGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificate);
            return new ResponseEntity<>(Map.of("certificate",
                    hateoasMapper.getGiftCertificateForCreateHateoasMapper(createdGiftCertificate)), HttpStatus.CREATED);
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }

    /**
     * A controller patch method for updating GiftCertificate
     *
     * @param id              - id of gift certificate (min value 1)
     * @param giftCertificate the GiftCertificate object for updating giftCertificate
     * @return CollectionModel of GiftCertificate with links
     * @see GiftCertificateService#updateGiftCertificate(long, GiftCertificateDTO)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGiftCertificate(@PathVariable("id")
                                                   @Min(value = 1, message = "Id should be >= 1") long id,
                                                   @RequestBody GiftCertificateDTO giftCertificate) {
        if (VerificationOfRequestsData.isGiftCertificateValidForUpdate(giftCertificate)) {
            GiftCertificate updatedGiftCertificate = giftCertificateService.updateGiftCertificate(id, giftCertificate);
            return ResponseEntity.ok(Map.of("giftCertificate",
                    hateoasMapper.getGiftCertificateForUpdateHateoasMapper(updatedGiftCertificate)));
        }
        throw new InvalidDataException("Nothing to update. All fields are empty");
    }

    /**
     * A controller patch method for updating GiftCertificate duration
     *
     * @param id       - id of gift certificate (min value 1)
     * @param duration - duration for updating (min value 1)
     * @return CollectionModel of GiftCertificate with links
     * @see GiftCertificateService#updateDuration(long, int)
     */
    @PatchMapping("update-duration/{id}")
    public ResponseEntity<?> updateDuration(@PathVariable("id") @Min(value = 1, message = "Id should be >= 1") long id,
                                            @RequestBody @Min(value = 1, message = "Price should be >= 1") int duration) {
        GiftCertificate updatedGiftCertificate = giftCertificateService.updateDuration(id, duration);
        return ResponseEntity.ok(Map.of("giftCertificate",
                hateoasMapper.getGiftCertificateForUpdateDurationHateoasMapper(updatedGiftCertificate)));
    }

    /**
     * A controller patch method for updating GiftCertificate price
     *
     * @param id    - id of gift certificate (min value 1)
     * @param price - price for updating (min value 1)
     * @return CollectionModel of GiftCertificate with links
     * @see GiftCertificateService#updatePrice(long, int)
     */
    @PatchMapping("update-price/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") @Min(value = 1, message = "Id should be >= 1") long id,
                                         @RequestBody @Min(value = 1, message = "Price should be >= 1") int price) {
        GiftCertificate updatedGiftCertificate = giftCertificateService.updatePrice(id, price);
        return ResponseEntity.ok(Map.of("giftCertificate",
                hateoasMapper.getGiftCertificateForUpdatePriceHateoasMapper(updatedGiftCertificate)));
    }

    /**
     * A controller get method for getting all GiftCertificates
     * Values for Pagination
     *
     * @param page - number of page (min value 0)
     * @param size - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getAllGiftCertificates(int, int)
     */
    @GetMapping
    public ResponseEntity<?> getAllCertificates(@RequestParam(value = "page", defaultValue = "0")
                                                @Min(value = 0, message = "Page index should be >= 0") int page,
                                                @RequestParam(value = "size", defaultValue = "20")
                                                @Min(value = 1, message = "Size should be should be >= 1") int size) {
        Page<GiftCertificate> allGiftCertificates = giftCertificateService.getAllGiftCertificates(page, size);
        PagedModel<GiftCertificate> allCertificatesHateoasMapper = hateoasMapper.getAllCertificatesHateoasMapper(allGiftCertificates);
        return ResponseEntity.ok(Map.of("giftCertificates", allCertificatesHateoasMapper));
    }

    /**
     * A controller get method for getting all GiftCertificates by tag name
     *
     * @param tagName - tagName for getting all gift certificates
     *                Values for Pagination
     * @param page    - number of page (min value 0)
     * @param size    - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getGiftCertificatesByTagName(String, int, int)
     */
    @GetMapping("get-tag-name")
    public ResponseEntity<?> getGiftCertificatesByTagName(@RequestParam("name") String tagName,
                                                          @RequestParam(value = "page", defaultValue = "0")
                                                          @Min(value = 0, message = "Page index should be >= 0") int page,
                                                          @RequestParam(value = "size", defaultValue = "20")
                                                          @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(tagName)) {
            Page<GiftCertificate> giftCertificatesByTagName = giftCertificateService.getGiftCertificatesByTagName(tagName, page, size);
            return ResponseEntity.ok(Map.of("giftCertificates",
                    hateoasMapper.getAllCertificatesByTagNameHateoasMapper(giftCertificatesByTagName)));
        }
        throw new InvalidDataException("Tag name is not valid. Check your inputs.");
    }

    /**
     * A controller get method for getting all GiftCertificates by tags names
     *
     * @param tagNamesSet - set of tagNames for getting all gift certificates
     *                    Values for Pagination
     * @param page        - number of page (min value 0)
     * @param size        - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getGiftCertificatesByTags(Set, int, int)
     */
    @GetMapping("get-by-tag-names")
    public ResponseEntity<?> getGiftCertificatesByTags(@RequestParam Set<String> tagNamesSet,
                                                       @RequestParam(value = "page", defaultValue = "0")
                                                       @Min(value = 0, message = "Page index should be >= 0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20")
                                                       @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isSetOfStringsCorrect(tagNamesSet)) {
            Page<GiftCertificate> giftCertificates = giftCertificateService
                    .getGiftCertificatesByTags(tagNamesSet, page, size);
            return ResponseEntity.ok(Map.of("giftCertificates",
                    hateoasMapper.getAllCertificatesByTagsHateoasMapper(giftCertificates)));
        }
        throw new InvalidDataException("Some tag names is not valid: " + tagNamesSet);
    }

    /**
     * A controller get method for getting all GiftCertificates by part of Description
     *
     * @param partOfDescription - description values for getting all gift certificates
     *                          Values for Pagination
     * @param page              - number of page (min value 0)
     * @param size              - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getGiftCertificateByPartOfDescription(String, int, int)
     */
    @GetMapping("get-gift-certificate-description")
    public ResponseEntity<?> getGiftCertificatesByPartOfDescription(@RequestParam("description") String partOfDescription,
                                                                    @RequestParam(value = "page", defaultValue = "0")
                                                                    @Min(value = 0, message = "Page index should be >= 0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "20")
                                                                    @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(partOfDescription)) {
            Page<GiftCertificate> giftCertificatesByName = giftCertificateService.getGiftCertificateByPartOfDescription(partOfDescription, page, size);
            return ResponseEntity.ok(Map.of("giftCertificates",
                    hateoasMapper.getAllCertificatesByPartOfDescriptionHateoasMapper(giftCertificatesByName)));
        }
        throw new InvalidDataException("GiftCertificate name is not valid. Check your inputs.");
    }

    /**
     * A controller get method for getting all GiftCertificates sorted by name
     *
     * @param sortingTypeName - sorting type for name (ASC OR DESC)
     *                        Values for Pagination
     * @param page            - number of page (min value 0)
     * @param size            - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getGiftCertificatesSortedByName(String, int, int)
     */
    @GetMapping("get-sort-name")
    public ResponseEntity<?> getGiftCertificatesSortedByName(@RequestParam(value = "sortingType", defaultValue = "ASC") String sortingTypeName,
                                                             @RequestParam(value = "page", defaultValue = "0")
                                                             @Min(value = 0, message = "Page index should be >= 0") int page,
                                                             @RequestParam(value = "size", defaultValue = "20")
                                                             @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(sortingTypeName)) {
            if (VerificationOfRequestsData.isSortingTypeCorrect(sortingTypeName)) {
                Page<GiftCertificate> giftCertificatesSortedByDate = giftCertificateService.getGiftCertificatesSortedByName(sortingTypeName, page, size);
                return ResponseEntity.ok(Map.of("giftCertificates",
                        hateoasMapper.getAllCertificatesSortedByNameHateoasMapper(giftCertificatesSortedByDate)));
            }
            throw new InvalidDataException("ASC or DESC value allowed only");
        }
        throw new InvalidDataException("Incorrect data");
    }

    /**
     * A controller get method for getting all GiftCertificates sorted by name and by date
     *
     * @param sortingTypeName - sorting type for name (ASC OR DESC)
     * @param sortingTypeDate - sorting type for date (ASC OR DESC)
     *                        Values for Pagination
     * @param page            - number of page (min value 0)
     * @param size            - count of tags (min value 1)
     * @return PagedModel of GiftCertificates with links
     * @see GiftCertificateService#getGiftCertificatesSortedByNameByDate(String, String, int, int)
     */
    @GetMapping("get-sort-name-date")
    public ResponseEntity<?> getGiftCertificatesSortedByNameByDate(@RequestParam(value = "sortingTypeName", defaultValue = "ASC") String sortingTypeName,
                                                                   @RequestParam(value = "sortingTypeDate", defaultValue = "ASC") String sortingTypeDate,
                                                                   @RequestParam(value = "page", defaultValue = "0")
                                                                   @Min(value = 0, message = "Page index should be >= 0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "20")
                                                                   @Min(value = 1, message = "Size should be should be >= 1") int size) {
        if (VerificationOfRequestsData.isStringValuesCorrect(sortingTypeName) && VerificationOfRequestsData.isStringValuesCorrect(sortingTypeDate)) {
            if (VerificationOfRequestsData.isListOfSortingTypesCorrect(List.of(sortingTypeName, sortingTypeDate))) {
                Page<GiftCertificate> giftCertificatesSortedByNameByDate = giftCertificateService
                        .getGiftCertificatesSortedByNameByDate(sortingTypeName, sortingTypeDate, page, size);
                return ResponseEntity.ok(Map.of("giftCertificates",
                        hateoasMapper.getAllCertificatesSortedByNameAndByDateHateoasMapper(giftCertificatesSortedByNameByDate)));
            }
            throw new InvalidDataException("ASC or DESC value allowed only");
        }
        throw new InvalidDataException("Incorrect data");
    }
}