package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PetTypeMapperTest {

    private PetTypeMapper petTypeMapper;

    @BeforeEach
    public void setUp() {
        petTypeMapper = new PetTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(petTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(petTypeMapper.fromId(null)).isNull();
    }
}
