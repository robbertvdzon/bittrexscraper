package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.util.AnalysisUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RSICalculation implements CalculationEnrichment{
    @Override
    public void enrich(final long marketUuid, final List<CoinRate> history, CoinRate newValue) {
        long start = newValue.getTimestamp();
        long period = TimeUnit.HOURS.toSeconds(1);
        double rsi = AnalysisUtils.rsi(history, start, period);
        newValue.setRsi1h(rsi);
    }
}
