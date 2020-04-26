package pl.ailux.carparts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CarMakeMapperTest {

    private CarMakeMapper carMakeMapper;

    @BeforeEach
    public void setUp() {
        carMakeMapper = new CarMakeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(carMakeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(carMakeMapper.fromId(null)).isNull();
    }
}
