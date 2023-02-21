package com.epam.esm.giftcertificate;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.tag.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Vlad Storoshchuk
 */
@Service
@EnableTransactionManagement
public class GiftCertificateService {
    private final GiftCertificateRepo giftCertificateRepo;
    private final TagService tagService;

    public GiftCertificateService(GiftCertificateRepo giftCertificateRepo, TagService tagService) {
        this.giftCertificateRepo = giftCertificateRepo;
        this.tagService = tagService;
    }

    /**
     * A service method for creating gift certificate
     *
     * @param giftCertificate the GiftCertificate object that will be created in database
     * @return saved GiftCertificate
     * @see GiftCertificateRepo#existsByName(String) for checking if GiftCertificate already exists
     * @see TagService#saveAllTags(Set) for saving all Tags
     * @see GiftCertificateRepo#save(Object) for saving GiftCertificate
     */
    @Transactional
    public GiftCertificate createGiftCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateRepo.existsByName(giftCertificate.getName())) {
            if(giftCertificate.getId() != null){
                giftCertificate.setId(null);
            }
            if (giftCertificate.getTags() != null) {
                tagService.saveAllTags(giftCertificate.getTags());
            }
            return giftCertificateRepo.save(giftCertificate);
        } else {
            throw new InvalidDataException("Certificate already exists: " + giftCertificate.getName());
        }
    }

    /**
     * A service method for getting all gift certificates
     *
     * @param page - number of page (min value 0)
     * @param size - count of tags (min value 1)
     * @return PageModel of all GiftCertificates
     * @see GiftCertificateRepo#findAll() for getting all GiftCertificates
     */
    public Page<GiftCertificate> getAllGiftCertificates(int page, int size) {
        return Optional.of(giftCertificateRepo.findAll(PageRequest.of(page, size))).orElse(Page.empty(PageRequest.of(page, size)));
    }

    /**
     * A service method for getting gift certificate by id
     *
     * @param id - GiftCertificateId (min value 1)
     * @return GiftCertificate
     * @see GiftCertificateRepo#findById(Object) for getting GiftCertificate
     */
    public GiftCertificate getGiftCertificateById(long id) {
        return giftCertificateRepo.findById(id).orElseThrow(
                () -> new ItemNotFoundException("No gift certificate with (id = " + id + ")"));
    }

    /**
     * A service method for deleting gift certificate
     *
     * @param id - GiftCertificateId (min value 1)
     * @return true
     * @see GiftCertificateRepo#existsByName(String) for checking if GiftCertificate already exists
     * @see GiftCertificateRepo#deleteById(Object) for deleting GiftCertificate by id
     */
    public boolean deleteGiftCertificate(long id) {
        if (giftCertificateRepo.existsById(id)) {
            giftCertificateRepo.deleteById(id);
            return true;
        }
        throw new ItemNotFoundException("No GiftCertificate with (id = " + id + ")");
    }

    /**
     * A service method for updating gift certificate
     *
     * @param id              - GiftCertificateId (min value 1)
     * @param giftCertificate the GiftCertificate object for updating GiftCertificate
     * @return true
     * @see GiftCertificateRepo#existsByName(String) for checking if GiftCertificate already exists
     * @see GiftCertificateRepo#save(Object)
     */
    @Transactional
    @Modifying
    public GiftCertificate updateGiftCertificate(long id, GiftCertificate giftCertificate) {
        if (giftCertificateRepo.existsByName(giftCertificate.getName())
                && !giftCertificate.getName().equals(getGiftCertificateById(id).getName())) {
            throw new ItemNotFoundException("Gift certificate with such name already exists " + giftCertificate.getName());
        } else {
            GiftCertificate giftCertificateForUpdate = makeGiftCertificateForUpdate(giftCertificate, getGiftCertificateById(id));
            return giftCertificateRepo.save(giftCertificateForUpdate);
        }
    }

    /**
     * A service method for making gift certificate for update
     *
     * @param giftCertificateWithUpdates - GiftCertificate object from request which contains values for update
     * @param giftCertificateForUpdates  GiftCertificate object from database with current values
     * @return GiftCertificate which already contain values from request
     * @see TagService#saveAllTags(Set)
     */
    private GiftCertificate makeGiftCertificateForUpdate(GiftCertificate giftCertificateWithUpdates, GiftCertificate giftCertificateForUpdates) {
        if (giftCertificateWithUpdates.getName() != null) {
            giftCertificateForUpdates.setName(giftCertificateWithUpdates.getName());
        }
        if (giftCertificateWithUpdates.getDescription() != null) {
            giftCertificateForUpdates.setDescription(giftCertificateWithUpdates.getDescription());
        }
        if (giftCertificateWithUpdates.getPrice() != null) {
            giftCertificateForUpdates.setPrice(giftCertificateWithUpdates.getPrice());
        }
        if (giftCertificateWithUpdates.getDuration() != null) {
            giftCertificateForUpdates.setDuration(giftCertificateWithUpdates.getDuration());
        }
        if (giftCertificateWithUpdates.getTags() != null) {
            giftCertificateForUpdates.setTags(giftCertificateWithUpdates.getTags());
            tagService.saveAllTags(giftCertificateForUpdates.getTags());
        }
        return giftCertificateForUpdates;
    }

    /**
     * A service method for updating gift certificate duration
     *
     * @param id       - GiftCertificateId (min value 1)
     * @param duration - duration value for update
     * @return updated GiftCertificate with updated Duration
     * @see GiftCertificateRepo#save(Object)
     */
    @Modifying
    public GiftCertificate updateDuration(long id, int duration) {
        GiftCertificate giftCertificate = getGiftCertificateById(id);
        giftCertificate.setDuration(duration);
        return giftCertificateRepo.save(giftCertificate);
    }

    /**
     * A service method for updating gift certificate price
     *
     * @param id    - GiftCertificateId (min value 1)
     * @param price - price value for update
     * @return updated GiftCertificate with updated Price
     * @see GiftCertificateRepo#save(Object)
     */
    @Modifying
    public GiftCertificate updatePrice(long id, int price) {
        GiftCertificate giftCertificate = getGiftCertificateById(id);
        giftCertificate.setPrice(price);
        return giftCertificateRepo.save(giftCertificate);
    }

    /**
     * A service method for getting all gift certificates by tag name
     *
     * @param tagName - name of tag
     * @param page    - number of page (min value 0)
     * @param size    - count of tags (min value 1)
     * @return Page of GiftCertificate
     * @see GiftCertificateRepo#getGiftCertificatesByTagName(String, int, int)
     */
    public Page<GiftCertificate> getGiftCertificatesByTagName(String tagName, int page, int size) {
        List<GiftCertificate> allGiftCertificateByTagName = giftCertificateRepo
                .getGiftCertificatesByTagName(tagName, size * page, (size * page) + size);
        return new PageImpl<>(allGiftCertificateByTagName);
    }

    /**
     * A service method for getting all gift certificates by tag name
     *
     * @param partOfDescription - part of GiftCertificate description
     * @param page              - number of page (min value 0)
     * @param size              - count of tags (min value 1)
     * @return Page of GiftCertificate
     * @see GiftCertificateRepo#getGiftCertificateByPartOfDescription(String, int, int)
     */
    public Page<GiftCertificate> getGiftCertificateByPartOfDescription(String partOfDescription, int page, int size) {
        List<GiftCertificate> allGiftCertificateByPartOfName = giftCertificateRepo.
                getGiftCertificateByPartOfDescription(partOfDescription + "%",
                        size * page,
                        (size * page) + size);
        return new PageImpl<>(allGiftCertificateByPartOfName);
    }

    /**
     * A service method for getting all gift certificates by tag name
     *
     * @param sortDirection - value for sorting by name (ASC or DESC)
     * @param page          - number of page (min value 0)
     * @param size          - count of tags (min value 1)
     * @return Page of GiftCertificate
     * @see GiftCertificateRepo#findAll() for getting all GiftCertificates
     */
    public Page<GiftCertificate> getGiftCertificatesSortedByName(String sortDirection, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase()), "name"));
        return giftCertificateRepo.findAll(pageRequest);
    }

    /**
     * A service method for getting all gift certificates by tag name
     *
     * @param firstSortDirection  - value for sorting by name (ASC or DESC)
     * @param secondSortDirection - value for sorting by date (ASC or DESC)
     * @param page                - number of page (min value 0)
     * @param size                - count of tags (min value 1)
     * @return Page of GiftCertificates
     * @see GiftCertificateRepo#findAll() for getting all GiftCertificates
     */
    public Page<GiftCertificate> getGiftCertificatesSortedByNameByDate(String firstSortDirection, String secondSortDirection, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(firstSortDirection.toUpperCase()), "name")
                .and(Sort.by(Sort.Direction.valueOf(secondSortDirection.toUpperCase()), "createDate")));
        return giftCertificateRepo.findAll(pageRequest);
    }

    /**
     * A service method for getting all gift certificates by tags name
     *
     * @param stringSet - tags name
     * @param page      - number of page (min value 0)
     * @param size      - count of tags (min value 1)
     * @return Page of GiftCertificate
     * @see GiftCertificateRepo#getByTags(Set, int, int, int)  for getting all GiftCertificates by Tags
     */
    public Page<GiftCertificate> getGiftCertificatesByTags(Set<String> stringSet, int page, int size) {
        List<GiftCertificate> giftCertificates =
                giftCertificateRepo.getByTags(stringSet, stringSet.size(), size * page, (size * page) + size);
        return new PageImpl<>(giftCertificates);
    }

}