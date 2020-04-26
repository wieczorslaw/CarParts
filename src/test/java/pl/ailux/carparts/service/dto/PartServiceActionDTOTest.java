package pl.ailux.carparts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class PartServiceActionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartServiceActionDTO.class);
        PartServiceActionDTO partServiceActionDTO1 = new PartServiceActionDTO();
        partServiceActionDTO1.setId(1L);
        PartServiceActionDTO partServiceActionDTO2 = new PartServiceActionDTO();
        assertThat(partServiceActionDTO1).isNotEqualTo(partServiceActionDTO2);
        partServiceActionDTO2.setId(partServiceActionDTO1.getId());
        assertThat(partServiceActionDTO1).isEqualTo(partServiceActionDTO2);
        partServiceActionDTO2.setId(2L);
        assertThat(partServiceActionDTO1).isNotEqualTo(partServiceActionDTO2);
        partServiceActionDTO1.setId(null);
        assertThat(partServiceActionDTO1).isNotEqualTo(partServiceActionDTO2);
    }
}
