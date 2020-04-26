package pl.ailux.carparts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CarPartMapperTest {

    private CarPartMapper carPartMapper;

    @BeforeEach
    public void setUp() {
        carPartMapper = new CarPartMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(carPartMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(carPartMapper.fromId(null)).isNull();
    }
}
