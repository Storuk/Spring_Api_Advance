package com.epam.esm.commercetools.graphql.models;

import com.commercetools.api.models.graph_ql.GraphQLVariablesMap;
import com.epam.esm.commercetools.graphql.GraphQlQueries;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
public class GetByTagsGraphQlRequest extends CustomGraphQlRequest {
    private PageRequest pageRequest;
    private Set<String> setOfTags;
    private String operator;
    private static final String query = GraphQlQueries.getAllGiftCertificatesByTagName;
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String WHERE = "where";
    private static GraphQLVariablesMap graphQLVariablesMap;

    public GetByTagsGraphQlRequest(PageRequest pageRequest, Set<String> setOfTags, String operator) {
        super(query);
        this.pageRequest = pageRequest;
        this.setOfTags = setOfTags;
        this.operator = operator;
        graphQLVariablesMap = GraphQLVariablesMap.builder()
                .addValue(WHERE, generateTagsVariableRequest(setOfTags, operator))
                .addValue(OFFSET, pageRequest.getOffset())
                .addValue(LIMIT, pageRequest.getPageSize())
                .build();
    }

    @Override
    public GraphQLVariablesMap getVariables() {
        return graphQLVariablesMap;
    }

    private String generateTagsVariableRequest(Set<String> setOfTags, String operator) {
        List<String> tagNames = setOfTags.stream().toList();
        StringBuilder a = new StringBuilder();
        a.append("masterData(current(masterVariant(attributes(");
        for (String tagName : tagNames) {
            if (tagName.equals(tagNames.get(0))) {
                a.append("value = \"").append(tagName).append("\"");
            } else {
                a.append(setOperatorToRequest(operator)).append(tagName).append("\"");
            }
        }
        a.append("))))");
        return a.toString();
    }

    private String setOperatorToRequest(String operator) {
        return String.format("%s value = \"", operator);
    }

}
