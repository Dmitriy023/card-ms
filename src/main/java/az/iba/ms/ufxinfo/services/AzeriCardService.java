package az.iba.ms.ufxinfo.services;

import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AzeriCardService {

    UfxMsg getBalanceInfo(String cardNumber) throws JsonProcessingException;

    UfxMsg getTransactions(String cardNumber, String from, String to, int pageSize, int page)
            throws JsonProcessingException;
}
