package pl.ailux.carparts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class CarPartDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarPartDTO.class);
        CarPartDTO carPartDTO1 = new CarPartDTO();
        carPartDTO1.setId(1L);
        CarPartDTO carPartDTO2 = new CarPartDTO();
        assertThat(carPartDTO1).isNotEqualTo(carPartDTO2);
        carPartDTO2.setId(carPartDTO1.getId());
        assertThat(carPartDTO1).isEqualTo(carPartDTO2);
        carPartDTO2.setId(2L);
        assertThat(carPartDTO1).isNotEqualTo(carPartDTO2);
        carPartDTO1.setId(null);
        assertThat(carPartDTO1).isNotEqualTo(carPartDTO2);
    }
}
