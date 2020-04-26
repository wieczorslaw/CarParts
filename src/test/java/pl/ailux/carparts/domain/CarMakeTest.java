package pl.ailux.carparts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class CarMakeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarMake.class);
        CarMake carMake1 = new CarMake();
        carMake1.setId(1L);
        CarMake carMake2 = new CarMake();
        carMake2.setId(carMake1.getId());
        assertThat(carMake1).isEqualTo(carMake2);
        carMake2.setId(2L);
        assertThat(carMake1).isNotEqualTo(carMake2);
        carMake1.setId(null);
        assertThat(carMake1).isNotEqualTo(carMake2);
    }
}
