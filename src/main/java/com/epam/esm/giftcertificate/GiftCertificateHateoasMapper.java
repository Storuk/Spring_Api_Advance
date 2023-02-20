package com.epam.esm.giftcertificate;

import com.epam.esm.orders.OrderController;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagController;
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
public class GiftCertificateHateoasMapper {
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler;

    /**
     * A component method for adding links to all GiftCertificates
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetAllAndGetAllByTagNameAndGetByTagsName(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"));
        return pagedModel;
    }

    /**
     * A component method for adding links to all GiftCertificates by Tag Name
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesByTagNameHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetAllAndGetAllByTagNameAndGetByTagsName(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"));
        return pagedModel;
    }

    /**
     * A component method for adding links to all GiftCertificates by tags name
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesByTagsHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetAllAndGetAllByTagNameAndGetByTagsName(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"));
        return pagedModel;
    }

    /**
     * A component method for adding links to all GiftCertificates by Part of description
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesByPartOfDescriptionHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetByPartOfDescriptionAndSortByNameAndSortByNameByDate(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"));
        return pagedModel;
    }

    /**
     * A component method for adding links to all GiftCertificates sorted by name
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesSortedByNameHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetByPartOfDescriptionAndSortByNameAndSortByNameByDate(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"));
        return pagedModel;
    }

    /**
     * A component method for adding links to all GiftCertificates sorted by name and by date
     *
     * @param pagedGiftCertificate - Page of GiftCertificate
     * @return PagedModel of GiftCertificates with links
     */
    public PagedModel<GiftCertificate> getAllCertificatesSortedByNameAndByDateHateoasMapper(Page<GiftCertificate> pagedGiftCertificate) {
        PagedModel<GiftCertificate> pagedModel = defaultForPagedModelToModel(pagedGiftCertificate);
        defaultForPagedModelWithoutGetByPartOfDescriptionAndSortByNameAndSortByNameByDate(pagedModel);
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"));
        return pagedModel;
    }

