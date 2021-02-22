package com.packtpub.micronaut.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitMapperTest {

    private VisitMapper visitMapper;

    @BeforeEach
    public void setUp() {
        visitMapper = new VisitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(visitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(visitMapper.fromId(null)).isNull();
    }
}
