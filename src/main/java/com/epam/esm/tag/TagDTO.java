package com.epam.esm.tag;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDTO {
    private String name;
}
