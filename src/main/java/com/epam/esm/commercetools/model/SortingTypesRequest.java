package com.epam.esm.commercetools.model;

import com.epam.esm.annotations.ExcludeModelsCoverage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcludeModelsCoverage
public class SortingTypesRequest {
    private String sortingTypeName;
    private String sortingTypeDate;
}
