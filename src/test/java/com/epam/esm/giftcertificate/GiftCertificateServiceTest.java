package com.epam.esm.giftcertificate;

import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepo giftCertificateRepoMock;
    @Mock
    private TagService tagServiceMock;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMock;

    @Test
    void createGiftCertificateTest_ReturnCreatedGiftCertificate() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("giftCertificate").tags(Set.of(Tag.builder().name("tag").build())).build();
        when(giftCertificateRepoMock.giftCertificateExists(giftCertificate.getName())).thenReturn(false);
        when(giftCertificateRepoMock.save(giftCertificate)).thenReturn(giftCertificate);
        when(tagServiceMock.saveAllTags(giftCertificate.getTags())).thenReturn(true);
        assertEquals(giftCertificate, giftCertificateServiceMock.createGiftCertificate(giftCertificate));
    }

    @Test
    void createGiftCertificateTest_InvalidDataException_WhenNameHasAlreadyExisted() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.giftCertificateExists(giftCertificate.getName())).thenReturn(true);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> giftCertificateServiceMock.createGiftCertificate(giftCertificate));

        assertEquals("Certificate already exists: " + giftCertificate.getName(), exception.getMessage());
    }

    @Test
    void getAllGiftCertificatesTest() {
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        when(giftCertificateRepoMock.findAll(PageRequest.of(0, 5))).thenReturn(allGiftCertificates);

        assertEquals(allGiftCertificates, giftCertificateServiceMock.getAllGiftCertificates(0, 5));
    }

    @Test
    void getGiftCertificateByIdTestTrue_WhenGiftCertificateExists() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.findById(1L)).thenReturn(Optional.of(giftCertificate));

        assertEquals(giftCertificate, giftCertificateServiceMock.getGiftCertificateById(1L));
    }

    @Test
    void getGiftCertificateByIdTestItemNotFoundException_WhenGiftCertificateNotExists() {
        long giftCertificateId = 1L;
        when(giftCertificateRepoMock.findById(giftCertificateId)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.getGiftCertificateById(giftCertificateId));

        assertEquals("No gift certificate with (id = " + giftCertificateId + ")", exception.getMessage());
    }

    @Test
    void deleteGiftCertificateByIdTest_ItemNotFoundException_WhenSuchGiftCertificateNotExist() {
        long giftCertificateId = 1L;
        when(giftCertificateRepoMock.existsById(giftCertificateId)).thenReturn(false);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.deleteGiftCertificate(giftCertificateId));

        assertEquals("No GiftCertificate with (id = " + giftCertificateId + ")", exception.getMessage());
    }

    @Test
    void deleteGiftCertificateByIdTestFalse_WhenSuchGiftCertificateExist() {
        long giftCertificateId = 1L;
        when(giftCertificateRepoMock.existsById(giftCertificateId)).thenReturn(true);
        assertTrue(giftCertificateServiceMock.deleteGiftCertificate(giftCertificateId));
        verify(giftCertificateRepoMock).deleteById(any());
    }

    @Test
    void updateGiftCertificateTest_ReturnUpdatedCertificateWithoutTags() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.giftCertificateExists(giftCertificate.getName())).thenReturn(false);
        when(giftCertificateRepoMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepoMock.save(giftCertificate)).thenReturn(giftCertificate);
        assertEquals(giftCertificate, giftCertificateServiceMock.updateGiftCertificate(1L, giftCertificate));
    }

    @Test
    void updateGiftCertificateTest_ReturnUpdatedCertificateWithTags() {
        Set<Tag> tags = Set.of(Tag.builder().name("2ha2rd1").build(), Tag.builder().name("exp12").build());
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate")
                .description("description").price(1).duration(1).tags(tags).build();
        when(giftCertificateRepoMock.giftCertificateExists(giftCertificate.getName())).thenReturn(false);
        when(giftCertificateRepoMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepoMock.save(giftCertificate)).thenReturn(giftCertificate);
        when(tagServiceMock.saveAllTags(tags)).thenReturn(true);
        assertEquals(giftCertificate, giftCertificateServiceMock.updateGiftCertificate(1L, giftCertificate));
    }

    @Test
    void updateGiftCertificateTest_ItemNotFoundException() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.giftCertificateExists(giftCertificate.getName())).thenReturn(true);
        when(giftCertificateRepoMock.findById(1L))
                .thenReturn(Optional.of(GiftCertificate.builder().name("secondGiftCertificate").build()));
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.updateGiftCertificate(1L, giftCertificate));
        assertEquals("Gift certificate with such name already exists " + giftCertificate.getName(), exception.getMessage());
    }

    @Test
    void updateDurationTrue_WhenGiftCertificateExist() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepoMock.save(giftCertificate)).thenReturn(GiftCertificate
                .builder().name("giftCertificate").duration(100).build());

        assertEquals(giftCertificate, giftCertificateServiceMock.updateDuration(1L, 100));
    }

    @Test
    void updateDurationTestItemNotFoundException_WhenGiftCertificateNotExist() {
        long giftCertificateId = 1L;
        when(giftCertificateRepoMock.findById(giftCertificateId)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.updateDuration(giftCertificateId, 100));

        assertEquals("No gift certificate with (id = " + giftCertificateId + ")", exception.getMessage());
    }

    @Test
    void updatePriceTestTrue_WhenGiftCertificateExist() {
        GiftCertificate giftCertificate = GiftCertificate.builder().name("giftCertificate").build();
        when(giftCertificateRepoMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepoMock.save(giftCertificate)).thenReturn(GiftCertificate
                .builder().name("giftCertificate").price(100).build());

        assertEquals(giftCertificate, giftCertificateServiceMock.updatePrice(1L, 100));
    }

    @Test
    void updatePriceTest_ItemNotFoundException_WhenGiftCertificateNotExist() {
        long giftCertificateId = 1L;
        when(giftCertificateRepoMock.findById(giftCertificateId)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.updatePrice(giftCertificateId, 100));

        assertEquals("No gift certificate with (id = " + giftCertificateId + ")", exception.getMessage());
    }

    @Test
    void getGiftCertificatesByTagNameTest() {
        List<GiftCertificate> giftCertificateList = List.of(GiftCertificate.builder().name("tag").build());
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(giftCertificateList);
        when(giftCertificateRepoMock.getGiftCertificatesByTagName("tag", 0, 3))
                .thenReturn(allGiftCertificates.stream().toList());
        assertEquals(allGiftCertificates, giftCertificateServiceMock.getGiftCertificatesByTagName("tag", 0, 3));
    }

    @Test
    void getGiftCertificateWithTagsByPartOfDescriptionTest() {
        List<GiftCertificate> giftCertificateList = List.of(GiftCertificate.builder().description("tag").build());
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(giftCertificateList);
        when(giftCertificateRepoMock.getGiftCertificateByPartOfDescription("tag%", 0, 3))
                .thenReturn(allGiftCertificates.stream().toList());
        assertEquals(allGiftCertificates, giftCertificateServiceMock.getGiftCertificateByPartOfDescription("tag", 0, 3));
    }

    @Test
    void getGiftCertificatesSortedByNameTest() {
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));
        when(giftCertificateRepoMock.findAll(pageRequest)).thenReturn(allGiftCertificates);
        assertEquals(allGiftCertificates, giftCertificateServiceMock.getGiftCertificatesSortedByName("DESC", 0, 3));
    }

    @Test
    void getGiftCertificatesSortedByNameByDateTest() {
        String sortDirectionName = "Desc";
        String sortDirectionDate = "Asc";
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(List.of(new GiftCertificate()));
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.valueOf(sortDirectionName.toUpperCase()), "name")
                .and(Sort.by(Sort.Direction.valueOf(sortDirectionDate.toUpperCase()), "createDate")));
        when(giftCertificateRepoMock.findAll(pageRequest)).thenReturn(allGiftCertificates);
        assertEquals(allGiftCertificates, giftCertificateServiceMock.getGiftCertificatesSortedByNameByDate(sortDirectionName, sortDirectionDate, 0, 3));
    }

    @Test
    void getGiftCertificatesByTagsTest() {
        Set<String> tagNamesSet = Set.of("tag");
        List<GiftCertificate> giftCertificateList = List.of(GiftCertificate.builder().name("tag").build());
        Page<GiftCertificate> allGiftCertificates = new PageImpl<>(giftCertificateList);
        when(giftCertificateRepoMock.getByTags(tagNamesSet, 1, 0, 3))
                .thenReturn(allGiftCertificates.stream().toList());
        assertEquals(allGiftCertificates, giftCertificateServiceMock.getGiftCertificatesByTags(tagNamesSet, 0, 3));
    }
}