package az.iba.ms.ufxinfo.parsers.azericard;

import az.iba.ms.ufxinfo.models.azericard.commons.ExtraRsContent;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ResponseParser {

    public UfxMsg parse(String xmlResponse) throws JsonProcessingException {

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper.readValue(xmlResponse, UfxMsg.class);
    }

    public ExtraRsContent parseContent(String content) {

        ExtraRsContent extraRsContent = new ExtraRsContent();
        if (StringUtils.isNotBlank(content)) {
            String[] parts = content.split(";");
            for (String p : parts) {
                String[] attr = p.split("=");
                if (attr[0].equals("PLASTIC_TYPE")) {
                    extraRsContent.setPlasticType(attr[1]);
                }
            }
        }

        return extraRsContent;
    }
}
