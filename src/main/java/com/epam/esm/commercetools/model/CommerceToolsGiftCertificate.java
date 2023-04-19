package com.epam.esm.commercetools.model;

import com.epam.esm.annotations.ExcludeModelsCoverage;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@ExcludeModelsCoverage
public class CommerceToolsGiftCertificate {
    private String productId;
    private String name;
    private String description;
    private Integer duration;
    private Date createDate;
    private Date lastUpdateDate;
    private Set<CommerceToolsTag> tags;
    private Integer price;
    private final String currency = "EUR";
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CommerceToolsGiftCertificate that)) return false;

        return new EqualsBuilder().append(getProductId(), that.getProductId()).append(getName(), that.getName()).append(getDescription(), that.getDescription()).append(getPrice(), that.getPrice()).append(getDuration(), that.getDuration()).append(getTags(), that.getTags()).append(getCreateDate(), that.getCreateDate()).append(getLastUpdateDate(), that.getLastUpdateDate()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProductId()).append(getName()).append(getDescription()).append(getPrice()).append(getDuration()).append(getTags()).append(getCreateDate()).append(getLastUpdateDate()).toHashCode();
    }
}
