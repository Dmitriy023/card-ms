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
public class DataRs {

    @JacksonXmlProperty(localName = "ContractRs")
    private ContractRs contractRs;

    @JacksonXmlProperty(localName = "Stmt")
    private Stmt stmt;
}
