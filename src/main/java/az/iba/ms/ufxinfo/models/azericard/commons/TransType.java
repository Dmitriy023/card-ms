package az.iba.ms.ufxinfo.models.azericard.commons;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransType {

    @JacksonXmlProperty(localName = "TransCode")
    private TransCode transCode;

    @JacksonXmlElementWrapper(localName = "TransRules")
    @JacksonXmlProperty(localName = "TransRule")
    private List<TransRule> transRules;
}
