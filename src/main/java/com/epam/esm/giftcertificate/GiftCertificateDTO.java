package com.epam.esm.giftcertificate;

import com.epam.esm.tag.TagDTO;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftCertificateDTO {
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private Set<TagDTO> tags;
}