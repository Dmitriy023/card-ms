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
public class AdditionalStmt {

    @JacksonXmlProperty(localName = "BeginBalance")
    private BeginBalance beginBalance;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "StmtItem")
    private List<StmtItem> stmtItems;
}
