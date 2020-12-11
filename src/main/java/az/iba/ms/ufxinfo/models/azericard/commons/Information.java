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
public class Information {

    @JacksonXmlProperty(localName = "Institution")
    private String institution;

    @JacksonXmlProperty(localName = "OrderDprt")
    private String orderDprt;

    @JacksonXmlProperty(localName = "ObjectType")
    private String objectType;

    @JacksonXmlProperty(localName = "ActionType")
    private String actionType;

    @JacksonXmlElementWrapper(localName = "ResultDtls")
    @JacksonXmlProperty(localName = "Parm")
    private List<Parm> resultDtls;

    @JacksonXmlProperty(localName = "ObjectFor")
    private ObjectFor objectFor;

    @JacksonXmlProperty(localName = "DataRs")
    private DataRs dataRs;

    @JacksonXmlProperty(localName = "Status")
    private Status status;

    @JacksonXmlProperty(localName = "RegNumber")
    private String regNumber;
}
