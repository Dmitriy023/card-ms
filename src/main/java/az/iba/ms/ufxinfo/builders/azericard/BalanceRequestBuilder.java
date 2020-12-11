package az.iba.ms.ufxinfo.builders.azericard;

import az.iba.ms.ufxinfo.models.azericard.commons.ContractIdt;
import az.iba.ms.ufxinfo.models.azericard.commons.Information;
import az.iba.ms.ufxinfo.models.azericard.commons.MsgData;
import az.iba.ms.ufxinfo.models.azericard.commons.ObjectFor;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.Source;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class BalanceRequestBuilder {

    public UfxMsg build(String cardNumber) {

        List<Parm> resultDtls = getResultDlts();

        ContractIdt contractIdt = ContractIdt.builder().contractNumber(cardNumber).build();

        ObjectFor objectFor = ObjectFor.builder().contractIdt(contractIdt).build();

        Information information =
                Information.builder()
                        .institution("0010")
                        .orderDprt("0101")
                        .objectType("Contract")
                        .actionType("Inquiry")
                        .regNumber("AAA-0002-545514")
                        .resultDtls(resultDtls)
                        .objectFor(objectFor)
                        .build();

        MsgData msgData = MsgData.builder().information(information).build();

        Source source = Source.builder().app("IBAR").build();

        return UfxMsg.builder()
                .msgType("Information")
                .scheme("WAY4Appl")
                .direction("Rq")
                .version("2.0")
                .source(source)
                .msgData(msgData)
                .msgId(UUID.randomUUID().toString())
                .build();
    }

    private List<Parm> getResultDlts() {
        List<Parm> resultDtls = new ArrayList<>();
        resultDtls.add(Parm.builder().parmCode("Balance").value("AVAILABLE;BLOCKED;CR_LIMIT;").build());
        resultDtls.add(Parm.builder().parmCode("Status").value("Y").build());
        resultDtls.add(Parm.builder().parmCode("Client").value("Y").build());
        resultDtls.add(Parm.builder().parmCode("Parent").value("Y").build());
        resultDtls.add(Parm.builder().parmCode("OrderingEnabled").value("Y").build());
        return resultDtls;
    }
}
