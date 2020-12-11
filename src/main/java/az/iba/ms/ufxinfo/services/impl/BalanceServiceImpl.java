package az.iba.ms.ufxinfo.services.impl;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.mappers.BalanceDtoMapper;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.services.AzeriCardService;
import az.iba.ms.ufxinfo.services.BalanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceServiceImpl.class);

    @Autowired
    private AzeriCardService azeriCardService;

    @Autowired
    private BalanceDtoMapper balanceDtoMapper;

    @Override
    public List<BalanceDto> getBalance(String cardNumbers) {
        return Arrays.stream(cardNumbers.split(","))
                .parallel()
                .map(this::getBalanceDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BalanceDto getBalanceDto(String cardNumber) {
        try {
            UfxMsg balanceInfo = azeriCardService.getBalanceInfo(cardNumber);

            if (!balanceInfo.getRespCode().equals("0")) {
                LOGGER.error(
                        "AzeriCard error " + balanceInfo.getRespText() + " " + cardNumber);
                return null;
            }

            BalanceDto balanceDto = balanceDtoMapper.map(balanceInfo);
            balanceDto.setCardNumber(cardNumber);
            return balanceDto;
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
