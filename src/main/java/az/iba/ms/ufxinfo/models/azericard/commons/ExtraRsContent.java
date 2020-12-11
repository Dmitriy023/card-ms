package az.iba.ms.ufxinfo.models.azericard.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraRsContent {

    private String clientId;

    private String contractType;

    private String extraCard;

    private String cardHolderName;

    private String creditType;

    private String totalDebt;

    private String plasticType;
}
