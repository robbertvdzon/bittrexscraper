package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.util.AnalysisUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SchaffTrendCycleCalculation implements CalculationEnrichment{
    @Override
    public void enrich(final long marketUuid, final List<CoinRate> history, CoinRate newValue) {
        long start = newValue.getTimestamp();
        long period = TimeUnit.HOURS.toSeconds(1);
        long step = TimeUnit.MINUTES.toSeconds(1);
        double stc = AnalysisUtils.schaffTrendCycle(history, start, period, step);
        newValue.setStc1h1m(stc);
    }
}
