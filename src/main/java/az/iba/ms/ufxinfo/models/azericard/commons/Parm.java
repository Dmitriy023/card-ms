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
public class Parm {

    @JacksonXmlProperty(localName = "ParmCode")
    private String parmCode;

    @JacksonXmlProperty(localName = "Value")
    private String value;
}
