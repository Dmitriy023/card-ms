package az.iba.ms.ufxinfo.services.impl;

import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.mappers.TransactionDtoMapper;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.services.AzeriCardService;
import az.iba.ms.ufxinfo.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private AzeriCardService azeriCardService;

    @Autowired
    private TransactionDtoMapper transactionDtoMapper;

    @Override
    public List<TransactionDto> getTransactions(String cardNumber, String from, String to, int pageSize, int page) {
        try {
            UfxMsg transactionResponse = azeriCardService.getTransactions(cardNumber, from, to, pageSize, page);

            if (!transactionResponse.getRespCode().equals("0")) {
                LOGGER.error("AzeriCard error " + transactionResponse.getRespText());
                return new ArrayList<>();
            }

            return transactionDtoMapper.map(transactionResponse, pageSize, page);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
