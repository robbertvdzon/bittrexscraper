package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.storage.CoinRateRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class Enrich {
    @Inject
    List<CalculationEnrichment> calculations;

    @Inject
    CoinRateRepository coinRateRepository;

    public CoinRate enrich(final long marketUuid, CoinRate newValue){
        List<CoinRate> history = coinRateRepository.findBymarketUuidOrderByTimestampDesc(marketUuid);
        calculations.stream().forEach(calculation -> calculation.enrich(marketUuid, history, newValue));
        return newValue;
    }
}
