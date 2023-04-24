package com.epam.esm.commercetools.graphql.graphqlResponseModels;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Value {
    private String currencyCode;
    private long centAmount;
}
