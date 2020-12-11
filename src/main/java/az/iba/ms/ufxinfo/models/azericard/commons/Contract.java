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
public class Contract {

    @JacksonXmlProperty(localName = "ClientType")
    private String clientType;

    @JacksonXmlProperty(localName = "ClientCategory")
    private String clientCategory;

    @JacksonXmlProperty(localName = "ContractIDT")
    private ContractIdt contractIdt;

    @JacksonXmlProperty(localName = "MainContract")
    private MainContract mainContract;

    @JacksonXmlProperty(localName = "Currency")
    private String currency;

    @JacksonXmlProperty(localName = "ContractName")
    private String contractName;

    @JacksonXmlProperty(localName = "Product")
    private Product product;

    @JacksonXmlProperty(localName = "ProductionParms")
    private ProductionParms productionParms;

    @JacksonXmlProperty(localName = "PlasticInfo")
    private PlasticInfo plasticInfo;

    @JacksonXmlProperty(localName = "DateOpen")
    private String dateOpen;

    @JacksonXmlProperty(localName = "AddContractInfo")
    private AddContractInfo addContractInfo;
}
