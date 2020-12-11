package az.iba.ms.ufxinfo.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import az.iba.ms.ufxinfo.models.azericard.commons.Balance;
import az.iba.ms.ufxinfo.models.azericard.commons.Client;
import az.iba.ms.ufxinfo.models.azericard.commons.ClientInfo;
import az.iba.ms.ufxinfo.models.azericard.commons.Contract;
import az.iba.ms.ufxinfo.models.azericard.commons.ContractIdt;
import az.iba.ms.ufxinfo.models.azericard.commons.ExtraRsContent;
import az.iba.ms.ufxinfo.models.azericard.commons.Info;
import az.iba.ms.ufxinfo.models.azericard.commons.Information;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.PlasticInfo;
import az.iba.ms.ufxinfo.models.azericard.commons.ProductionParms;
import az.iba.ms.ufxinfo.models.azericard.commons.Status;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import az.iba.ms.ufxinfo.utils.ResourceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseParserTest {

    @Autowired
    private ResponseParser responseParser;

    @Test
    public void shouldParse_whenBalanceResponseXmlProvided() throws Exception {
        String xmlResponse = ResourceReader.readFileToString("classpath:Balance.rs.xml");

        UfxMsg ufxMsg = responseParser.parse(xmlResponse);

        checkHeader(ufxMsg);

        Information information = ufxMsg.getMsgData().getInformation();
        checkInformation(information);

        List<Parm> resultDtls = information.getResultDtls();
        checkResultDtls(resultDtls);

        Contract contract = information.getDataRs().getContractRs().getContract();
        checkContract(contract);

        Info info = information.getDataRs().getContractRs().getInfo();
        checkInfo(info);

        Status status = information.getStatus();
        assertThat(status.getRespClass()).isEqualTo("Information");
        assertThat(status.getRespCode()).isEqualTo("0");
        assertThat(status.getRespText()).isEqualTo("Successfully processed");
    }

    private void checkInfo(Info info) {
        Status status = info.getStatus();
        assertThat(status.getStatusClass()).isEqualTo("Valid");
        assertThat(status.getStatusCode()).isEqualTo("00");
        assertThat(status.getStatusDetails()).isEqualTo("Card OK");
        assertThat(status.getProductionStatus()).isEqualTo("Ready");

        List<Balance> balances = info.getBalances();
        checkBalances(balances);
    }

    private void checkContract(Contract contract) {
        assertThat(contract.getClientType()).isEqualTo("PR");
        assertThat(contract.getClientCategory()).isEqualTo("Private");

        ContractIdt contractIdt = contract.getContractIdt();
        assertThat(contractIdt.getContractNumber()).isEqualTo("4444444444444444");
        assertThat(contractIdt.getRbsNumber()).isEqualTo("33888889443333333333");

        Client client = contractIdt.getClient();
        checkClient(client);

        assertThat(contract.getCurrency()).isEqualTo("AZN");
        assertThat(contract.getContractName()).isEqualTo("Lastname Name");

        assertThat(contract.getProduct().getAddInfo().getParm().getParmCode())
                .isEqualTo("ContractCategory");
        assertThat(contract.getProduct().getAddInfo().getParm().getValue()).isEqualTo("Card");

        ProductionParms productionParms = contract.getProductionParms();
        assertThat(productionParms.getCardExpiry()).isEqualTo("1909");
        assertThat(productionParms.getSequenceNumber()).isEqualTo("1");

        PlasticInfo plasticInfo = contract.getPlasticInfo();
        assertThat(plasticInfo.getFirstName()).isEqualTo("Name");
        assertThat(plasticInfo.getLastName()).isEqualTo("Lastname");
        assertThat(plasticInfo.getCompanyName()).isEqualTo("Company");

        assertThat(contract.getDateOpen()).isEqualTo("2012-09-28");
        assertThat(contract.getAddContractInfo().getExtraRs())
                .isEqualTo("CLIENT_ID=9999999;CREDIT_TYPE=D;PLASTIC_TYPE=0160SVED;");
    }

    private void checkHeader(UfxMsg ufxMsg) {
        assertThat(ufxMsg.getScheme()).isEqualTo("WAY4Appl");
        assertThat(ufxMsg.getMsgType()).isEqualTo("Information");
        assertThat(ufxMsg.getDirection()).isEqualTo("Rs");
        assertThat(ufxMsg.getVersion()).isEqualTo("2.0");
        assertThat(ufxMsg.getRespClass()).isEqualTo("I");
        assertThat(ufxMsg.getRespCode()).isEqualTo("0");
        assertThat(ufxMsg.getRespText()).isEqualTo("Successfully processed");
        assertThat(ufxMsg.getMsgId()).isEqualTo("AAA-555-333-EEE-2312414123456789");
        assertThat(ufxMsg.getSource().getApp()).isEqualTo("ZIRAATBANK");
    }

    private void checkInformation(Information information) {
        assertThat(information.getRegNumber()).isNull();
        assertThat(information.getInstitution()).isEqualTo("0160");
        assertThat(information.getOrderDprt()).isEqualTo("0160UPL");
        assertThat(information.getObjectType()).isEqualTo("Contract");
        assertThat(information.getActionType()).isEqualTo("Inquiry");
        assertThat(information.getObjectFor().getContractIdt().getContractNumber())
                .isEqualTo("4444444444444444");
    }

    private void checkResultDtls(List<Parm> resultDtls) {
        assertThat(resultDtls.get(0).getParmCode()).isEqualTo("Balance");
        assertThat(resultDtls.get(0).getValue()).isEqualTo("BLOCKED");

        assertThat(resultDtls.get(1).getParmCode()).isEqualTo("Status");
        assertThat(resultDtls.get(1).getValue()).isEqualTo("Y");

        assertThat(resultDtls.get(2).getParmCode()).isEqualTo("Client");
        assertThat(resultDtls.get(2).getValue()).isEqualTo("Y");

        assertThat(resultDtls.get(3).getParmCode()).isEqualTo("OrderingEnabled");
        assertThat(resultDtls.get(3).getValue()).isEqualTo("Y");
    }

    private void checkBalances(List<Balance> balances) {
        assertThat(balances.get(0).getName()).isEqualTo("BLOCKED");
        assertThat(balances.get(0).getType()).isEqualTo("BLOCKED");
        assertThat(balances.get(0).getAmount()).isEqualTo("0.00");
        assertThat(balances.get(0).getCurrency()).isEqualTo("AZN");

        assertThat(balances.get(1).getName()).isEqualTo("AVAILABLE");
        assertThat(balances.get(1).getType()).isEqualTo("AVAILABLE");
        assertThat(balances.get(1).getAmount()).isEqualTo("99999.99");
        assertThat(balances.get(1).getCurrency()).isEqualTo("AZN");

        assertThat(balances.get(2).getName()).isEqualTo("CR_LIMIT");
        assertThat(balances.get(2).getType()).isEqualTo("CR_LIMIT");
        assertThat(balances.get(2).getAmount()).isEqualTo("12.00");
        assertThat(balances.get(2).getCurrency()).isEqualTo("AZN");

        assertThat(balances.get(3).getName()).isEqualTo("B1");
        assertThat(balances.get(3).getType()).isEqualTo("B1");
        assertThat(balances.get(3).getAmount()).isEqualTo("13.00");
        assertThat(balances.get(3).getCurrency()).isEqualTo("AZN");
    }

    private void checkClient(Client client) {
        assertThat(client.getOrderDprt()).isEqualTo("0160UPL");
        assertThat(client.getClientType()).isEqualTo("PR");
        assertThat(client.getDateOpen()).isEqualTo("2012-09-27");

        ClientInfo clientInfo = client.getClientInfo();
        assertThat(clientInfo.getClientNumber()).isEqualTo("AZE 77777777");
        assertThat(clientInfo.getRegNumber()).isEqualTo("CB12/10800");
        assertThat(clientInfo.getShortName()).isEqualTo("Lastname Name");
        assertThat(clientInfo.getFirstName()).isEqualTo("Name");
        assertThat(clientInfo.getLastName()).isEqualTo("Lastname");
        assertThat(clientInfo.getSecurityName()).isEqualTo("Info");

        PlasticInfo plasticInfo = client.getPlasticInfo();
        assertThat(plasticInfo.getFirstName()).isEqualTo("Name");
        assertThat(plasticInfo.getLastName()).isEqualTo("Lastname");
        assertThat(plasticInfo.getCompanyName()).isEqualTo("Company");

        assertThat(client.getBaseAddress().getAddressLine1()).isEqualTo("Company");
    }

    @Test
    public void shouldParse_whenExtraRsProvided() {
        String extraRs = "CLIENT_ID=9999999;CREDIT_TYPE=D;PLASTIC_TYPE=0160SVED;";

        ExtraRsContent extraRsContent = responseParser.parseContent(extraRs);

        assertThat(extraRsContent.getPlasticType()).isEqualTo("0160SVED");
    }

    @Test
    public void shouldParse_whenTransactionRequestXmlProvided() throws JsonProcessingException {
        String xmlResponse = ResourceReader.readFileToString("classpath:Transaction.rq.xml");

        UfxMsg ufxMsg = responseParser.parse(xmlResponse);

        assertThat(ufxMsg.getMsgData().getDoc().getRequestor().getContractNumber())
                .isEqualTo("4127208121070931");
    }

    @Test
    public void shouldParse_whenTransactionResponseXmlProvided() throws JsonProcessingException {
        String xmlResponse = ResourceReader.readFileToString("classpath:Transaction.rs.xml");

        UfxMsg ufxMsg = responseParser.parse(xmlResponse);

        assertThat(ufxMsg.getMsgData().getDoc().getRequestor().getContractNumber())
                .isEqualTo("4127208121070931");
    }
}
