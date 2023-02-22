package com.epam.esm.tag;

import com.epam.esm.exceptions.controlleradvice.ControllerAdvisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private MockMvc mvc;
    @Mock
    private TagService tagService;
    @InjectMocks
    private TagController tagController;
    @Mock
    private TagHateoasMapper tagHateoasMapper;
    private JacksonTester<Map<String, CollectionModel<?>>> jsonTagCollectionModel;
    private JacksonTester<Map<String, PagedModel<?>>> jsonTagPagedModel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(tagController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    void createTagTest() throws Exception {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        TagDTO tagDTO = TagDTO.builder().name("tag").build();
        when(tagService.createTag(tagDTO)).thenReturn(tag);
        when(tagHateoasMapper.createTagHateoas(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        MockHttpServletResponse response = mvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonTagCollectionModel.write(Map.of("createdTag", CollectionModel.of(List.of(tag)))).getJson(), response.getContentAsString());
    }

    @Test
    void createTagTest_ThrowsBadRequest() throws Exception {
        Tag tag = Tag.builder().id(1L).name("").build();
        MockHttpServletResponse response = mvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());
    }

    @Test
    void getAllTagsTest() throws Exception {
        Page<Tag> tagList = Page.empty();
        when(tagService.getAllTags(0, 10)).thenReturn(tagList);
        when(tagHateoasMapper.getAllTagHateoas(tagList)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/tags?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonTagPagedModel.write(Map.of("tags", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getTagByIdTest() throws Exception {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        when(tagService.getTagById(1L)).thenReturn(tag);
        when(tagHateoasMapper.getTagByIdHateoas(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        MockHttpServletResponse response = mvc.perform(get("/tags/1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonTagCollectionModel.write(Map.of("tag", CollectionModel.of(List.of(tag)))).getJson(), response.getContentAsString());
    }

    @Test
    void deleteTagTest() throws Exception {
        when(tagService.deleteTag(1L)).thenReturn(true);
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete("/tags/1")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getTheMostlyUsedTagInUserOrdersTest() throws Exception {
        Tag tag = Tag.builder().id(1L).name("tag").build();
        when(tagService.getTheMostlyUsedTagInUserOrders()).thenReturn(tag);
        when(tagHateoasMapper.getMostlyUsedTagHateoas(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        MockHttpServletResponse response = mvc.perform(get("/tags/mostly-used-tag")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonTagCollectionModel.write(Map.of("mostlyUsedTag", CollectionModel.of(List.of(tag)))).getJson(), response.getContentAsString());
    }
}