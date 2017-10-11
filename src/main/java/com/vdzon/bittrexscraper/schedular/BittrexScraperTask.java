package com.vdzon.bittrexscraper.schedular;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.CoinVolume;
import com.vdzon.bittrexscraper.pojo.ExchangeApiResponse;
import com.vdzon.bittrexscraper.pojo.MarketSummary;
import com.vdzon.bittrexscraper.storage.CoinRateRepository;
import com.vdzon.bittrexscraper.storage.CoinVolumeRepository;
import com.vdzon.bittrexscraper.storage.MarketSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BittrexScraperTask {
    private static final Logger log = LoggerFactory.getLogger(BittrexScraperTask.class);

    private static final String URL = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
    private static final double MAX_PERC_CHANGED = 1;
    private RestTemplate restTemplate = new RestTemplate();
    private Map<String, MarketSummary> cache = new HashMap<>();

    @Inject
    MarketSummaryRepository marketSummaryRepository;

    @Inject
    CoinVolumeRepository coinVolumeRepository;

    @Inject
    CoinRateRepository coinRateRepository;


    @PostConstruct
    public void init(){
        loadCache();
    }

    private void loadCache() {
        Iterable<MarketSummary> all = marketSummaryRepository.findAll();
        all.forEach(summ->cache.put(summ.getMarketName(), summ));
    }


    @Scheduled(fixedDelay = 10000)
    public void reportCurrentTime() {
        List<MarketSummary> summaries = getMarketSummaries();
        int changes = summaries.stream().mapToInt(summ-> processSummary(summ)).sum();
        log.info("#changes : {}", changes);

    }

    private List<MarketSummary> getMarketSummaries() {
        ExchangeApiResponse result = restTemplate.getForObject(URL, ExchangeApiResponse.class);
        return result.result;
    }

    private int processSummary(MarketSummary summ) {
        MarketSummary lastMarketSummary = findInCacheOrCreateNew(summ);
        boolean volumeChanged = valueChangedEnough(summ.getVolume(), lastMarketSummary.getVolume(),MAX_PERC_CHANGED);
        volumeChanged = false; // only check rate!
        boolean rateChanged = valueChangedEnough(summ.getLast(), lastMarketSummary.getLast(),MAX_PERC_CHANGED);
        if (volumeChanged||rateChanged) {
            if (lastMarketSummary!=null) {
                summ.uuid = lastMarketSummary.uuid;
            }
            marketSummaryRepository.save(summ);
            cache.put(summ.getMarketName(), summ);
        }
        if (volumeChanged) {
            System.out.println("Volume is changed more then "+MAX_PERC_CHANGED+"% for "+summ.getMarketName()+" : "+ lastMarketSummary.getVolume()+" -> "+summ.getVolume());
            coinVolumeRepository.save(new CoinVolume(summ.uuid,summ.getVolume()));
        }
        if (rateChanged) {
            System.out.println("Rate is changed more then "+MAX_PERC_CHANGED+"% for "+summ.getMarketName()+"/"+summ.getUuid()+" : "+ lastMarketSummary.getLast()+" -> "+summ.getLast());
            coinRateRepository.save(new CoinRate(summ.uuid,summ.getLast()));
        }
        return volumeChanged||rateChanged ? 1 : 0;
    }

    private MarketSummary findInCacheOrCreateNew(MarketSummary summ) {
        MarketSummary marketSummary = cache.get(summ.getMarketName());
        if (marketSummary==null){
            marketSummary = new MarketSummary();
            marketSummary.setMarketName(summ.getMarketName());
        }
        return marketSummary;
    }

    private boolean valueChangedEnough(double cached, double current, double maxPerc) {
        if (cached==current) return false;
        if (cached==0) return true;
        double diff = Math.abs(cached-current);
        double percentageChanged = (diff*100)/cached;
        return percentageChanged>maxPerc;
    }
}
