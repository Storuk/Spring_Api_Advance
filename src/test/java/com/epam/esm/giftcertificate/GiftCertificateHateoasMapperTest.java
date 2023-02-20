package com.epam.esm.giftcertificate;

import com.epam.esm.orders.OrderController;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GiftCertificateHateoasMapperTest {
    @Mock
    private PagedResourcesAssembler<GiftCertificate> orderPagedResourcesAssembler;
    @Captor
    ArgumentCaptor<Link> argumentCaptor;
    @InjectMocks
    private GiftCertificateHateoasMapper giftCertificateHateoasMapper;

    @Test
    void getGiftCertificateByIdHateoasMapperTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        CollectionModel<GiftCertificate> giftCertificateByIdModel = CollectionModel.of(List.of(giftCertificate));

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
                        .withRel(() -> "update duration in gift certificate"));

        for (Tag tag : giftCertificate.getTags()) {
            tag
                    .add(linkTo(methodOn(TagController.class)
                            .deleteTag(tag.getId()))
                            .withRel(() -> "delete tag by id"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTagById(tag.getId()))
                            .withRel(() -> "get tag by id"));
        }

        giftCertificateByIdModel
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
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"));

        CollectionModel<GiftCertificate> result = giftCertificateHateoasMapper.getGiftCertificateByIdHateoasMapper(giftCertificateForTest);

        assertEquals(giftCertificateByIdModel.getContent().stream().toList(), result.getContent().stream().toList());
    }

    @Test
    void getGiftCertificateForUpdateHateoasMapperTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        CollectionModel<GiftCertificate> giftCertificateByIdModel = CollectionModel.of(List.of(giftCertificate));

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
                        .withRel(() -> "update duration in gift certificate"));

        for (Tag tag : giftCertificate.getTags()) {
            tag
                    .add(linkTo(methodOn(TagController.class)
                            .deleteTag(tag.getId()))
                            .withRel(() -> "delete tag by id"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTagById(tag.getId()))
                            .withRel(() -> "get tag by id"));
        }

        giftCertificateByIdModel
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
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"));

        CollectionModel<GiftCertificate> result = giftCertificateHateoasMapper.getGiftCertificateForUpdateHateoasMapper(giftCertificateForTest);

        assertEquals(giftCertificateByIdModel.getContent().stream().toList(), result.getContent().stream().toList());
    }

    @Test
    void getGiftCertificateForUpdatePriceHateoasMapperTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        CollectionModel<GiftCertificate> giftCertificateByIdModel = CollectionModel.of(List.of(giftCertificate));

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
                        .withRel(() -> "update duration in gift certificate"));

        for (Tag tag : giftCertificate.getTags()) {
            tag
                    .add(linkTo(methodOn(TagController.class)
                            .deleteTag(tag.getId()))
                            .withRel(() -> "delete tag by id"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTagById(tag.getId()))
                            .withRel(() -> "get tag by id"));
        }

        giftCertificateByIdModel
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
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"));

        CollectionModel<GiftCertificate> result = giftCertificateHateoasMapper.getGiftCertificateForUpdatePriceHateoasMapper(giftCertificateForTest);

        assertEquals(giftCertificateByIdModel.getContent().stream().toList(), result.getContent().stream().toList());
    }

    @Test
    void getGiftCertificateForUpdateDurationHateoasMapperTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        CollectionModel<GiftCertificate> giftCertificateByIdModel = CollectionModel.of(List.of(giftCertificate));

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
                        .withRel(() -> "delete gift certificate"));

        for (Tag tag : giftCertificate.getTags()) {
            tag
                    .add(linkTo(methodOn(TagController.class)
                            .deleteTag(tag.getId()))
                            .withRel(() -> "delete tag by id"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTagById(tag.getId()))
                            .withRel(() -> "get tag by id"));
        }

        giftCertificateByIdModel
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
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"));

        CollectionModel<GiftCertificate> result = giftCertificateHateoasMapper.getGiftCertificateForUpdateDurationHateoasMapper(giftCertificateForTest);

        assertEquals(giftCertificateByIdModel.getContent().stream().toList(), result.getContent().stream().toList());
    }

    @Test
    void getGiftCertificateForCreateHateoasMapperTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        CollectionModel<GiftCertificate> giftCertificateByIdModel = CollectionModel.of(List.of(giftCertificate));

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
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update price in gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateDuration(giftCertificate.getId(), 0))
                        .withRel(() -> "update duration in gift certificate"));

        for (Tag tag : giftCertificate.getTags()) {
            tag
                    .add(linkTo(methodOn(TagController.class)
                            .deleteTag(tag.getId()))
                            .withRel(() -> "delete tag by id"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTagById(tag.getId()))
                            .withRel(() -> "get tag by id"));
        }

        giftCertificateByIdModel
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
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(OrderController.class)
                        .createOrder(0, 0))
                        .withRel(() -> "create order"));

        CollectionModel<GiftCertificate> result = giftCertificateHateoasMapper.getGiftCertificateForCreateHateoasMapper(giftCertificateForTest);

        assertEquals(giftCertificateByIdModel.getContent().stream().toList(), result.getContent().stream().toList());
    }

    @Test
    void giftCertificateDefaultLinksTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

        GiftCertificate giftCertificateForTest = GiftCertificate.builder()
                .id(1L).name("giftCertificate").description(null).price(1)
                .duration(1).tags(Set.of(Tag.builder().id(1L).name("tag").build()))
                .createDate(null).lastUpdateDate(null).build();

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
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update price in gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateDuration(giftCertificate.getId(), 0))
                        .withRel(() -> "update duration in gift certificate"));

        assertEquals(giftCertificate.getLinks().toString(),
                giftCertificateHateoasMapper.giftCertificateDefaultLinks(giftCertificateForTest).getLinks().toString());
    }

    @Test
    void getAllCertificatesHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get gift certificates by tags names");
    }

    @Test
    void getAllCertificatesByTagNameHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesByTagNameHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get gift certificates by tags names");
    }

    @Test
    void getAllCertificatesByTagsHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesByTagsHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get gift certificates by tag name");
    }

    @Test
    void getAllCertificatesByPartOfDescriptionHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesByPartOfDescriptionHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get all gift certificates sorted by name and by date");
    }

    @Test
    void getAllCertificatesSortedByNameHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesSortedByNameHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get all gift certificates by part of description");
    }

    @Test
    void getAllCertificatesSortedByNameAndByDateHateoasMapperTest() {
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PagedModel<GiftCertificate> giftCertificatesPagedModel = mock(PagedModel.class);
        when(giftCertificatesPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(giftCertificatesPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(giftCertificatesPagedModel);
        PagedModel<GiftCertificate> result = giftCertificateHateoasMapper.getAllCertificatesSortedByNameAndByDateHateoasMapper(giftCertificates);

        assertEquals(result, giftCertificatesPagedModel);
        verify(result, times(9)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create order");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get all gift certificates by part of description");
    }

}