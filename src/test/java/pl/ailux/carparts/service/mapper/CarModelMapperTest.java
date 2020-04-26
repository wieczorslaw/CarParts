package pl.ailux.carparts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CarModelMapperTest {

    private CarModelMapper carModelMapper;

    @BeforeEach
    public void setUp() {
        carModelMapper = new CarModelMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(carModelMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(carModelMapper.fromId(null)).isNull();
    }
}
