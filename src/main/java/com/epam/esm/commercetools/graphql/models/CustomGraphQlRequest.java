package com.epam.esm.commercetools.graphql.models;

import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.commercetools.api.models.graph_ql.GraphQLVariablesMap;

public abstract class CustomGraphQlRequest implements GraphQLRequest {
    private String query;
    private GraphQLVariablesMap graphQLVariablesMap;
    private String operationName;

    public CustomGraphQlRequest(String query) {
        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public GraphQLVariablesMap getVariables() {
        return graphQLVariablesMap;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Override
    public void setVariables(GraphQLVariablesMap variables) {
        this.graphQLVariablesMap = variables;
    }
}
