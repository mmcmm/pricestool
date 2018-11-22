package com.pricestool.pricestool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.service.dto.opdto.*;
import java.util.*;


@Service
@Transactional
public class OpskinsService {

    // @Value("${OP_API_KEY}")
    // private String OP_API_KEY;

    private final Logger log = LoggerFactory.getLogger(OpskinsService.class);

    final String OP_ITEMS_API_URL = "https://api-trade.opskins.com/IItem/GetItems/v1/?key=";
   
    final String OP_PRICES_API_URL = "https://api.opskins.com/IPricing/GetSuggestedPrices/v2/?appid=1912";


    private final VgoitemService vgoItemService;


    public OpskinsService(VgoitemService vgoItemService) {
        this.vgoItemService = vgoItemService;
    }

    @Async
    @Scheduled(cron = "0 */2 * * * *") // 5
    public void updateItems() {
        log.debug("Run scheduled opskins  update items {}");

        // get all normal items
        Map<String, Price> prices = callEndpoint(OP_PRICES_API_URL).getResponse();
        for (String name : prices.keySet()) {
            Vgoitem vgoitem = new Vgoitem(); 
            vgoitem.setName(name);
            vgoitem.setOp7day(prices.get(name).get_op_7_day());        
            vgoitem.setOp7day(prices.get(name).get_op_30_day());
            // vgoItemService.save(vgoitem);        
        } 
    }

    private OpskinsDTO callEndpoint(String endpoint) {

        RestTemplate restTemplate = restTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
        HttpEntity<String> entityOP = new HttpEntity<>("parameters", headers);
        ResponseEntity<OpskinsDTO> respEntityOP;
        OpskinsDTO opresp = null;

        try {
            respEntityOP = restTemplate.exchange(endpoint, HttpMethod.GET, entityOP, OpskinsDTO.class);
            opresp = respEntityOP.getBody();
        } catch (Exception ex) {
            log.error("Failed to fetch OPSkins data: " + ex.getMessage());
        }
        return opresp;

    }

    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                try {
                    ((MappingJackson2HttpMessageConverter) converter)
                        .setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
                } catch (Exception ex) {
                    log.error("Failed to add text converter: " + ex.getMessage());
                }
            }
        }
        return template;
    }
}
