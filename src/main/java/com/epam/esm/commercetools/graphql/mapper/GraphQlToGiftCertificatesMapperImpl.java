package com.epam.esm.commercetools.graphql.mapper;

import com.commercetools.api.models.graph_ql.GraphQLResponse;
import com.epam.esm.commercetools.graphql.graphqlResponseModels.GraphQlCertificateResponse;
import com.epam.esm.commercetools.graphql.graphqlResponseModels.Results;
import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;
import com.epam.esm.commercetools.model.CommerceToolsTag;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphQlToGiftCertificatesMapperImpl implements GraphQlToGiftCertificatesMapper {
    @Override
    public List<CommerceToolsGiftCertificate> getGiftCertificateListFromGraphQlResponse(GraphQLResponse graphQlResponse) {
        List<Results> results =
                getResultsModelFromGraphQlResponse(graphQlResponse.getData());

        if (!results.isEmpty()) {
            List<CommerceToolsGiftCertificate> commerceGiftCertificates = new ArrayList<>();
            results.forEach(
                    result ->
                            commerceGiftCertificates.add(CommerceToolsGiftCertificate
                                    .builder()
                                    .productId(result.getId())
                                    .name(getName(result))
                                    .description(getDescription(result))
                                    .price(getPrice(result))
                                    .duration(getDuration(result))
                                    .createDate(getCreateDate(result))
                                    .lastUpdateDate(getLastUpdateDate(result))
                                    .tags(getTags(result))
                                    .build())
            );
            return commerceGiftCertificates;
        }
        return List.of();
    }

    private String getName(Results results) {
        return results
                .getMasterData()
                .getCurrent()
                .getNameAllLocales()
                .get(0)
                .getValue();
    }

    private String getDescription(Results results) {
        return results
                .getMasterData()
                .getCurrent()
                .getDescriptionAllLocales()
                .get(0)
                .getValue();
    }

    private Integer getPrice(Results results) {
        return Math.toIntExact(results
                .getMasterData()
                .getCurrent()
                .getMasterVariant()
                .getPrices().get(0).getValue().getCentAmount());
    }

    private Integer getDuration(Results results) {
        return Integer.valueOf(results
                .getMasterData()
                .getCurrent()
                .getMasterVariant()
                .getAttributesRaw()
                .get(0).getValue().toString());
    }

    private Date getCreateDate(Results results) {
        return Date.from(results
                .getCreatedAt()
                .toInstant());
    }

    private Date getLastUpdateDate(Results results) {
        return Date.from(results
                .getLastModifiedAt()
                .toInstant());
    }

    private Set<CommerceToolsTag> getTags(Results results) {
        List<String> tagNames = (List<String>) results
                .getMasterData()
                .getCurrent()
                .getMasterVariant()
                .getAttributesRaw()
                .get(1)
                .getValue();

        Set<CommerceToolsTag> commerceToolsTags = new HashSet<>();
        for (String tagName : tagNames) {
            commerceToolsTags.add(CommerceToolsTag.builder().name(tagName).build());
        }
        return commerceToolsTags;
    }

    private List<Results> getResultsModelFromGraphQlResponse(Object response) {
        return JsonMapper.builder().findAndAddModules().build()
                .convertValue(response, GraphQlCertificateResponse.class).getProducts().getResults();
    }
}