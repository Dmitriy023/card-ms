package az.iba.ms.ufxinfo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.utils.CardNumberConverter;
import az.iba.ms.ufxinfo.utils.ResourceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class AzeriCardServiceTest {

    private final String azeriCardAddress = "http://10.254.78.2:34444/way4gateibar/httpadapter";
    @Autowired
    private AzeriCardService azeriCardService;
    @MockBean
    private CardNumberConverter cardNumberConverter;
    @MockBean
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity<String> response;

    @Test
    public void shouldReturnBalanceResponse() throws Exception {
        String cardNumber = "213123121";

        when(cardNumberConverter.convert(any())).thenReturn(cardNumber);
        when(restTemplate.postForEntity(eq(azeriCardAddress), any(), eq(String.class)))
                .thenReturn(response);

        String xmlResponse = ResourceReader.readFileToString("classpath:Balance.rs.xml");

        when(response.getBody()).thenReturn(xmlResponse);

        UfxMsg balanceResponse = azeriCardService.getBalanceInfo(cardNumber);

        assertThat(balanceResponse).isNotNull();
    }

    @Test
    public void shouldReturnTransactionResponse() throws JsonProcessingException {
        String xmlResponse = ResourceReader.readFileToString("classpath:Transaction.rs.xml");

        when(cardNumberConverter.convert(any())).thenReturn("213123121");
        when(restTemplate.postForEntity(eq(azeriCardAddress), any(), eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(xmlResponse);

        UfxMsg transactionsResponse =
                azeriCardService.getTransactions(
                        "213123121", "2020-10-09", "2020-10-10", 10, 1);

        assertThat(transactionsResponse).isNotNull();
    }
}
