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
public class Info {

    @JacksonXmlProperty(localName = "Status")
    private Status status;

    @JacksonXmlElementWrapper(localName = "Balances")
    @JacksonXmlProperty(localName = "Balance")
    private List<Balance> balances;

    @JacksonXmlElementWrapper(localName = "SubContracts")
    @JacksonXmlProperty(localName = "SubContract")
    private List<SubContract> subContracts;
}
