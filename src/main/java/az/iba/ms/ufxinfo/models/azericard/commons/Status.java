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
public class Status {

    @JacksonXmlProperty(localName = "StatusClass")
    private String statusClass;

    @JacksonXmlProperty(localName = "StatusCode")
    private String statusCode;

    @JacksonXmlProperty(localName = "StatusDetails")
    private String statusDetails;

    @JacksonXmlProperty(localName = "ProductionStatus")
    private String productionStatus;

    @JacksonXmlProperty(localName = "RespClass")
    private String respClass;

    @JacksonXmlProperty(localName = "RespCode")
    private String respCode;

    @JacksonXmlProperty(localName = "RespText")
    private String respText;
}
