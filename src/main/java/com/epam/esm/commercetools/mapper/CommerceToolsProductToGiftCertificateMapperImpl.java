package com.epam.esm.commercetools.mapper;

import com.commercetools.api.models.product.Attribute;
import com.commercetools.api.models.product.Product;
import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;
import com.epam.esm.commercetools.model.CommerceToolsTag;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommerceToolsProductToGiftCertificateMapperImpl implements CommerceToolsProductToGiftCertificateMapper {

    @Override
    public CommerceToolsGiftCertificate convertCommerceToolsProductToCommerceToolsGiftCertificate(Product product) {
        List<Attribute> attributes = product.getMasterData()
                .getStaged()
                .getMasterVariant()
                .getAttributes();
        return CommerceToolsGiftCertificate.builder()
                .productId(product.getId())
                .name(getProductName(product))
                .price(getProductPrice(product))
                .description(getProductDescription(product))
                .createDate(getProductCreateDate(product))
                .lastUpdateDate(getProductLastModifiedAt(product))
                .duration(getProductDuration(attributes))
                .tags(getProductTags(attributes))
                .build();
    }

    @Override
    public List<CommerceToolsGiftCertificate> convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(List<Product> products) {
        List<CommerceToolsGiftCertificate> giftCertificates = new ArrayList<>();
        for (Product product : products) {
            giftCertificates.add(convertCommerceToolsProductToCommerceToolsGiftCertificate(product));
        }
        return giftCertificates;
    }

    private String getProductName(Product product) {
        return product.getMasterData()
                .getStaged()
                .getName()
                .get(Locale.ENGLISH);
    }

    private int getProductPrice(Product product) {
        return (product.getMasterData()
                .getStaged()
                .getMasterVariant()
                .getPrices()
                .get(0)
                .getValue()
                .getCentAmount()
                .intValue()) / 100;
    }

    private String getProductDescription(Product product) {
        return Objects.requireNonNull(product
                        .getMasterData()
                        .getStaged()
                        .getDescription())
                .get(Locale.ENGLISH);
    }

    private Date getProductCreateDate(Product product) {
        return Date.from(product.getCreatedAt().toInstant());
    }

    private Date getProductLastModifiedAt(Product product) {
        return Date.from(product.getLastModifiedAt().toInstant());
    }

    private Integer getProductDuration(List<Attribute> attributes) {
        return Integer.valueOf(attributes.get(0).getValue().toString());
    }

    private Set<CommerceToolsTag> getProductTags(List<Attribute> attributes) {
        attributes.remove(0);
        if (!attributes.iterator().hasNext()) {
            return Set.of();
        } else {
            Set<CommerceToolsTag> tags = new HashSet<>();
            List<String> tagNamesList = (List<String>) attributes.iterator().next().getValue();
            for (String tagName : tagNamesList) {
                tags.add(CommerceToolsTag.builder().name(tagName).build());
            }
            return tags;
        }
    }

//    private static List<Attribute> setDurationAndTagsForCrate(GiftCertificateDTO giftCertificate) {
//        List<Attribute> attributes = new ArrayList<>();
//        attributes.add(Attribute.builder().name("duration").value(giftCertificate.getDuration()).build());
//        if (giftCertificate.getTags() != null) {
//            attributes.add(Attribute.builder().name("tags").value(setTagsAttribute(giftCertificate.getTags())).build());
//        }
//        return attributes;
//    }
//
//    private static List<String> setTagsAttribute(Set<TagDTO> tags) {
//        List<String> tagsName = new ArrayList<>();
//        for (TagDTO tag : tags) {
//            tagsName.add(tag.getName());
//        }
//        return tagsName;
//    }
//    public static void main(String[] args) {
//        GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder().name("avc").duration(1)
//                .tags(Set.of(TagDTO.builder().name("tag1").build(), TagDTO.builder().name("tag2").build())).build();
//        List<Attribute> attributes = setDurationAndTagsForCrate(giftCertificateDTO);
//
//        CommerceToolsProductToGiftCertificateMapperImpl commerceToolsProductToGiftCertificateMapper = new CommerceToolsProductToGiftCertificateMapperImpl();
//
//        System.out.println(commerceToolsProductToGiftCertificateMapper.getProductTags(attributes));
//    }
}
