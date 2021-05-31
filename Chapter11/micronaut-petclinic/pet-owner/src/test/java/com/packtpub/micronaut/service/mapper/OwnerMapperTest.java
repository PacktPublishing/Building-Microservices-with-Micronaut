package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerMapperTest {

    private OwnerMapper ownerMapper;

    @BeforeEach
    public void setUp() {
        ownerMapper = new OwnerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ownerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ownerMapper.fromId(null)).isNull();
    }
}
