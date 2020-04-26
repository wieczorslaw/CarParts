package pl.ailux.carparts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class CarMakeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarMakeDTO.class);
        CarMakeDTO carMakeDTO1 = new CarMakeDTO();
        carMakeDTO1.setId(1L);
        CarMakeDTO carMakeDTO2 = new CarMakeDTO();
        assertThat(carMakeDTO1).isNotEqualTo(carMakeDTO2);
        carMakeDTO2.setId(carMakeDTO1.getId());
        assertThat(carMakeDTO1).isEqualTo(carMakeDTO2);
        carMakeDTO2.setId(2L);
        assertThat(carMakeDTO1).isNotEqualTo(carMakeDTO2);
        carMakeDTO1.setId(null);
        assertThat(carMakeDTO1).isNotEqualTo(carMakeDTO2);
    }
}
