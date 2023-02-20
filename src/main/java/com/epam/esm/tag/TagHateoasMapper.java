package com.epam.esm.tag;

import com.epam.esm.giftcertificate.GiftCertificateController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Vlad Storoshchuk
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TagHateoasMapper {
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;

    /**
     * A component method for adding links to created Tag
     *
     * @param tag created Tag
     * @return CollectionModel of Tag with links
     */
    public CollectionModel<Tag> createTagHateoas(Tag tag) {
        CollectionModel<Tag> tagCollectionModel = CollectionModel.of(List.of(tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"))
                .add(linkTo(methodOn(TagController.class)
                        .getTagById(tag.getId()))
                        .withRel(() -> "get tag by id"))));
        defaultLinksForCollectionModel(tagCollectionModel);
        tagCollectionModel
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"));
        return tagCollectionModel;
    }

    /**
     * A component method for adding links to the mostly used Tag
     *
     * @param tag created Tag
     * @return CollectionModel of Tag with links
     */
    public CollectionModel<Tag> getMostlyUsedTagHateoas(Tag tag) {
        CollectionModel<Tag> tagCollectionModel = CollectionModel.of(List.of(tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"))
                .add(linkTo(methodOn(TagController.class)
                        .getTagById(tag.getId()))
                        .withRel(() -> "get tag by id"))));
        defaultLinksForCollectionModel(tagCollectionModel);
        tagCollectionModel
                .add(linkTo(methodOn(TagController.class)
                        .createTag(new Tag()))
                        .withRel(() -> "create tag"));
        return tagCollectionModel;
    }

    /**
     * A component method for adding links to Tag
     *
     * @param tag created Tag
     * @return CollectionModel of Tag with links
     */
    public CollectionModel<Tag> getTagByIdHateoas(Tag tag) {
        CollectionModel<Tag> tagCollectionModel = CollectionModel.of(List.of(tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"))
                .add(linkTo(methodOn(TagController.class)
                        .getTagById(tag.getId()))
                        .withRel(() -> "get tag by id"))));
        defaultLinksForCollectionModel(tagCollectionModel);
        tagCollectionModel
                .add(linkTo(methodOn(TagController.class)
                        .createTag(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"));
        return tagCollectionModel;
    }

    /**
     * A component method for adding links to all Tags
     *
     * @param tags Page of Tags
     * @return PagedModel of Tag with links
     */
    public PagedModel<Tag> getAllTagHateoas(Page<Tag> tags) {
        PagedModel<Tag> tagsPagedModel = pagedResourcesAssembler
                .toModel(tags, tag -> tag
                        .add(linkTo(methodOn(TagController.class)
                                .deleteTag(tag.getId()))
                                .withRel(() -> "delete tag by id"))
                        .add(linkTo(methodOn(TagController.class)
                                .getTagById(tag.getId()))
                                .withRel(() -> "get tag by id")));

        tagsPagedModel
                .add(linkTo(methodOn(TagController.class)
                        .createTag(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"));
        return tagsPagedModel;
    }

    private void defaultLinksForCollectionModel(CollectionModel<Tag> tagCollectionModel) {
        tagCollectionModel
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift-certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift-certificates by tags names"));
    }
}
