package pl.ailux.carparts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PartSellingArgumentMapperTest {

    private PartSellingArgumentMapper partSellingArgumentMapper;

    @BeforeEach
    public void setUp() {
        partSellingArgumentMapper = new PartSellingArgumentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(partSellingArgumentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(partSellingArgumentMapper.fromId(null)).isNull();
    }
}
