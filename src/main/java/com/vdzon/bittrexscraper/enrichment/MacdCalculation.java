package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.util.AnalysisUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MacdCalculation implements CalculationEnrichment{
    @Override
    public void enrich(final long marketUuid, final List<CoinRate> history, CoinRate newValue) {
        long start = newValue.getTimestamp();
        long period = TimeUnit.HOURS.toSeconds(1);
        long step = TimeUnit.MINUTES.toSeconds(1);
        double[] sample = AnalysisUtils.macd(marketUuid, history, start, period, step);
        double macd1h1m = sample[0];
        double signal1h1m = sample[1];
        newValue.setMacd1h1m(macd1h1m);
        newValue.setSignald1h1m(signal1h1m);
    }
}
