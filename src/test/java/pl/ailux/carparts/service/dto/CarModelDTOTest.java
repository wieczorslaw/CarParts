package pl.ailux.carparts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class CarModelDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarModelDTO.class);
        CarModelDTO carModelDTO1 = new CarModelDTO();
        carModelDTO1.setId(1L);
        CarModelDTO carModelDTO2 = new CarModelDTO();
        assertThat(carModelDTO1).isNotEqualTo(carModelDTO2);
        carModelDTO2.setId(carModelDTO1.getId());
        assertThat(carModelDTO1).isEqualTo(carModelDTO2);
        carModelDTO2.setId(2L);
        assertThat(carModelDTO1).isNotEqualTo(carModelDTO2);
        carModelDTO1.setId(null);
        assertThat(carModelDTO1).isNotEqualTo(carModelDTO2);
    }
}
