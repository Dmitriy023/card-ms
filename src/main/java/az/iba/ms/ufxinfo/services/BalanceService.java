package az.iba.ms.ufxinfo.services;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import java.util.List;

public interface BalanceService {

    List<BalanceDto> getBalance(String cardNumbers);

}
