package com.epam.esm.giftcertificate;

import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GiftCertificateDtoToEntityMapperTest {
    @InjectMocks
    private GiftCertificateDtoToEntityMapper giftCertificateDtoToEntityMapper;

    @Test
    void convertGiftCertificateDtoToGiftCertificate() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("giftCertificate").tags(Set.of(Tag.builder().name("tag").build())).build();
        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder()
                .name("giftCertificate").tags(Set.of(TagDTO.builder().name("tag").build())).build();
        assertEquals(giftCertificate, giftCertificateDtoToEntityMapper.convertGiftCertificateDtoToGiftCertificate(giftCertificateDTO));
    }

    @Test
    void convertSetOfTagDtoToSetOfTag() {
        Set<Tag> tags = Set.of(Tag.builder().name("2ha2rd1").build(), Tag.builder().name("exp12").build());
        Set<TagDTO> tagsDTO = Set.of(TagDTO.builder().name("2ha2rd1").build(), TagDTO.builder().name("exp12").build());
        assertEquals(tags, giftCertificateDtoToEntityMapper.convertSetOfTagDtoToSetOfTag(tagsDTO));
    }
}