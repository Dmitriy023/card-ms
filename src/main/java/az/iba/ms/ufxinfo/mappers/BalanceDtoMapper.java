package az.iba.ms.ufxinfo.mappers;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.models.azericard.commons.Balance;
import az.iba.ms.ufxinfo.models.azericard.commons.Contract;
import az.iba.ms.ufxinfo.models.azericard.commons.ContractRs;
import az.iba.ms.ufxinfo.models.azericard.commons.ExtraRsContent;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceDtoMapper {

    @Autowired
    private ResponseParser responseParser;

    public BalanceDto map(UfxMsg balanceResponse) {

        BalanceDto balanceDto = new BalanceDto();

        ContractRs contractRs =
                balanceResponse.getMsgData().getInformation().getDataRs().getContractRs();

        Contract contract = contractRs.getContract();
        if (contract.getMainContract() != null
                && StringUtils.isNotBlank(
                contract.getMainContract().getContractIdt().getContractNumber())) {
            balanceDto.setContractNumber(
                    "N" + contract.getMainContract().getContractIdt().getContractNumber());
        }
        balanceDto.setCurrency(contract.getCurrency());
        balanceDto.setRegNumber(contract.getContractIdt().getClient().getClientInfo().getRegNumber());
        balanceDto.setCardStatusClass(contractRs.getInfo().getStatus().getStatusClass());
        balanceDto.setCardStatusCode(contractRs.getInfo().getStatus().getStatusCode());
        balanceDto.setCardStatus(contractRs.getInfo().getStatus().getStatusDetails());
        balanceDto.setCardBin(contract.getContractIdt().getContractNumber().substring(0, 6));
        balanceDto.setExtraInfo(contract.getAddContractInfo().getExtraRs());

        ExtraRsContent extraRsContent = responseParser.parseContent(balanceDto.getExtraInfo());
        balanceDto.setCardTypeCode(extraRsContent.getPlasticType());

        setBalanceType(balanceDto, contractRs.getInfo().getBalances());

        return balanceDto;
    }

    private void setBalanceType(BalanceDto balanceDto, List<Balance> balances) {
        for (Balance b : balances) {
            switch (b.getType()) {
                case "AVAILABLE":
                    balanceDto.setAvailableBalance(new BigDecimal(b.getAmount()));
                    break;
                case "BLOCKED":
                    balanceDto.setBlockedAmount(new BigDecimal(b.getAmount()));
                    break;
                case "CR_LIMIT":
                    balanceDto.setCreditLimit(new BigDecimal(b.getAmount()));
                    break;
                case "B1":
                    balanceDto.setBonus(new BigDecimal(b.getAmount()));
                    break;
                default:
                    break;
            }
        }
    }
}
