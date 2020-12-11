package az.iba.ms.ufxinfo.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import az.iba.ms.ufxinfo.dtos.AccountAmountDto;
import az.iba.ms.ufxinfo.dtos.BillingDto;
import az.iba.ms.ufxinfo.dtos.ExtraDto;
import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.models.azericard.commons.AccountAmount;
import az.iba.ms.ufxinfo.models.azericard.commons.DocData;
import az.iba.ms.ufxinfo.models.azericard.commons.Extra;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.SourceDtls;
import az.iba.ms.ufxinfo.models.azericard.commons.StmtItem;
import az.iba.ms.ufxinfo.models.azericard.commons.Transaction;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import az.iba.ms.ufxinfo.utils.ResourceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionDtoMapperTest {

    @Autowired
    TransactionDtoMapper mapper;

    private UfxMsg transactionResponse;
    private StmtItem stmtItem;

    private UfxMsg buildTransactionResponse() throws JsonProcessingException {
        String xmlResponse = ResourceReader.readFileToString("classpath:Transaction.rs.xml");

        ResponseParser responseParser = new ResponseParser();
        return responseParser.parse(xmlResponse);
    }

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        transactionResponse = buildTransactionResponse();
        stmtItem =
                transactionResponse
                        .getMsgData()
                        .getDoc()
                        .getDataRs()
                        .getStmt()
                        .getAdditionalStmt()
                        .getStmtItems()
                        .get(0);
    }

    @Test
    public void shouldMapToTransactionDto_WhenSampleResponseFileProvided() {

        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.size()).isEqualTo(1);

        TransactionDto dto = dtos.get(0);

        assertThat(dto.getAmount()).isEqualTo(BigDecimal.valueOf(22500, 2));
        assertThat(dto.getCurrency()).isEqualTo("AZN");
        assertThat(dto.getDate()).isEqualTo("2020-09-19 00:00:00");
        assertThat(dto.getDescription()).isEqualTo("Account Credit RBS Balance loader");
        assertThat(dto.getMcc()).isNull();
        assertThat(dto.getTrnTypeId()).isEqualTo("146");
        assertThat(dto.getTrnTypeCode()).isEqualTo("I2");
        assertThat(dto.getRrn()).isNull();
        assertThat(dto.getStatus()).isEqualTo("Successfully completed");
        assertThat(dto.getLocalAmount()).isEqualTo(BigDecimal.valueOf(22500, 2));
        assertThat(dto.getTrnType()).isEqualTo("C");
        assertThat(dto.getDescriptionExt()).isEqualTo("RBS Balance loader");

        AccountAmountDto accountAmountDto = dto.getPostingDetailsDto().getAccountAmountDto();

        assertThat(accountAmountDto.getAmount()).isEqualTo(BigDecimal.valueOf(22500, 2));
        assertThat(accountAmountDto.getType()).isEqualTo("Full");
        assertThat(accountAmountDto.getCurrency()).isEqualTo("AZN");

        assertThat(dto.getPostingDetailsDto().getProcessingStatus()).isEqualTo("Posted");

        BillingDto billingDto = dto.getBillingDto();
        assertThat(billingDto.getPhaseDate()).isEqualTo("2020-09-21");
        assertThat(billingDto.getCurrency()).isEqualTo("AZN");
        assertThat(billingDto.getAmount()).isEqualTo(BigDecimal.valueOf(22500, 2));
        assertThat(billingDto.getExtras()).isEmpty();
    }

    @Test
    public void shouldHandleTransactionAmount_WhenBothConditionsProvided() {

        DocData docData = stmtItem.getDocData();
        docData.setTransaction(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getAmount()).isEqualTo("0.00");

        docData.setTransaction(Transaction.builder().amount("12.00").build());
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(1200, 2));
    }

    @Test
    public void shouldHandleMcc_WhenBothConditionsProvided() {

        DocData docData = stmtItem.getDocData();
        docData.setSourceDtls(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getMcc()).isNull();

        docData.setSourceDtls(SourceDtls.builder().sic("sic").build());
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getMcc()).isEqualTo("sic");
    }

    @Test
    public void shouldHandleRnn_WhenBothConditionsProvided() {

        DocData docData = stmtItem.getDocData();
        docData.setParms(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getRrn()).isNull();

        docData.setParms(new ArrayList<>());
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getRrn()).isNull();

        List<Parm> parms = new ArrayList<>();
        parms.add(Parm.builder().parmCode("RRN").value("rnnVal").build());
        docData.setParms(parms);
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getRrn()).isEqualTo("rnnVal");
    }

    @Test
    public void shouldHandleAccountAmount_WhenBothConditionsProvided() {

        stmtItem.getPostingDetails().setAccountAmount(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getLocalAmount()).isNull();

        stmtItem.getPostingDetails().setAccountAmount(AccountAmount.builder().amount("12.00").build());
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getLocalAmount()).isEqualTo(BigDecimal.valueOf(1200, 2));
    }

    @Test
    public void shouldHandleTrnType_WhenBothConditionsProvided() {

        stmtItem.getDocData().setTransaction(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        stmtItem.getDocData().setTransaction(Transaction.builder().build());
        Transaction transaction = stmtItem.getDocData().getTransaction();
        transaction.setAmount(null);
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        transaction.setAmount("0.00");
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        transaction.setAmount("2.00");
        stmtItem.getPostingDetails().getAccountAmount().setAmount(null);
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        stmtItem.getPostingDetails().getAccountAmount().setAmount("-1.00");
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        stmtItem.getPostingDetails().getAccountAmount().setAmount("1.00");
        stmtItem.getPostingDetails().getAccountAmount().setType("Blocked");
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("D");

        stmtItem.getPostingDetails().getAccountAmount().setType("");
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getTrnType()).isEqualTo("C");
    }

    @Test
    public void shouldHandlePostingDetails_WhenBothConditionsProvided() {

        stmtItem.getPostingDetails().getAccountAmount().setType(null);
        stmtItem.getPostingDetails().getAccountAmount().setCurrency(null);
        stmtItem.getPostingDetails().getAccountAmount().setAmount(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto()).isNull();

        stmtItem.getPostingDetails().setAccountAmount(null);
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto()).isNull();

        stmtItem
                .getPostingDetails()
                .setAccountAmount(
                        AccountAmount.builder().type("type").currency("cur").amount("2.00").build());
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto()).isNotNull();
        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto().getType())
                .isEqualTo("type");
        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto().getCurrency())
                .isEqualTo("cur");
        assertThat(dtos.get(0).getPostingDetailsDto().getAccountAmountDto().getAmount())
                .isEqualTo(BigDecimal.valueOf(200, 2));
    }

    @Test
    public void shouldHandleBillingExtras_WhenBothConditionsProvided() {

        stmtItem.getDocData().getBilling().setExtras(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getBillingDto().getExtras()).isEmpty();

        List<Extra> extras = new ArrayList<>();
        extras.add(
                Extra.builder().amount("1.00").currency("AZN").details("details").type("type").build());
        stmtItem.getDocData().getBilling().setExtras(extras);
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getBillingDto().getExtras().size()).isEqualTo(1);

        ExtraDto extraDto = dtos.get(0).getBillingDto().getExtras().get(0);
        assertThat(extraDto.getAmount()).isEqualTo(BigDecimal.valueOf(100, 2));
        assertThat(extraDto.getCurrency()).isEqualTo("AZN");
        assertThat(extraDto.getDetails()).isEqualTo("details");
        assertThat(extraDto.getType()).isEqualTo("type");
    }

    @Test
    public void shouldHandleAmount_WhenBothConditionsProvided() {

        Transaction transaction = stmtItem.getDocData().getTransaction();
        transaction.setAmount(null);
        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getAmount()).isNull();

        transaction.setAmount("12.00");
        dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(1200, 2));
    }

    @Test
    public void shouldHandleNullStmtItems() throws JsonProcessingException {
        transactionResponse = buildTransactionResponse();
        transactionResponse
                .getMsgData()
                .getDoc()
                .getDataRs()
                .getStmt()
                .getAdditionalStmt()
                .setStmtItems(null);

        List<TransactionDto> dtos = mapper.map(transactionResponse, 100, 1);

        assertThat(dtos).isEmpty();
    }
}
