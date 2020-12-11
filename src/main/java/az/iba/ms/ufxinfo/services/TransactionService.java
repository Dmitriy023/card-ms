package az.iba.ms.ufxinfo.services;

import az.iba.ms.ufxinfo.dtos.TransactionDto;
import java.util.List;

public interface TransactionService {

    List<TransactionDto> getTransactions(
            String cardNumber, String from, String to, int pageSize, int page);

}
