package com.epam.esm.tag;

import org.springframework.stereotype.Component;

@Component
public class TagDtoToEntityMapper {
    public Tag convertTagDtoToEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        return tag;
    }
}
