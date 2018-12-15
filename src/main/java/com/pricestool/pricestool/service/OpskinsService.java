package com.pricestool.pricestool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.pricestool.pricestool.domain.Vgoitem;
import com.pricestool.pricestool.service.dto.lowprice.LowPriceDTO;
import com.pricestool.pricestool.service.dto.lowprice.LowPriceItem;
import com.pricestool.pricestool.service.dto.opdto.*;
import com.pricestool.pricestool.service.dto.sales.SalesDTO;
import com.pricestool.pricestool.service.dto.sales.SalesItem;

import java.util.*;

@Service
@Transactional
public class OpskinsService {

    // @Value("${OP_API_KEY}")
    // private String OP_API_KEY;

    private final Logger log = LoggerFactory.getLogger(OpskinsService.class);

    final String OP_ITEMS_API_URL = "https://api-trade.opskins.com/IItem/GetItems/v1/?key=";

    final String OP_PRICES_API_URL = "https://api.opskins.com/IPricing/GetSuggestedPrices/v2/?appid=1912";

    final String OP_LOW_PRICES_API_URL = "https://api.opskins.com/IPricing/GetAllLowestListPrices/v1?appid=1912";

    final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36";

    private final VgoitemService vgoItemService;

    public OpskinsService(VgoitemService vgoItemService) {
        this.vgoItemService = vgoItemService;
    }

    @Async
    @Scheduled(cron = "0 0 */8 * * *") // 0 0 */8 * * *
    public void updateItems() {
        log.debug("Run scheduled opskins  update items {}");
        try {
            // get all normal items
            Map<String, Price> prices = callEndpoint(OP_PRICES_API_URL).getResponse();
            vgoItemService.deleteAll();
            for (String name : prices.keySet()) {

                Vgoitem vgoitem = new Vgoitem();
                Price item = prices.get(name);
                vgoitem.setName(name);
                vgoitem.setOp7day(item.getOp7Day());
                vgoitem.setOp30day(item.getOp30Day());

                // get sales number
                SalesDTO sales = opSaleData(name);
                if (sales != null) {
                    SalesItem[] salesArr = sales.getResponse();
                    if (salesArr != null) {
                        int saleNr = 0;
                        long currTimestamp = System.currentTimeMillis() / 1000;
                        long past7days = currTimestamp - 24 * 60 * 60 * 7;
                        for (SalesItem saleItem : salesArr) {
                            if (saleItem.getTimestamp() < past7days) {
                                break;
                            }
                            saleNr++;
                        }
                        vgoitem.setSales(saleNr);
                    } else {
                        vgoitem.setSales(0);
                    }
                }

                vgoItemService.save(vgoitem);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.debug("Failed to save: " + e.getMessage());
        }
    }

    @Async
    @Scheduled(cron = "0 */19 * * * *") // 19
    public void updateItemLowPrices() {
        log.debug("Run scheduled opskins lowest prices {}");

        // get all normal items
        try {
            Map<String, LowPriceItem> minPrices = opPriceData().getResponse();
            List<Vgoitem> exisistingItems = vgoItemService.findAll();
            for (Vgoitem vgoitem : exisistingItems) {
                LowPriceItem item = minPrices.get(vgoitem.getName());
                if (item != null) {
                    vgoitem.setQty(item.getQuantity());
                    vgoitem.setMinPrice(item.getPrice());
                    vgoItemService.save(vgoitem);
                }
            }
        } catch (Exception e) {
            log.debug("Failed to save low price: " + e.getMessage());
        }
    }

    private OpskinsDTO callEndpoint(String endpoint) {

        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        headers.set("User-Agent", USER_AGENT);

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

    private LowPriceDTO opPriceData() {

        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        headers.set("User-Agent", USER_AGENT);

        HttpEntity<String> entityOP = new HttpEntity<>("parameters", headers);
        ResponseEntity<LowPriceDTO> respEntityOP;
        LowPriceDTO opresp = null;
        try {
            respEntityOP = restTemplate.exchange(OP_LOW_PRICES_API_URL, HttpMethod.GET, entityOP, LowPriceDTO.class);
            opresp = respEntityOP.getBody();
        } catch (Exception ex) {
            log.error("Failed to fetch OPSkins data", ex.getMessage());
        }
        return opresp;
    }

    private SalesDTO opSaleData(String name) {
        try {
            final String endpoint = "https://api.opskins.com/ISales/GetLastSales/v1/?appid=1912&market_name=" + name
                    + "&contextid=1&key=afc99418b41514a559d55200099a12";

            RestTemplate restTemplate = restTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
            headers.set("User-Agent", USER_AGENT);

            HttpEntity<String> entityOP = new HttpEntity<>("parameters", headers);
            ResponseEntity<SalesDTO> respEntityOP;
            SalesDTO opresp = null;

            respEntityOP = restTemplate.exchange(endpoint, HttpMethod.GET, entityOP, SalesDTO.class);
            opresp = respEntityOP.getBody();
            return opresp;
        } catch (Exception ex) {
            log.error("Failed to fetch OPSkins sales data", ex.getMessage());
            return null;
        }
    }
}
