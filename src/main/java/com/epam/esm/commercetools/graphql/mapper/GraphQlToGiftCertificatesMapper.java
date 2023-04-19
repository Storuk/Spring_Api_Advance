package com.epam.esm.commercetools.graphql.mapper;

import com.commercetools.api.models.graph_ql.GraphQLResponse;
import com.epam.esm.commercetools.model.CommerceToolsGiftCertificate;

import java.util.List;

public interface GraphQlToGiftCertificatesMapper {
    List<CommerceToolsGiftCertificate> getGiftCertificateListFromGraphQlResponse(GraphQLResponse graphQlResponse);
}
