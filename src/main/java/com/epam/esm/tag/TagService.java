package com.epam.esm.tag;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Vlad Storoshchuk
 */
@Service
public class TagService {
    private final TagRepo tagRepo;

    public TagService(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    /**
     * A service method for creating Tag
     *
     * @param tag Tag object that will be created in database
     * @return saved Tag
     * @see TagRepo#save(Object) for saving Tag
     */
    public Tag createTag(Tag tag) {
        if (!tagExists(tag.getName())) {
            return tagRepo.save(tag);
        }
        throw new InvalidDataException("Tag already exist: " + tag.getName());
    }

    /**
     * A service method for checking if Tag already exists
     *
     * @param name Tag name
     * @return boolean
     * @see TagRepo#existsByName(String) (Object) for checking if Tag already exists
     */
    public boolean tagExists(String name) {
        return tagRepo.existsByName(name);
    }

    /**
     * A service method for getting all Tags
     *
     * @param page number of page (min value 0)
     * @param size count of Tags (min value 1)
     * @return Page of Tags
     * @see TagRepo#findAll() for getting all Tags
     */
    public Page<Tag> getAllTags(int page, int size) {
        return tagRepo.findAll(PageRequest.of(page, size));
    }

    /**
     * A service method for getting Tag id
     *
     * @param tagName tag name
     * @return Tag id
     * @see TagRepo#getTagIdByTagName(String) for getting Tag id
     */
    public long getTagIdByTagName(String tagName) {
        return tagRepo.getTagIdByTagName(tagName);
    }

    /**
     * A service method for getting Tag by id
     *
     * @param id Tag id
     * @return Tag
     * @see TagRepo#getTagIdByTagName(String) for getting Tag id
     */
    public Tag getTagById(long id) {
        return tagRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("No tag with id = " + id));
    }

    /**
     * A service method for deleting Tag
     *
     * @param id Tag id
     * @return boolean
     * @see TagRepo#existsByName(String) (Object) for checking if Tag already exists
     * @see TagRepo#deleteById(Object) (String) for getting Tag id
     */
    public boolean deleteTag(long id) {
        if (tagRepo.existsById(id)) {
            tagRepo.deleteById(id);
            return true;
        }
        throw new ItemNotFoundException("No tag with id = " + id);
    }

    /**
     * A service method for saving all Tags
     *
     * @param tags set of Tags name
     * @return boolean
     * @see TagRepo#saveAllAndFlush(Iterable) for saving all Tags
     */
    public boolean saveAllTags(Set<Tag> tags) {
        Set<Tag> newTags = new HashSet<>();
        for (Tag tag : tags) {
            if (tagExists(tag.getName())) {
                tag.setId(getTagIdByTagName(tag.getName()));
            }
            newTags.add(tag);
        }
        tagRepo.saveAllAndFlush(newTags);
        return true;
    }

    /**
     * A service method for getting the mostly used Tag of user with the highest price of orders
     *
     * @return Tag
     * @see TagRepo#getTheMostlyUsedTag() for getting mostly used Tag
     */
    public Tag getTheMostlyUsedTagInUserOrders() {
        return tagRepo.getTheMostlyUsedTag();
    }
}