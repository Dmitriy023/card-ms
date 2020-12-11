package az.iba.ms.ufxinfo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import az.iba.ms.ufxinfo.utils.ResourceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class BalanceServiceTest {

    private final ResponseParser responseParser = new ResponseParser();
    @Autowired
    private BalanceService balanceService;
    @MockBean
    private AzeriCardService azeriCardService;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void shouldReturnEmptyList_WhenNotSuccessfulMessageReturned()
            throws JsonProcessingException {
        String cardNumbers = "4444555566667777";

        when(azeriCardService.getBalanceInfo("4444555566667777"))
                .thenReturn(UfxMsg.builder().respCode("-2000").build());

        List<BalanceDto> result = balanceService.getBalance(cardNumbers);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnBalanceDtos() throws JsonProcessingException {
        String cardNumbers = "4444555566667777,4444444444444444";

        String xmlResponse = ResourceReader.readFileToString("classpath:Balance.rs.xml");
        UfxMsg ufxMsg = responseParser.parse(xmlResponse);

        when(azeriCardService.getBalanceInfo("4444555566667777"))
                .thenReturn(ufxMsg);
        when(azeriCardService.getBalanceInfo("4444444444444444"))
                .thenReturn(ufxMsg);

        List<BalanceDto> result = balanceService.getBalance(cardNumbers);

        assertThat(result.size()).isEqualTo(2);

        when(azeriCardService.getBalanceInfo("4444555566667777"))
                .thenThrow(new JsonProcessingException("") {
                });

        result = balanceService.getBalance(cardNumbers);

        assertThat(result.size()).isEqualTo(1);
    }
}
