package com.epam.esm.tag;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.utils.VerificationOfRequestsData;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Class TagController which contain method related with Tag
 *
 * @author Vlad Storoshchuk
 */
@Validated
@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;
    private final TagHateoasMapper tagHateoasMapper;

    public TagController(TagService tagService, TagHateoasMapper tagHateoasMapper) {
        this.tagService = tagService;
        this.tagHateoasMapper = tagHateoasMapper;
    }

    /**
     * A controller post method for creating a new tag
     *
     * @param tag the Tag object that will be created in database
     * @see TagService#createTag(Tag)
     */
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (VerificationOfRequestsData.isTagCorrect(tag)) {
            Tag createdTag = tagService.createTag(tag);
            return new ResponseEntity<>(Map.of("createdTag",
                    tagHateoasMapper.createTagHateoas(createdTag)), HttpStatus.CREATED);
        }
        throw new InvalidDataException("Invalid data. Check your fields.");
    }


    /**
     * A controller get method for getting all tags
     *
     * @param page - number of page (min value 0)
     * @param size - count of tags (min value 1)
     * @see TagService#getAllTags(int, int)
     */
    @GetMapping
    public ResponseEntity<?> getAllTags(@RequestParam(value = "page", defaultValue = "0")
                                        @Min(value = 0, message = "Page index should be >= 0") int page,
                                        @RequestParam(value = "size", defaultValue = "10")
                                        @Min(value = 1, message = "Size should be should be >= 1") int size) {
        Page<Tag> tags = tagService.getAllTags(page, size);
        return ResponseEntity.ok(Map.of("tags", tagHateoasMapper.getAllTagHateoas(tags)));
    }

    /**
     * A controller get method for getting tag by id
     *
     * @param id - tag id (min value 0)
     * @see TagService#getTagById(long)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable("id") @Min(value = 1, message = "Id should be >= 1") long id) {
        Tag currentTag = tagService.getTagById(id);
        return ResponseEntity.ok(Map.of("tag", tagHateoasMapper.getTagByIdHateoas(currentTag)));
    }

    /**
     * A controller delete method for deleting tag by id
     *
     * @param id - tag id (min value 0)
     * @see TagService#deleteTag(long)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") @Min(value = 1, message = "Id should be >= 1") long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
    }

    /**
     * A controller get method for getting the mostly used tag of user with the highest price of orders
     *
     * @see TagService#getTheMostlyUsedTagInUserOrders()
     */
    @GetMapping("/mostly-used-tag")
    public ResponseEntity<?> getTheMostlyUsedTagInUserOrders() {
        Tag getTheMostFrequentlyTag = tagService.getTheMostlyUsedTagInUserOrders();
        return ResponseEntity.ok(Map.of("mostlyUsedTag",
                tagHateoasMapper.getMostlyUsedTagHateoas(getTheMostFrequentlyTag)));
    }
}