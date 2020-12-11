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
public class InstInfo {

    @JacksonXmlProperty(localName = "InstitutionIdType")
    private String institutionIdType;

    @JacksonXmlProperty(localName = "Institution")
    private String institution;
}
