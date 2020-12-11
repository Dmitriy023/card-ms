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
public class Client {

    @JacksonXmlProperty(localName = "OrderDprt")
    private String orderDprt;

    @JacksonXmlProperty(localName = "ClientType")
    private String clientType;

    @JacksonXmlProperty(localName = "ClientCategory")
    private String clientCategory;

    @JacksonXmlProperty(localName = "ClientInfo")
    private ClientInfo clientInfo;

    @JacksonXmlProperty(localName = "PlasticInfo")
    private PlasticInfo plasticInfo;

    @JacksonXmlProperty(localName = "DateOpen")
    private String dateOpen;

    @JacksonXmlProperty(localName = "BaseAddress")
    private BaseAddress baseAddress;

    @JacksonXmlElementWrapper(localName = "PhoneList")
    @JacksonXmlProperty(localName = "Phone")
    private List<Phone> phones;
}
