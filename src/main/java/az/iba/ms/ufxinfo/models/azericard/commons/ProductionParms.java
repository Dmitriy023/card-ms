package az.iba.ms.ufxinfo.models.azericard.commons;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionParms {

    @JacksonXmlProperty(localName = "CardExpiry")
    private String cardExpiry;

    @JacksonXmlProperty(localName = "SequenceNumber")
    private String sequenceNumber;
}
