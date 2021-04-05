package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PetMapperTest {

    private PetMapper petMapper;

    @BeforeEach
    public void setUp() {
        petMapper = new PetMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(petMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(petMapper.fromId(null)).isNull();
    }
}
