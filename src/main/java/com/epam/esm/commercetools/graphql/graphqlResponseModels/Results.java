package com.epam.esm.commercetools.graphql.graphqlResponseModels;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Results {
    private String id;
    private Date createdAt;
    private Date lastModifiedAt;
    private MasterData masterData;
}
