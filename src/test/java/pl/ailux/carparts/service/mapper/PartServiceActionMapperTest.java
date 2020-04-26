package pl.ailux.carparts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PartServiceActionMapperTest {

    private PartServiceActionMapper partServiceActionMapper;

    @BeforeEach
    public void setUp() {
        partServiceActionMapper = new PartServiceActionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(partServiceActionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(partServiceActionMapper.fromId(null)).isNull();
    }
}
