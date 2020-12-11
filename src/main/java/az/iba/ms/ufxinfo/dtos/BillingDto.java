package az.iba.ms.ufxinfo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingDto {

    private String phaseDate;

    private String currency;

    private BigDecimal amount;

    @JsonProperty(value = "extra")
    private List<ExtraDto> extras;
}
