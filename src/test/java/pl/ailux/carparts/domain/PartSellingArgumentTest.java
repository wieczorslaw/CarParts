package pl.ailux.carparts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class PartSellingArgumentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartSellingArgument.class);
        PartSellingArgument partSellingArgument1 = new PartSellingArgument();
        partSellingArgument1.setId(1L);
        PartSellingArgument partSellingArgument2 = new PartSellingArgument();
        partSellingArgument2.setId(partSellingArgument1.getId());
        assertThat(partSellingArgument1).isEqualTo(partSellingArgument2);
        partSellingArgument2.setId(2L);
        assertThat(partSellingArgument1).isNotEqualTo(partSellingArgument2);
        partSellingArgument1.setId(null);
        assertThat(partSellingArgument1).isNotEqualTo(partSellingArgument2);
    }
}
