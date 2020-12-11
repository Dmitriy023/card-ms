package az.iba.ms.ufxinfo.services.impl;

import az.iba.ms.ufxinfo.builders.azericard.BalanceRequestBuilder;
import az.iba.ms.ufxinfo.builders.azericard.TransactionRequestBuilder;
import az.iba.ms.ufxinfo.handlers.RestTemplateResponseErrorHandler;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import az.iba.ms.ufxinfo.parsers.azericard.ResponseParser;
import az.iba.ms.ufxinfo.services.AzeriCardService;
import az.iba.ms.ufxinfo.utils.CardNumberConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AzeriCardServiceImpl implements AzeriCardService {

    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AzeriCardService.class);
    @Autowired
    private BalanceRequestBuilder balanceRequestBuilder;
    @Autowired
    private TransactionRequestBuilder transactionRequestBuilder;
    @Autowired
    private ResponseParser responseParser;
    @Autowired
    private CardNumberConverter cardNumberConverter;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${endpoints.azericard}")
    private String azeriCardEndpoint;

    @Bean
    public RestTemplate restTemplate(
            @Value("${restTemplate.connectTimeout}") int connectTimeout,
            @Value("${restTemplate.readTimeout}") int readTimeout) {

        RestTemplate template = new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler("azeri-card"))
                .build();

        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setReadTimeout(connectTimeout);
        rf.setConnectTimeout(readTimeout);

        template.setRequestFactory(rf);

        return template;
    }

    private UfxMsg fetchResponseFromAzeriCard(UfxMsg balanceRequest) throws JsonProcessingException {

        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String balanceRequestStr = XML_MAPPER.writeValueAsString(balanceRequest);

        LOGGER.debug("UFX request: " + balanceRequestStr);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        HttpEntity<String> request = new HttpEntity<>(balanceRequestStr, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(azeriCardEndpoint, request, String.class);

        LOGGER.debug("UFX response: " + response.getBody());

        return responseParser.parse(response.getBody());
    }

    @Override
    public UfxMsg getBalanceInfo(String cardNumber) throws JsonProcessingException {

        String convertedCardNumber = cardNumberConverter.convert(cardNumber);
        UfxMsg balanceRequest = balanceRequestBuilder.build(convertedCardNumber);

        return fetchResponseFromAzeriCard(balanceRequest);
    }

    @Override
    public UfxMsg getTransactions(String cardNumber, String from, String to, int pageSize, int page)
            throws JsonProcessingException {

        String convertedCardNumber = cardNumberConverter.convert(cardNumber);
        int count = pageSize * page;
        UfxMsg transactionRequest =
                transactionRequestBuilder.build(convertedCardNumber, from, to, count);

        return fetchResponseFromAzeriCard(transactionRequest);
    }
}
