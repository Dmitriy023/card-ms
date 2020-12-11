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
public class PostingDetails {

    @JacksonXmlProperty(localName = "Status")
    private Status status;

    @JacksonXmlProperty(localName = "AccountAmount")
    private AccountAmount accountAmount;

    @JacksonXmlProperty(localName = "ProcessingStatus")
    private String processingStatus;
}
