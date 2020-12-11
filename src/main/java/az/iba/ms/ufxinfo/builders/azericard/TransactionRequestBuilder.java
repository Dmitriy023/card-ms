package az.iba.ms.ufxinfo.builders.azericard;

import az.iba.ms.ufxinfo.models.azericard.commons.Doc;
import az.iba.ms.ufxinfo.models.azericard.commons.DocRefSet;
import az.iba.ms.ufxinfo.models.azericard.commons.InstInfo;
import az.iba.ms.ufxinfo.models.azericard.commons.MsgData;
import az.iba.ms.ufxinfo.models.azericard.commons.Parm;
import az.iba.ms.ufxinfo.models.azericard.commons.Requestor;
import az.iba.ms.ufxinfo.models.azericard.commons.Source;
import az.iba.ms.ufxinfo.models.azericard.commons.TransCode;
import az.iba.ms.ufxinfo.models.azericard.commons.TransRule;
import az.iba.ms.ufxinfo.models.azericard.commons.TransType;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestBuilder {

    public UfxMsg build(String contractNumber, String from, String to, int count) {

        List<TransRule> transRules = new ArrayList<>();
        transRules.add(TransRule.builder().parmCode("SkipAuth").value("Y").build());

        TransType transType = TransType.builder()
                .transCode(TransCode.builder().msgCode("01000UFX").build()).transRules(transRules).build();

        InstInfo instInfo = InstInfo.builder().institutionIdType("BIN").institution("0010").build();
        Source source = Source.builder().contractNumber("90100001").instInfo(instInfo).build();

        List<Parm> resultDtls = getResultDlts(from, to, count);

        DocRefSet docRefSet =
                DocRefSet.builder().parm(Parm.builder().parmCode("RRN").value("").build()).build();

        Requestor requestor = Requestor.builder().contractNumber(contractNumber).build();

        Doc doc = Doc.builder()
                .transType(transType)
                .requestor(requestor)
                .source(source)
                .resultDtls(resultDtls)
                .docRefSet(docRefSet)
                .build();

        MsgData msgData = MsgData.builder().doc(doc).build();

        Source source2 = Source.builder().app("IBAR").build();

        return UfxMsg.builder()
                .msgType("Doc")
                .scheme("WAY4Doc")
                .direction("Rq")
                .version("2.0")
                .msgId(UUID.randomUUID().toString())
                .source(source2)
                .msgData(msgData)
                .build();
    }

    private List<Parm> getResultDlts(String from, String to, int count) {
        List<Parm> resultDtls = new ArrayList<>();
        resultDtls.add(Parm.builder().parmCode("StmtType").value("Additional").build());
        resultDtls.add(Parm.builder().parmCode("StmtContType").value("All").build());
        resultDtls.add(Parm.builder().parmCode("Balance").value("AVAILABLE").build());
        resultDtls.add(Parm.builder().parmCode("DateFrom").value(from).build());
        resultDtls.add(Parm.builder().parmCode("DateTo").value(to).build());
        resultDtls.add(Parm.builder().parmCode("StmtMaxRecords").value(count + "").build());
        resultDtls.add(Parm.builder().parmCode("OrderingRule").value("StmtDesc").build());
        return resultDtls;
    }
}
