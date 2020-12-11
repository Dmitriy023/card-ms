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
public class Doc {

    @JacksonXmlProperty(localName = "TransType")
    private TransType transType;

    @JacksonXmlProperty(localName = "DocRefSet")
    private DocRefSet docRefSet;

    @JacksonXmlProperty(localName = "Requestor")
    private Requestor requestor;

    @JacksonXmlElementWrapper(localName = "ResultDtls")
    @JacksonXmlProperty(localName = "Parm")
    private List<Parm> resultDtls;

    @JacksonXmlProperty(localName = "DataRs")
    private DataRs dataRs;

    @JacksonXmlProperty(localName = "Source")
    private Source source;
}
