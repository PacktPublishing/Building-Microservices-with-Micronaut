package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VetMapperTest {

    private VetMapper vetMapper;

    @BeforeEach
    public void setUp() {
        vetMapper = new VetMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(vetMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(vetMapper.fromId(null)).isNull();
    }
}