    /**
     * A component method for adding links to GiftCertificate by id
     *
     * @param giftCertificate GiftCertificate
     * @return CollectionModel of GiftCertificates with links
     */
    public CollectionModel<GiftCertificate> getGiftCertificateByIdHateoasMapper(GiftCertificate giftCertificate) {
        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                giftCertificate
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(giftCertificate.getId()))
                                .withRel(() -> "delete gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(giftCertificate.getId(), new GiftCertificate()))
                                .withRel(() -> "update gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updatePrice(giftCertificate.getId(), 0))
                                .withRel(() -> "update price in gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateDuration(giftCertificate.getId(), 0))
                                .withRel(() -> "update duration in gift certificate"))));
        defaultLinksForCollectionModel(giftCertificateCollectionModel);
        tagsDefaultLinks(giftCertificate);
        giftCertificateCollectionModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
        return giftCertificateCollectionModel;
    }

    /**
     * A component method for adding links to updated GiftCertificate
     *
     * @param giftCertificate GiftCertificate
     * @return CollectionModel of GiftCertificates with links
     */
    public CollectionModel<GiftCertificate> getGiftCertificateForUpdateHateoasMapper(GiftCertificate giftCertificate) {
        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                giftCertificate
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .getGiftCertificateById(giftCertificate.getId()))
                                .withRel(() -> "get gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(giftCertificate.getId()))
                                .withRel(() -> "delete gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updatePrice(giftCertificate.getId(), 0))
                                .withRel(() -> "update price in gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateDuration(giftCertificate.getId(), 0))
                                .withRel(() -> "update duration in gift certificate"))));
        defaultLinksForCollectionModel(giftCertificateCollectionModel);
        tagsDefaultLinks(giftCertificate);
        giftCertificateCollectionModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
        return giftCertificateCollectionModel;
    }

    /**
     * A component method for adding links to GiftCertificate with updated price
     *
     * @param giftCertificate GiftCertificate
     * @return CollectionModel of GiftCertificates with links
     */
    public CollectionModel<GiftCertificate> getGiftCertificateForUpdatePriceHateoasMapper(GiftCertificate giftCertificate) {
        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                giftCertificate
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .getGiftCertificateById(giftCertificate.getId()))
                                .withRel(() -> "get gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(giftCertificate.getId()))
                                .withRel(() -> "delete gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(giftCertificate.getId(), new GiftCertificate()))
                                .withRel(() -> "update gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateDuration(giftCertificate.getId(), 0))
                                .withRel(() -> "update duration in gift certificate"))));
        defaultLinksForCollectionModel(giftCertificateCollectionModel);
        tagsDefaultLinks(giftCertificate);
        giftCertificate
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
        return giftCertificateCollectionModel;
    }

    /**
     * A component method for adding links to GiftCertificate with updated duration
     *
     * @param giftCertificate GiftCertificate
     * @return CollectionModel of GiftCertificates with links
     */
    public CollectionModel<GiftCertificate> getGiftCertificateForUpdateDurationHateoasMapper(GiftCertificate giftCertificate) {
        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                giftCertificate
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .getGiftCertificateById(giftCertificate.getId()))
                                .withRel(() -> "get gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(giftCertificate.getId(), new GiftCertificate()))
                                .withRel(() -> "update gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .updatePrice(giftCertificate.getId(), 0))
                                .withRel(() -> "update price in gift certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(giftCertificate.getId()))
                                .withRel(() -> "delete gift certificate"))));
        defaultLinksForCollectionModel(giftCertificateCollectionModel);
        tagsDefaultLinks(giftCertificate);
        giftCertificateCollectionModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
        return giftCertificateCollectionModel;
    }

    /**
     * A component method for adding links to created GiftCertificate
     *
     * @param giftCertificate GiftCertificate
     * @return CollectionModel of GiftCertificates with links
     */
    public CollectionModel<GiftCertificate> getGiftCertificateForCreateHateoasMapper(GiftCertificate giftCertificate) {
        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                giftCertificateDefaultLinks(giftCertificate)));
        defaultLinksForCollectionModel(giftCertificateCollectionModel);
        tagsDefaultLinks(giftCertificate);
        giftCertificateCollectionModel
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
        return giftCertificateCollectionModel;
    }

    /* Utils Methods For Adding Links */

    /**
     * A component method for adding default links to GiftCertificate Tags
     *
     * @param giftCertificate GiftCertificate
     */
    public void tagsDefaultLinks(GiftCertificate giftCertificate) {
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                if (!tag.hasLinks()) {
                    tag
                            .add(linkTo(methodOn(TagController.class)
                                    .deleteTag(tag.getId()))
                                    .withRel(() -> "delete tag by id"))
                            .add(linkTo(methodOn(TagController.class)
                                    .getTagById(tag.getId()))
                                    .withRel(() -> "get tag by id"));
                }
            }
        }
    }

    /**
     * A component method for adding default links to GiftCertificate
     *
     * @param giftCertificate GiftCertificate
     */
    public GiftCertificate giftCertificateDefaultLinks(GiftCertificate giftCertificate) {
        return giftCertificate
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificateById(giftCertificate.getId()))
                        .withRel(() -> "get gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(giftCertificate.getId()))
                        .withRel(() -> "delete gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(giftCertificate.getId(), new GiftCertificate()))
                        .withRel(() -> "update gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update price in gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateDuration(giftCertificate.getId(), 0))
                        .withRel(() -> "update duration in gift certificate"));
    }

    /**
     * A component method for adding default links to Page of GiftCertificates
     *
     * @param pagedGiftCertificate Page of GiftCertificates
     */
    private PagedModel<GiftCertificate> defaultForPagedModelToModel(Page<GiftCertificate> pagedGiftCertificate) {
        return pagedResourcesAssembler
                .toModel(pagedGiftCertificate, giftCertificate -> {
                    giftCertificateDefaultLinks(giftCertificate);
                    tagsDefaultLinks(giftCertificate);
                    return giftCertificate;
                });
    }

    /**
     * A component method for adding default links to Collection of Object
     *
     * @param collectionModel CollectionModel of GiftCertificate or Order
     */
    public void defaultLinksForCollectionModel(CollectionModel<?> collectionModel) {
        collectionModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"))
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"));
    }

    /**
     * A component method for adding default links
     * Without GetAll GetAllByTagName and GetByTagsName to PagedModel of GiftCertificates
     *
     * @param pagedModel Page of GiftCertificates
     */
    private void defaultForPagedModelWithoutGetAllAndGetAllByTagNameAndGetByTagsName(PagedModel<GiftCertificate> pagedModel) {
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"))
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
    }

    /**
     * A component method for adding default links
     * Without GetByPartOfDescription SortByName and SortByNameByDate to PagedModel of GiftCertificates
     *
     * @param pagedModel Page of GiftCertificates
     */
    private void defaultForPagedModelWithoutGetByPartOfDescriptionAndSortByNameAndSortByNameByDate(PagedModel<GiftCertificate> pagedModel) {
        pagedModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift certificates"))
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));
    }
}