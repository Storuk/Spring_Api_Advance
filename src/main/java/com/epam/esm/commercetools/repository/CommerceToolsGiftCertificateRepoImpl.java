package com.epam.esm.commercetools.repository;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.common.PriceDraft;
import com.commercetools.api.models.product.*;
import com.commercetools.api.models.product_type.ProductTypeResourceIdentifier;
import com.epam.esm.commercetools.graphql.mapper.GraphQlToGiftCertificatesMapper;
import com.epam.esm.commercetools.graphql.models.GetByTagsGraphQlRequest;
import com.epam.esm.commercetools.mapper.CommerceToolsProductToGiftCertificateMapperImpl;
import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;
import com.epam.esm.commercetools.model.CommerceToolsTag;
import com.epam.esm.commercetools.model.SortingTypesRequest;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import com.epam.esm.tag.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommerceToolsGiftCertificateRepoImpl implements CommerceToolsGiftCertificateRepo {
    private final ProjectApiRoot apiRoot;
    private final CommerceToolsProductToGiftCertificateMapperImpl commerceToolsProductToGiftCertificateMapper;
    private static final String PRODUCT_TYPE = "0fdc4ec5-4536-4fad-9a70-8882dc87578d";
    private static final String GIFT_CERTIFICATE_DURATION = "duration";
    private static final String GIFT_CERTIFICATE_TAGS = "tags";
    private static final long FROM_EUR_TO_CENT_MULTIPLIER = 100;
    private static final String PRICE_CURRENCY = "EUR";
    private final GraphQlToGiftCertificatesMapper graphQlToGiftCertificatesMapper;

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificatesByTagsName(Set<String> tagsName, PageRequest pageRequest, String operator) {
        GetByTagsGraphQlRequest getByTagsGraphQlRequest = GetByTagsGraphQlRequest
                .builder()
                .pageRequest(pageRequest)
                .setOfTags(tagsName)
                .operator(operator)
                .build();

        return graphQlToGiftCertificatesMapper
                .getGiftCertificateListFromGraphQlResponse(
                        apiRoot
                                .graphql()
                                .post(getByTagsGraphQlRequest)
                                .executeBlocking()
                                .getBody());
    }

    @Override
    public CommerceToolsGiftCertificate updateGiftCertificate(String id, long version, GiftCertificateDTO giftCertificateDTO) {
        return commerceToolsProductToGiftCertificateMapper.convertCommerceToolsProductToCommerceToolsGiftCertificate(apiRoot
                .products()
                .withId(id)
                .post(ProductUpdate
                        .builder()
                        .version(version)
                        .actions(setActionsForUpdate(giftCertificateDTO))
                        .build())
                .executeBlocking()
                .getBody());
    }

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificates(PageRequest pageRequest) {
        return commerceToolsProductToGiftCertificateMapper
                .convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(
                        apiRoot
                                .products()
                                .get()
                                .withOffset(pageRequest.getOffset())
                                .withLimit(pageRequest.getPageSize())
                                .executeBlocking()
                                .getBody()
                                .getResults());
    }

    @Override
    public CommerceToolsGiftCertificate getGiftCertificateById(String id) {
        return commerceToolsProductToGiftCertificateMapper
                .convertCommerceToolsProductToCommerceToolsGiftCertificate(
                        apiRoot
                                .products()
                                .withId(id)
                                .get()
                                .executeBlocking()
                                .getBody());
    }

    @Override
    public void deleteGiftCertificate(String id, long version) {
        apiRoot.products()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking()
                .getBody();
    }

    @Override
    public long getCommerceToolsProductVersion(String id) {
        return apiRoot
                .products()
                .withId(id)
                .get()
                .executeBlocking()
                .getBody()
                .getVersion();
    }

    @Override
    public void unPublishProduct(String id, long version) {
        apiRoot.products()
                .withId(id)
                .post(ProductUpdate
                        .builder()
                        .version(version)
                        .actions(ProductUpdateAction.unpublishBuilder().build())
                        .build())
                .executeBlocking()
                .getBody();
    }

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificatesSortedByName(String sortDirection, PageRequest pageRequest) {
        return commerceToolsProductToGiftCertificateMapper
                .convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(
                        apiRoot
                                .products()
                                .get()
                                .withSort(getNameSort(sortDirection))
                                .withOffset(pageRequest.getOffset())
                                .withLimit(pageRequest.getPageSize())
                                .executeBlocking()
                                .getBody()
                                .getResults());
    }

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificatesSortedByNameByDate(SortingTypesRequest sortingTypes, PageRequest pageRequest) {
        return commerceToolsProductToGiftCertificateMapper
                .convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(
                        apiRoot
                                .products()
                                .get()
                                .withSort(getNameSort(sortingTypes.getSortingTypeName()))
                                .addSort(getDateSort(sortingTypes.getSortingTypeDate()))
                                .withOffset(pageRequest.getOffset())
                                .withLimit(pageRequest.getPageSize())
                                .executeBlocking()
                                .getBody()
                                .getResults());
    }

    private String getNameSort(String sortingTypeName) {
        return "masterData.current.name.en-us " + sortingTypeName.toLowerCase();
    }

    private String getDateSort(String sortingTypeDate) {
        return "createdAt " + sortingTypeDate.toLowerCase();
    }

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificatesByPartOfDescription(String description, PageRequest pageRequest) {
        return commerceToolsProductToGiftCertificateMapper
                .convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(
                        apiRoot
                                .products()
                                .get()
                                .executeBlocking()
                                .getBody()
                                .getResults())
                .stream()
                .filter(commerceToolsGiftCertificate -> commerceToolsGiftCertificate
                        .getDescription()
                        .startsWith(description))
                .skip(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .sorted(comparing(CommerceToolsGiftCertificate::getProductId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificatesByTagNames(Set<String> tagNameSet, PageRequest pageRequest) {
        return commerceToolsProductToGiftCertificateMapper
                .convertListOfCommerceToolsProductToListOfCommerceToolsGiftCertificates(
                        apiRoot
                                .products()
                                .get()
                                .executeBlocking()
                                .getBody()
                                .getResults())
                .stream()
                .filter(commerceToolsGiftCertificate -> commerceToolsGiftCertificate
                        .getTags()
                        .stream()
                        .map(CommerceToolsTag::getName)
                        .collect(Collectors.toSet())
                        .containsAll(tagNameSet))
                .skip(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .sorted(Comparator.comparing(CommerceToolsGiftCertificate::getProductId))
                .collect(Collectors.toList());
    }

    @Override
    public CommerceToolsGiftCertificate createGiftCertificate(GiftCertificateDTO giftCertificate) {
        return commerceToolsProductToGiftCertificateMapper
                .convertCommerceToolsProductToCommerceToolsGiftCertificate(apiRoot
                        .products()
                        .create(ProductDraft
                                .builder()
                                .productType(getProductTypeResourceIdentifier())
                                .name(getLocalizedGiftCertificateName(giftCertificate))
                                .description(getLocalizedGiftCertificateDescription(giftCertificate))
                                .masterVariant(getProductVariantDraft(giftCertificate))
                                .slug(getProductSlug(giftCertificate))
                                .publish(true)
                                .build())
                        .executeBlocking()
                        .getBody());
    }

    private ProductVariantDraft getProductVariantDraft(GiftCertificateDTO giftCertificate) {
        List<Attribute> attributes = setDurationAndTagsForCreate(giftCertificate);
        return ProductVariantDraft
                .builder()
                .prices(getProductPriceDraft(giftCertificate))
                .attributes(attributes)
                .build();
    }

    private List<Attribute> setDurationAndTagsForCreate(GiftCertificateDTO giftCertificate) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(setDurationAttribute(giftCertificate));
        if (giftCertificate.getTags() != null) {
            attributes.add(setTagsAttribute(giftCertificate.getTags()));
        }
        return attributes;
    }

    private PriceDraft getProductPriceDraft(GiftCertificateDTO giftCertificate) {
        return PriceDraft
                .builder()
                .value(Money
                        .builder()
                        .currencyCode(PRICE_CURRENCY)
                        .centAmount(giftCertificate.getPrice().longValue() * FROM_EUR_TO_CENT_MULTIPLIER)
                        .build())
                .build();
    }

    public List<ProductUpdateAction> setActionsForUpdate(GiftCertificateDTO giftCertificate) {
        List<ProductUpdateAction> actionsList = new ArrayList<>();
        if (giftCertificate.getName() != null) {
            actionsList.add(ProductUpdateAction.changeNameBuilder()
                    .name(getLocalizedGiftCertificateName(giftCertificate))
                    .build());
        }
        if (giftCertificate.getDescription() != null) {
            actionsList.add(ProductUpdateAction.setDescriptionBuilder()
                    .description(getLocalizedGiftCertificateDescription(giftCertificate))
                    .build());
        }
        if (giftCertificate.getPrice() != null) {
            actionsList.add(ProductUpdateAction
                    .setPricesBuilder()
                    .variantId(1L)
                    .prices(getProductPriceDraft(giftCertificate))
                    .build());
        }
        if (giftCertificate.getDuration() != null) {
            Attribute durationAttribute = setDurationAttribute(giftCertificate);
            actionsList.add(ProductUpdateAction
                    .setAttributeBuilder()
                    .variantId(1L)
                    .name(durationAttribute.getName())
                    .value(durationAttribute.getValue())
                    .build());
        }
        if (giftCertificate.getTags() != null) {
            Attribute durationAttribute = setTagsAttribute(giftCertificate.getTags());
            actionsList.add(ProductUpdateAction
                    .setAttributeBuilder()
                    .variantId(1L)
                    .name(durationAttribute.getName())
                    .value(durationAttribute.getValue())
                    .build());
        }
        actionsList.add(ProductUpdateAction.publishBuilder().build());
        return actionsList;
    }

    private LocalizedString getLocalizedGiftCertificateName(GiftCertificateDTO giftCertificate) {
        return LocalizedString.of(Locale.US, giftCertificate.getName());
    }

    private LocalizedString getLocalizedGiftCertificateDescription(GiftCertificateDTO giftCertificate) {
        return LocalizedString.of(Locale.US, giftCertificate.getDescription());
    }

    private ProductTypeResourceIdentifier getProductTypeResourceIdentifier() {
        return ProductTypeResourceIdentifier
                .builder()
                .id(PRODUCT_TYPE)
                .build();
    }

    private LocalizedString getProductSlug(GiftCertificateDTO giftCertificate) {
        return LocalizedString.of(Locale.US, giftCertificate
                .getName().replaceAll("\\s", ""));
    }

    private Attribute setTagsAttribute(Set<TagDTO> tags) {
        List<String> tagsName = new ArrayList<>();
        for (TagDTO tag : tags) {
            tagsName.add(tag.getName());
        }
        return Attribute
                .builder()
                .name(GIFT_CERTIFICATE_TAGS)
                .value(tagsName)
                .build();
    }

    private Attribute setDurationAttribute(GiftCertificateDTO giftCertificate) {
        return Attribute
                .builder()
                .name(GIFT_CERTIFICATE_DURATION)
                .value(giftCertificate.getDuration())
                .build();
    }
}