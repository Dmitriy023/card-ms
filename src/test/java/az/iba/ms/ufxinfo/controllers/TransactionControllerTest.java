package az.iba.ms.ufxinfo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import az.iba.ms.ufxinfo.dtos.AccountAmountDto;
import az.iba.ms.ufxinfo.dtos.BillingDto;
import az.iba.ms.ufxinfo.dtos.PostingDetailsDto;
import az.iba.ms.ufxinfo.dtos.ResponseDto;
import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.services.TransactionService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    private final String contractNumber = "4444555566667777";
    private final String from = "2020-10-10";
    private final String to = "2020-10-11";
    private final int pageSize = 100;
    private final int page = 1;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TransactionController transactionController;
    @MockBean
    private TransactionService transactionService;

    @Test
    public void shouldReturn200_WhenValidParamsProvided() throws Exception {
        String url = "/v1/cards/5490349934171340/transactions?from=2020-09-19&to=2020-09-21&pagesize=100&page=1";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn404_WhenPathParamNotProvided() throws Exception {
        String url = "/v1/cards/transactions";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn400_WhenParamsNotProvided() throws Exception {
        String url = "/v1/cards/5490349934171340/transactions";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400_WhenPageSizeOutOfRange() throws Exception {
        String url = "/v1/cards/5490349934171340/transactions?from=2020-09-19&to=2020-09-21&pagesize=1000&page=1";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400_WhenPageOutOfRange() throws Exception {
        String url = "/v1/cards/5490349934171340/transactions?from=2020-09-19&to=2020-09-21&pagesize=10&page=0";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400_WhenCardNumberContainsLetter() throws Exception {
        String url = "/v1/cards/5490349934171340a/transactions?from=2020-09-19&to=2020-09-21&pagesize=10&page=1";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400_WhenNotValidDateProvided() throws Exception {
        String url = "/v1/cards/5490349934171340/transactions?from=2020-9-19&to=2020-09-21&pagesize=10&page=1";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());

        url = "/v1/cards/5490349934171340/transactions?from=2020-09-19&to=2020-9-21&pagesize=10&page=1";
        mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnTransactionList_WhenTransactionDataFound() {

        TransactionDto dto = getTransactionDto();

        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionDtos.add(dto);
        transactionDtos.add(dto);

        when(transactionService.getTransactions(contractNumber, from, to, pageSize, page))
                .thenReturn(transactionDtos);

        ResponseEntity<ResponseDto<List<TransactionDto>>> responseEntity =
                transactionController.getTransactions(contractNumber, from, to, pageSize, page);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isEqualTo(transactionDtos);
    }

    @Test
    public void shouldReturnEmptyList_WhenNoTransactionDataFound() {

        List<TransactionDto> transactionDtos = new ArrayList<>();

        when(transactionService.getTransactions(contractNumber, from, to, pageSize, page))
                .thenReturn(transactionDtos);

        ResponseEntity<ResponseDto<List<TransactionDto>>> responseEntity =
                transactionController.getTransactions(contractNumber, from, to, pageSize, page);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData().size()).isEqualTo(0);
    }

    private TransactionDto getTransactionDto() {
        return TransactionDto.builder()
                .amount(BigDecimal.valueOf(1200, 2))
                .currency("AZN")
                .date("2020-09-20 22:09:02")
                .description("Cash withdraw AZE BAKU ATM CNV ARAZ M OLIMPIK")
                .mcc("6011")
                .trnTypeId("13")
                .trnTypeCode("A1")
                .rrn("026401256130")
                .status("Successfully completed")
                .localAmount(BigDecimal.valueOf(-9000, 2))
                .trnType("D")
                .descriptionExt("ATM CNV ARAZ M OLIMPIK")
                .postingDetailsDto(getPostingDetailsDto())
                .billingDto(getBillingDto())
                .build();
    }

    private PostingDetailsDto getPostingDetailsDto() {
        return PostingDetailsDto.builder()
                .accountAmountDto(
                        AccountAmountDto.builder()
                                .type("Full")
                                .amount(BigDecimal.valueOf(-9000, 2))
                                .currency("AZN")
                                .build())
                .processingStatus("Posted")
                .build();
    }

    private BillingDto getBillingDto() {
        return BillingDto.builder()
                .phaseDate("2020-09-21")
                .currency("AZN")
                .amount(BigDecimal.valueOf(9000, 2))
                .extras(new ArrayList<>())
                .build();
    }
}
