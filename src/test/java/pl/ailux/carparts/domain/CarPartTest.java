package pl.ailux.carparts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class CarPartTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarPart.class);
        CarPart carPart1 = new CarPart();
        carPart1.setId(1L);
        CarPart carPart2 = new CarPart();
        carPart2.setId(carPart1.getId());
        assertThat(carPart1).isEqualTo(carPart2);
        carPart2.setId(2L);
        assertThat(carPart1).isNotEqualTo(carPart2);
        carPart1.setId(null);
        assertThat(carPart1).isNotEqualTo(carPart2);
    }
}
