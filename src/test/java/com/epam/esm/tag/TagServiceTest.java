package com.epam.esm.tag;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepo tagRepoMock;
    @InjectMocks
    private TagService tagServiceMock;

    @Test
    void createTagTest_TrueWhenTagWithSuchNameAlreadyExist() {
        Tag tag = Tag.builder().name("tag").build();
        when(tagRepoMock.tagExists(tag.getName())).thenReturn(false);
        when(tagRepoMock.save(tag)).thenReturn(tag);
        assertEquals(tag,tagServiceMock.createTag(tag));
    }

    @Test
    void createTagTest_FalseWhenTagWithSuchNameNotExist() {
        Tag tag = Tag.builder().name("tag").build();
        when(tagRepoMock.tagExists(tag.getName())).thenReturn(true);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> tagServiceMock.createTag(tag));

        assertEquals("Tag already exist: " + tag.getName(), exception.getMessage());
    }

    @Test
    void deleteTagTest_TrueWhenSuchTagExist() {
        long tagId = 1L;
        when(tagRepoMock.existsById(tagId)).thenReturn(true);
        assertTrue(tagServiceMock.deleteTag(tagId));
        verify(tagRepoMock).deleteById(any());
    }

    @Test
    void deleteTagTest_TrueWhenSuchTagNotExist() {
        long tagId = 1L;
        when(tagRepoMock.existsById(tagId)).thenReturn(false);
        ItemNotFoundException message = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.deleteTag(tagId));
        assertEquals("No tag with id = " + tagId, message.getMessage());
    }

    @Test
    void getTagByIdTest_TrueWhenTagExists() {
        Tag tag = Tag.builder().id(1L).build();
        when(tagRepoMock.findById(tag.getId())).thenReturn(Optional.of(tag));
        assertEquals(tag,tagServiceMock.getTagById(1L));
    }

    @Test
    void getTagByIdTest_FalseWhenTagNotExists() {
        long tagId = 1L;
        when(tagRepoMock.findById(tagId)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.getTagById(tagId));
        assertEquals("No tag with id = " + tagId, exception.getMessage());
    }

    @Test
    void getAllTagsTest() {
        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        Page<Tag> tags = new PageImpl<>(List.of(tag1,tag2));

        when(tagRepoMock.findAll(PageRequest.of(0, 3))).thenReturn(tags);

        assertEquals(tags,tagServiceMock.getAllTags(0, 3));
    }

    @Test
    void tagExistsTest_TrueWhenTagWithSuchNameAlreadyExist() {
        Tag tag = Tag.builder().name("tag").build();
        when(tagRepoMock.tagExists(tag.getName())).thenReturn(true);
        assertTrue(tagServiceMock.tagExists(tag.getName()));
    }

    @Test
    void tagExists_FalseWhenTagWithSuchNameNotExist() {
        Tag tag = Tag.builder().name("tag").build();
        when(tagRepoMock.tagExists(tag.getName())).thenReturn(false);
        assertFalse(tagServiceMock.tagExists(tag.getName()));
    }

    @Test
    void getTagIdByTagNameTest() {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        when(tagRepoMock.getTagIdByTagName(tag.getName())).thenReturn(tag.getId());
        assertEquals(tag.getId(), tagServiceMock.getTagIdByTagName(tag.getName()));
    }

    @Test
    void saveAllTagsTest() {
        Set<Tag> tags = Set.of(Tag.builder().name("tag").build());
        when(tagRepoMock.tagExists("tag")).thenReturn(true);
        when(tagRepoMock.saveAllAndFlush(tags)).thenReturn(tags.stream().toList());
        when(tagRepoMock.getTagIdByTagName("tag")).thenReturn(1L);

        assertTrue(tagServiceMock.saveAllTags(tags));
    }

    @Test
    void getTheMostlyUsedTagInUserOrdersTest() {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        when(tagRepoMock.getTheMostlyUsedTag()).thenReturn(tag);
        assertEquals(tag,tagServiceMock.getTheMostlyUsedTagInUserOrders());
    }
}