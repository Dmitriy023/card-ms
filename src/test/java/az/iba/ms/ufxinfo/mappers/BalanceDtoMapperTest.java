package az.iba.ms.ufxinfo.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.models.azericard.commons.ExtraRsContent;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import az.iba.ms.ufxinfo.utils.ResourceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BalanceDtoMapperTest {

    @Autowired
    private BalanceDtoMapper balanceDtoMapper;

    @MockBean
    private ResponseParser responseParser;

    private UfxMsg buildBalanceResponse() throws JsonProcessingException {
        String xmlResponse = ResourceReader.readFileToString("classpath:Balance.rs.xml");

        ResponseParser responseParser = new ResponseParser();
        return responseParser.parse(xmlResponse);
    }

    @BeforeEach
    public void beforeEach() {

        when(responseParser.parseContent(any()))
                .thenReturn(ExtraRsContent.builder().plasticType("0160SVED").build());
    }

    @Test
    public void shouldMapBalanceResponseToCardDto() throws JsonProcessingException {

        UfxMsg balanceResponse = buildBalanceResponse();

        BalanceDto balanceDto = balanceDtoMapper.map(balanceResponse);

        assertThat(balanceDto.getContractNumber()).isEqualTo("N4444444444444444");
        assertThat(balanceDto.getCurrency()).isEqualTo("AZN");
        assertThat(balanceDto.getCardTypeCode()).isEqualTo("0160SVED");
        assertThat(balanceDto.getBlockedAmount()).isEqualTo("0.00");
        assertThat(balanceDto.getAvailableBalance()).isEqualTo("99999.99");
        assertThat(balanceDto.getCreditLimit()).isEqualTo("12.00");
        assertThat(balanceDto.getBonus()).isEqualTo("13.00");
        assertThat(balanceDto.getExtraInfo())
                .isEqualTo("CLIENT_ID=9999999;CREDIT_TYPE=D;PLASTIC_TYPE=0160SVED;");
        assertThat(balanceDto.getRegNumber()).isEqualTo("CB12/10800");
        assertThat(balanceDto.getCardStatusClass()).isEqualTo("Valid");
        assertThat(balanceDto.getCardStatusCode()).isEqualTo("00");
        assertThat(balanceDto.getCardStatus()).isEqualTo("Card OK");
        assertThat(balanceDto.getCardBin()).isEqualTo("444444");
    }

    @Test
    public void shouldNotSetContractNumber_WhenMainContractNumberIsBlank()
            throws JsonProcessingException {

        UfxMsg balanceResponse = buildBalanceResponse();
        balanceResponse
                .getMsgData()
                .getInformation()
                .getDataRs()
                .getContractRs()
                .getContract()
                .getMainContract()
                .getContractIdt()
                .setContractNumber(null);

        BalanceDto balanceDto = balanceDtoMapper.map(balanceResponse);

        assertThat(balanceDto.getContractNumber()).isNull();

        balanceResponse
                .getMsgData()
                .getInformation()
                .getDataRs()
                .getContractRs()
                .getContract()
                .setMainContract(null);

        balanceDto = balanceDtoMapper.map(balanceResponse);

        assertThat(balanceDto.getContractNumber()).isNull();
    }

    @Test
    public void shouldHaveDefaultValueForAmount() throws JsonProcessingException {

        UfxMsg balanceResponse = buildBalanceResponse();
        balanceResponse
                .getMsgData()
                .getInformation()
                .getDataRs()
                .getContractRs()
                .getInfo()
                .setBalances(new ArrayList<>());

        BalanceDto balanceDto = balanceDtoMapper.map(balanceResponse);

        assertThat(balanceDto.getAvailableBalance()).isNull();
        assertThat(balanceDto.getCreditLimit()).isNull();
        assertThat(balanceDto.getBonus()).isNull();
        assertThat(balanceDto.getBlockedAmount()).isNull();
    }
}
