package pl.ailux.carparts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class PartServiceActionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartServiceAction.class);
        PartServiceAction partServiceAction1 = new PartServiceAction();
        partServiceAction1.setId(1L);
        PartServiceAction partServiceAction2 = new PartServiceAction();
        partServiceAction2.setId(partServiceAction1.getId());
        assertThat(partServiceAction1).isEqualTo(partServiceAction2);
        partServiceAction2.setId(2L);
        assertThat(partServiceAction1).isNotEqualTo(partServiceAction2);
        partServiceAction1.setId(null);
        assertThat(partServiceAction1).isNotEqualTo(partServiceAction2);
    }
}
