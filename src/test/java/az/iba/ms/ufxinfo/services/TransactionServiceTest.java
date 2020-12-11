package az.iba.ms.ufxinfo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.mappers.TransactionDtoMapper;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class TransactionServiceTest {

    private final String contractNumber = "44445555666677778888";
    private final String from = "2020-10-10";
    private final String to = "2020-10-11";
    private final int pageSize = 100;
    private final int page = 1;
    @MockBean
    private AzeriCardService azeriCardService;
    @Autowired
    private TransactionService transactionService;
    @MockBean
    private TransactionDtoMapper transactionDtoMapper;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void shouldReturnEmptyList_WhenNotSuccessfulMessageReturned()
            throws JsonProcessingException {

        when(azeriCardService.getTransactions(contractNumber, from, to, pageSize, page))
                .thenReturn(UfxMsg.builder().respCode("-2000").build());

        List<TransactionDto> result =
                transactionService.getTransactions(contractNumber, from, to, pageSize, page);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnCardTransactionDtos() throws JsonProcessingException {

        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionDtos.add(new TransactionDto());
        transactionDtos.add(new TransactionDto());
        transactionDtos.add(new TransactionDto());

        UfxMsg ufxMsg = UfxMsg.builder().respCode("0").build();

        when(azeriCardService.getTransactions(contractNumber, from, to, pageSize, page)).thenReturn(ufxMsg);
        when(transactionDtoMapper.map(ufxMsg, pageSize, page))
                .thenReturn(transactionDtos);

        List<TransactionDto> result =
                transactionService.getTransactions(contractNumber, from, to, pageSize, page);

        assertThat(result.size()).isEqualTo(3);

        when(azeriCardService.getTransactions(contractNumber, from, to, pageSize, page))
                .thenThrow(new JsonProcessingException("") {
                });

        result = transactionService.getTransactions(contractNumber, from, to, pageSize, page);

        assertThat(result).isEmpty();
    }
}
