package com.epam.esm.giftcertificate;

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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {
    private MockMvc mvc;
    @Mock
    private GiftCertificateService giftCertificateService;
    @InjectMocks
    private GiftCertificateController giftCertificateController;
    @Mock
    private GiftCertificateHateoasMapper giftCertificateHateoasMapper;
    private JacksonTester<Map<String, CollectionModel<?>>> jsonGiftCertificateCollectionModel;
    private JacksonTester<Map<String, PagedModel<?>>> jsonGiftCertificatePagedModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private GiftCertificateDtoToEntityMapper giftCertificateDtoToEntityMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(giftCertificateController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    void getGiftCertificateByIdTest() throws Exception {
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L).name("giftCertificate").build();
        when(giftCertificateService.getGiftCertificateById(1L)).thenReturn(giftCertificate);
        when(giftCertificateHateoasMapper.getGiftCertificateByIdHateoasMapper(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateCollectionModel.write(Map.of("giftCertificate", CollectionModel.of(List.of(giftCertificate)))).getJson(), response.getContentAsString());
    }

    @Test
    void deleteGiftCertificateTest() throws Exception {
        when(giftCertificateService.deleteGiftCertificate(1L)).thenReturn(true);
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete("/gift-certificates/1")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void createCertificateTest() throws Exception {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        when(giftCertificateDtoToEntityMapper.convertGiftCertificateDtoToGiftCertificate(giftCertificateDTO)).thenReturn(giftCertificate);
        when(giftCertificateService.createGiftCertificate(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateHateoasMapper.getGiftCertificateForCreateHateoasMapper(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        MockHttpServletResponse response = mvc.perform(post("/gift-certificates").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftCertificateDTO)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonGiftCertificateCollectionModel.write(Map.of("certificate", CollectionModel.of(List.of(giftCertificate)))).getJson(), response.getContentAsString());
    }

    @Test
    void createCertificateTest_ThrowsBadRequest() throws Exception {
        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder().
                name("123").description("certificate").price(1).duration(1).build();
        MockHttpServletResponse response = mvc.perform(post("/gift-certificates").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftCertificateDTO)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void updateGiftCertificateTest() throws Exception {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder().
                name("certificate").description("certificate").price(1).duration(1).build();
        when(giftCertificateDtoToEntityMapper.convertGiftCertificateDtoToGiftCertificate(giftCertificateDTO)).thenReturn(giftCertificate);
        when(giftCertificateService.updateGiftCertificate(1L, giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateHateoasMapper.getGiftCertificateForUpdateHateoasMapper(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        MockHttpServletResponse response = mvc.perform(patch("/gift-certificates/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftCertificateDTO)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateCollectionModel.write(Map.of("giftCertificate", CollectionModel.of(List.of(giftCertificate)))).getJson(), response.getContentAsString());
    }

    @Test
    void updateDurationTest() throws Exception {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(100).build();
        when(giftCertificateService.updateDuration(1L, 100)).thenReturn(giftCertificate);
        when(giftCertificateHateoasMapper.getGiftCertificateForUpdateDurationHateoasMapper(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        MockHttpServletResponse response = mvc.perform(patch("/gift-certificates/update-duration/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(100)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateCollectionModel.write(Map.of("giftCertificate", CollectionModel.of(List.of(giftCertificate)))).getJson(), response.getContentAsString());
    }

    @Test
    void updatePriceTest() throws Exception {
        GiftCertificate giftCertificate = GiftCertificate.builder().
                name("certificate").description("certificate").price(1).duration(100).build();
        when(giftCertificateService.updatePrice(1L, 100)).thenReturn(giftCertificate);
        when(giftCertificateHateoasMapper.getGiftCertificateForUpdatePriceHateoasMapper(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        MockHttpServletResponse response = mvc.perform(patch("/gift-certificates/update-price/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(100)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateCollectionModel.write(Map.of("giftCertificate", CollectionModel.of(List.of(giftCertificate)))).getJson(), response.getContentAsString());
    }

    @Test
    void getAllCertificatesTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getAllGiftCertificates(0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesByTagNameTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getGiftCertificatesByTagName("tag", 0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesByTagNameHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-tag-name?name=tag&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesByTagNameTest_ThrowsBadRequest() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-tag-name?name=123&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesByTagsTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getGiftCertificatesByTags(Set.of("tag"), 0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesByTagsHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-by-tag-names?tagNamesSet=tag&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesByTagsTest_ThrowsBadRequest() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-by-tag-names?tagNamesSet=123&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesByPartOfDescriptionTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getGiftCertificateByPartOfDescription("description", 0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesByPartOfDescriptionHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-gift-certificate-description?description=description&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesByPartOfDescriptionTest_ThrowsBadRequest() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-gift-certificate-description?description=123&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesSortedByNameTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getGiftCertificatesSortedByName("desc", 0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesSortedByNameHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name?sortingType=desc&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesSortedByNameTest_ThrowsBadRequest_WhenStringIncorrect() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name?sortingType=123&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesSortedByNameTest_ThrowsBadRequest_WhenSortingTypeIncorrect() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name?sortingType=new&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesSortedByNameByDateTest() throws Exception {
        Page<GiftCertificate> giftCertificatePage = Page.empty();
        when(giftCertificateService.getGiftCertificatesSortedByNameByDate("desc", "desc", 0, 10)).thenReturn(giftCertificatePage);
        when(giftCertificateHateoasMapper.getAllCertificatesSortedByNameAndByDateHateoasMapper(giftCertificatePage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name-date?sortingTypeName=desc&sortingTypeDate=desc&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonGiftCertificatePagedModel.write(Map.of("giftCertificates", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getGiftCertificatesSortedByNameByDateTest_ThrowsBadRequest_WhenStringsIncorrect() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name-date?sortingTypeName=123&sortingTypeDate=desc&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesSortedByNameByDateTest_ThrowsBadRequest_WhenSortingTypesIncorrect() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/gift-certificates/get-sort-name-date?sortingTypeName=new&sortingTypeDate=desc&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}