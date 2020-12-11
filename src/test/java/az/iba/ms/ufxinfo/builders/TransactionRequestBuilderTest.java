package az.iba.ms.ufxinfo.builders;

import static org.assertj.core.api.Assertions.assertThat;

import az.iba.ms.ufxinfo.builders.azericard.TransactionRequestBuilder;
import az.iba.ms.ufxinfo.models.azericard.commons.Doc;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionRequestBuilderTest {

    @Autowired
    private TransactionRequestBuilder transactionRequestBuilder;

    private final String contractNumber = "4444555566667777";
    private final String from = "2020-11-03";
    private final String to = "2020-11-04";
    private final int count = 100;

    @Test
    public void shouldBuildTransactionRequest() {

        UfxMsg ufxMsg = transactionRequestBuilder.build(contractNumber, from, to, count);

        assertThat(ufxMsg.getScheme()).isEqualTo("WAY4Doc");
        assertThat(ufxMsg.getMsgType()).isEqualTo("Doc");
        assertThat(ufxMsg.getDirection()).isEqualTo("Rq");
        assertThat(ufxMsg.getVersion()).isEqualTo("2.0");
        assertThat(ufxMsg.getMsgId()).isNotBlank();
        assertThat(ufxMsg.getSource().getApp()).isEqualTo("IBAR");

        checkDoc(ufxMsg.getMsgData().getDoc());

        checkResultDtls(ufxMsg.getMsgData().getDoc().getResultDtls());
    }

    private void checkDoc(Doc doc) {
        assertThat(doc.getTransType().getTransCode().getMsgCode()).isEqualTo("01000UFX");
        assertThat(doc.getTransType().getTransRules().get(0).getParmCode()).isEqualTo("SkipAuth");
        assertThat(doc.getTransType().getTransRules().get(0).getValue()).isEqualTo("Y");
        assertThat(doc.getRequestor().getContractNumber()).isEqualTo(contractNumber);
        assertThat(doc.getSource().getContractNumber()).isEqualTo("90100001");
        assertThat(doc.getSource().getInstInfo().getInstitutionIdType()).isEqualTo("BIN");
        assertThat(doc.getSource().getInstInfo().getInstitution()).isEqualTo("0010");
    }

    private void checkResultDtls(List<Parm> resultDtls) {
        assertThat(resultDtls.get(0).getParmCode()).isEqualTo("StmtType");
        assertThat(resultDtls.get(0).getValue()).isEqualTo("Additional");

        assertThat(resultDtls.get(1).getParmCode()).isEqualTo("StmtContType");
        assertThat(resultDtls.get(1).getValue()).isEqualTo("All");

        assertThat(resultDtls.get(2).getParmCode()).isEqualTo("Balance");
        assertThat(resultDtls.get(2).getValue()).isEqualTo("AVAILABLE");

        assertThat(resultDtls.get(3).getParmCode()).isEqualTo("DateFrom");
        assertThat(resultDtls.get(3).getValue()).isEqualTo(from);

        assertThat(resultDtls.get(4).getParmCode()).isEqualTo("DateTo");
        assertThat(resultDtls.get(4).getValue()).isEqualTo(to);

        assertThat(resultDtls.get(5).getParmCode()).isEqualTo("StmtMaxRecords");
        assertThat(resultDtls.get(5).getValue()).isEqualTo(count + "");

        assertThat(resultDtls.get(6).getParmCode()).isEqualTo("OrderingRule");
        assertThat(resultDtls.get(6).getValue()).isEqualTo("StmtDesc");
    }
}
