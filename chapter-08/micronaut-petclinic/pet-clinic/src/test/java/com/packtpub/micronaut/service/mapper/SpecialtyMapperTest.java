package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialtyMapperTest {

    private SpecialtyMapper specialityMapper;

    @BeforeEach
    public void setUp() {
        specialityMapper = new SpecialtyMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(specialityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(specialityMapper.fromId(null)).isNull();
    }
}
