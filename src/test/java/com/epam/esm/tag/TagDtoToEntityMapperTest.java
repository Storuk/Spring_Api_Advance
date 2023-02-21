package com.epam.esm.tag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class TagDtoToEntityMapperTest {
    @InjectMocks
    TagDtoToEntityMapper tagDtoToEntityMapper;

    @Test
    void convertTagDtoToEntity() {
        Tag tag = Tag.builder().name("tag").build();
        TagDTO tagDTO = TagDTO.builder().name("tag").build();
        assertEquals(tag, tagDtoToEntityMapper.convertTagDtoToEntity(tagDTO));
    }
}