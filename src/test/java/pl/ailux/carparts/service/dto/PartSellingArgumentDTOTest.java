package pl.ailux.carparts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.ailux.carparts.web.rest.TestUtil;

public class PartSellingArgumentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartSellingArgumentDTO.class);
        PartSellingArgumentDTO partSellingArgumentDTO1 = new PartSellingArgumentDTO();
        partSellingArgumentDTO1.setId(1L);
        PartSellingArgumentDTO partSellingArgumentDTO2 = new PartSellingArgumentDTO();
        assertThat(partSellingArgumentDTO1).isNotEqualTo(partSellingArgumentDTO2);
        partSellingArgumentDTO2.setId(partSellingArgumentDTO1.getId());
        assertThat(partSellingArgumentDTO1).isEqualTo(partSellingArgumentDTO2);
        partSellingArgumentDTO2.setId(2L);
        assertThat(partSellingArgumentDTO1).isNotEqualTo(partSellingArgumentDTO2);
        partSellingArgumentDTO1.setId(null);
        assertThat(partSellingArgumentDTO1).isNotEqualTo(partSellingArgumentDTO2);
    }
}
