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
public class Source {

    @JacksonXmlProperty(isAttribute = true)
    private String app;

    @JacksonXmlProperty(localName = "ContractNumber")
    private String contractNumber;

    @JacksonXmlProperty(localName = "InstInfo")
    private InstInfo instInfo;
}
