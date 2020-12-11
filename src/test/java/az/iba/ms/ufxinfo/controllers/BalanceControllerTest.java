package az.iba.ms.ufxinfo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.dtos.ResponseDto;
import az.iba.ms.ufxinfo.services.BalanceService;
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

@WebMvcTest(controllers = BalanceController.class)
public class BalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BalanceController balanceController;

    @MockBean
    private BalanceService balanceService;

    @Test
    public void shouldReturn200_WhenParamsProvided() throws Exception {
        mockMvc
                .perform(get("/v1/balances?card-list=123").contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn400_WhenParamsNotProvided() throws Exception {
        mockMvc
                .perform(get("/v1/balances").contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400_WhenNotValidCifProvided() throws Exception {
        mockMvc
                .perform(get("/v1/balances?card-list=23121a").contentType("application/json"))
                .andExpect(status().isBadRequest());
        mockMvc
                .perform(get("/v1/balances?cardlist=23121,123121").contentType("application/json"))
                .andExpect(status().isBadRequest());
        mockMvc
                .perform(get("/v1/balances?card-list=,").contentType("application/json"))
                .andExpect(status().isBadRequest());
        mockMvc
                .perform(get("/v1/balances?card-list=,1231").contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBalance_WhenDataFound() {
        String cardNumber = "4444555566667777";

        BalanceDto dto =
                BalanceDto.builder()
                        .cardBin("cardBin")
                        .contractNumber("4444555566667777")
                        .availableBalance(BigDecimal.valueOf(1200, 2))
                        .blockedAmount(BigDecimal.valueOf(1300, 2))
                        .bonus(BigDecimal.valueOf(1400, 2))
                        .cardStatus("cardStatus")
                        .cardStatusClass("cardStatusClass")
                        .cardStatusCode("cardStatusCode")
                        .cardTypeCode("cardTypeCode")
                        .creditLimit(BigDecimal.valueOf(1600, 2))
                        .currency("AZN")
                        .extraInfo("extraInfo")
                        .regNumber("regNumber")
                        .build();

        List<BalanceDto> balanceDtos = new ArrayList<>();
        balanceDtos.add(dto);

        when(balanceService.getBalance(cardNumber)).thenReturn(balanceDtos);

        ResponseEntity<ResponseDto<List<BalanceDto>>> responseEntity =
                balanceController.getBalance(cardNumber);

        BalanceDto result = responseEntity.getBody().getData().get(0);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    public void shouldReturnEmptyList_WhenNoDataFound() {
        String cardNumber = "4444555566667777";

        List<BalanceDto> balanceDtos = new ArrayList<>();

        when(balanceService.getBalance(cardNumber)).thenReturn(balanceDtos);

        ResponseEntity<ResponseDto<List<BalanceDto>>> responseEntity =
                balanceController.getBalance(cardNumber);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData().size()).isEqualTo(0);
    }
}
