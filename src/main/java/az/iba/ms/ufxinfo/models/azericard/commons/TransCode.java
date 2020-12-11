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
public class TransCode {

    @JacksonXmlProperty(localName = "MsgCode")
    private String msgCode;

    @JacksonXmlProperty(localName = "TransTypeId")
    private String transTypeId;

    @JacksonXmlProperty(localName = "TransTypeCode")
    private String transTypeCode;
}
