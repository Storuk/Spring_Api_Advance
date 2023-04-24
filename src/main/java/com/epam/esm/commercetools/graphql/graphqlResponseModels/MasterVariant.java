package com.epam.esm.commercetools.graphql.graphqlResponseModels;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class MasterVariant {
    private List<Prices> prices;
    private List<AttributesRaw> attributesRaw;
}
