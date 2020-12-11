package az.iba.ms.ufxinfo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostingDetailsDto {

    private String processingStatus;

    @JsonProperty("accountAmount")
    private AccountAmountDto accountAmountDto;
}
