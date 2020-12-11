package az.iba.ms.ufxinfo.builders;

import static org.assertj.core.api.Assertions.assertThat;

import az.iba.ms.ufxinfo.builders.azericard.BalanceRequestBuilder;
import az.iba.ms.ufxinfo.models.azericard.commons.Information;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BalanceRequestBuilderTest {

    @Autowired
    private BalanceRequestBuilder balanceRequestBuilder;

    @Test
    public void shouldBuildBalanceRequest() {
        String cardNumber = "4444444444444444";

        UfxMsg ufxMsg = balanceRequestBuilder.build(cardNumber);

        assertThat(ufxMsg.getScheme()).isEqualTo("WAY4Appl");
        assertThat(ufxMsg.getMsgType()).isEqualTo("Information");
        assertThat(ufxMsg.getDirection()).isEqualTo("Rq");
        assertThat(ufxMsg.getVersion()).isEqualTo("2.0");
        assertThat(ufxMsg.getMsgId()).isNotBlank();
        assertThat(ufxMsg.getSource().getApp()).isEqualTo("IBAR");

        Information information = ufxMsg.getMsgData().getInformation();
        assertThat(information.getInstitution()).isEqualTo("0010");
        assertThat(information.getOrderDprt()).isEqualTo("0101");
        assertThat(information.getObjectType()).isEqualTo("Contract");
        assertThat(information.getActionType()).isEqualTo("Inquiry");
        assertThat(information.getRegNumber()).isEqualTo("AAA-0002-545514");

        List<Parm> resultDts = information.getResultDtls();
        assertThat(resultDts.get(0).getParmCode()).isEqualTo("Balance");
        assertThat(resultDts.get(0).getValue()).isEqualTo("AVAILABLE;BLOCKED;CR_LIMIT;");

        assertThat(resultDts.get(1).getParmCode()).isEqualTo("Status");
        assertThat(resultDts.get(1).getValue()).isEqualTo("Y");

        assertThat(resultDts.get(2).getParmCode()).isEqualTo("Client");
        assertThat(resultDts.get(2).getValue()).isEqualTo("Y");

        assertThat(resultDts.get(3).getParmCode()).isEqualTo("Parent");
        assertThat(resultDts.get(3).getValue()).isEqualTo("Y");

        assertThat(resultDts.get(4).getParmCode()).isEqualTo("OrderingEnabled");
        assertThat(resultDts.get(4).getValue()).isEqualTo("Y");

        assertThat(information.getObjectFor().getContractIdt().getContractNumber())
                .isEqualTo("4444444444444444");
    }
}
