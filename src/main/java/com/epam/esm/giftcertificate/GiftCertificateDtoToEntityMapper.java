package com.epam.esm.giftcertificate;

import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GiftCertificateDtoToEntityMapper {

    public Set<Tag> convertSetOfTagDtoToSetOfTag(Set<TagDTO> tagDTOSet) {
        Set<Tag> tagSet = new HashSet<>();
        for (TagDTO tagDTO : tagDTOSet) {
            tagSet.add(Tag.builder().name(tagDTO.getName()).build());
        }
        return tagSet;
    }

    public GiftCertificate convertGiftCertificateDtoToGiftCertificate(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        if (giftCertificateDTO.getDescription() != null) {
            giftCertificate.setDescription(giftCertificateDTO.getDescription());
        }
        if (giftCertificateDTO.getName() != null) {
            giftCertificate.setName(giftCertificateDTO.getName());
        }
        if (giftCertificateDTO.getPrice() != null) {
            giftCertificate.setPrice(giftCertificateDTO.getPrice());
        }
        if (giftCertificateDTO.getDuration() != null) {
            giftCertificate.setDuration(giftCertificateDTO.getDuration());
        }
        if (giftCertificateDTO.getTags() != null) {
            giftCertificate.setTags(convertSetOfTagDtoToSetOfTag(giftCertificateDTO.getTags()));
        }
        return giftCertificate;
    }
}
