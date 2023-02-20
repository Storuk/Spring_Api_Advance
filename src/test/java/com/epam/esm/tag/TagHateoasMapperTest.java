package com.epam.esm.tag;

import com.epam.esm.giftcertificate.GiftCertificateController;
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
class TagHateoasMapperTest {
    @InjectMocks
    private TagHateoasMapper tagHateoasMapper;
    @Mock
    private PagedResourcesAssembler<Tag> tagPagedResourcesAssembler;
    @Captor
    ArgumentCaptor<Link> argumentCaptor;

    @Test
    void createTagHateoas() {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        Tag tagForTest = Tag.builder().id(1L).name("tag").build();

        CollectionModel<Tag> createdTag = CollectionModel.of(List.of(tag));
        tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"))
                .add(linkTo(methodOn(TagController.class)
                        .getTagById(tag.getId()))
                        .withRel(() -> "get tag by id"));
        createdTag
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift-certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift-certificates by tags names"));

        assertEquals(createdTag.getContent().stream().toList(), tagHateoasMapper.createTagHateoas(tagForTest).getContent().stream().toList());
    }

    @Test
    void getMostlyUsedTagHateoas() {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        Tag tagForTest = Tag.builder().id(1L).name("tag").build();

        CollectionModel<Tag> createdTag = CollectionModel.of(List.of(tag));
        tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"))
                .add(linkTo(methodOn(TagController.class)
                        .getTagById(tag.getId()))
                        .withRel(() -> "get tag by id"));
        createdTag
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift-certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift-certificates by tags names"));

        assertEquals(createdTag.getContent().stream().toList(),
                tagHateoasMapper.getMostlyUsedTagHateoas(tagForTest).getContent().stream().toList());
    }

    @Test
    void getTagByIdHateoas() {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        Tag tagForTest = Tag.builder().id(1L).name("tag").build();

        CollectionModel<Tag> createdTag = CollectionModel.of(List.of(tag));
        tag
                .add(linkTo(methodOn(TagController.class)
                        .deleteTag(tag.getId()))
                        .withRel(() -> "delete tag by id"));
        createdTag
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift-certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift-certificates by tags names"));

        assertEquals(createdTag.getContent().stream().toList(),
                tagHateoasMapper.getTagByIdHateoas(tagForTest).getContent().stream().toList());
    }

    @Test
    void getAllTagHateoas() {
        Page<Tag> tags = new PageImpl<>(List.of(new Tag()));
        PagedModel<Tag> tagsPagedModel = mock(PagedModel.class);
        when(tagsPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(tagsPagedModel);
        when(tagPagedResourcesAssembler.toModel(eq(tags), ArgumentMatchers.<RepresentationModelAssembler<Tag, Tag>>any())).thenReturn(tagsPagedModel);
        PagedModel<Tag> result = tagHateoasMapper.getAllTagHateoas(tags);

        assertEquals(tagsPagedModel, result);
        verify(result, times(5)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "create tag");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get gift certificates by tags names");
    }
}