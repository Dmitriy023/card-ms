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
public class StmtItem {

    @JacksonXmlProperty(localName = "DocData")
    private DocData docData;

    @JacksonXmlProperty(localName = "PostingDetails")
    private PostingDetails postingDetails;
}
