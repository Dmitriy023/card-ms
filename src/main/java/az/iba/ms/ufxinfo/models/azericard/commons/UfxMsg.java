package az.iba.ms.ufxinfo.models.azericard.commons;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "UFXMsg")
public class UfxMsg {

    @JacksonXmlProperty(isAttribute = true)
    private String scheme;

    @JacksonXmlProperty(isAttribute = true, localName = "msg_type")
    private String msgType;

    @JacksonXmlProperty(isAttribute = true)
    private String direction;

    @JacksonXmlProperty(isAttribute = true)
    private String version;

    @JacksonXmlProperty(isAttribute = true, localName = "resp_class")
    private String respClass;

    @JacksonXmlProperty(isAttribute = true, localName = "resp_code")
    private String respCode;

    @JacksonXmlProperty(isAttribute = true, localName = "resp_text")
    private String respText;

    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;

    @JacksonXmlProperty(localName = "Source")
    private Source source;

    @JacksonXmlProperty(localName = "MsgData")
    private MsgData msgData;
}
