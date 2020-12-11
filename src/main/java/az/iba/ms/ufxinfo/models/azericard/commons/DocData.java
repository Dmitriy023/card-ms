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
public class DocData {

    @JacksonXmlElementWrapper(localName = "DocRefSet")
    @JacksonXmlProperty(localName = "Parm")
    private List<Parm> parms;

    @JacksonXmlProperty(localName = "Transaction")
    private Transaction transaction;

    @JacksonXmlProperty(localName = "Billing")
    private Billing billing;

    @JacksonXmlProperty(localName = "TransType")
    private TransType transType;

    @JacksonXmlProperty(localName = "LocalDt")
    private String localDt;

    @JacksonXmlProperty(localName = "Description")
    private String description;

    @JacksonXmlProperty(localName = "DescriptionExt")
    private String descriptionExt;

    @JacksonXmlProperty(localName = "SourceDtls")
    private SourceDtls sourceDtls;
}
