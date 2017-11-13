package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.util.AnalysisUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class HighLowCalculation implements CalculationEnrichment{
    @Override
    public void enrich(final long marketUuid, final List<CoinRate> history, CoinRate newValue) {
        long start = newValue.getTimestamp();
        long period = TimeUnit.HOURS.toSeconds(24);
        double[] sample = AnalysisUtils.highestAndLowest(history, start, start-period);
        newValue.setHighest24h(sample[0]);
        newValue.setLowest24h(sample[1]);

        period = TimeUnit.HOURS.toSeconds(26);
        sample = AnalysisUtils.highestAndLowest(history, start, start-period);
        newValue.setHighest26h(sample[0]);
        newValue.setLowest26h(sample[1]);

        period = TimeUnit.HOURS.toSeconds(12);
        sample = AnalysisUtils.highestAndLowest(history, start, start-period);
        newValue.setHighest12h(sample[0]);
        newValue.setLowest12h(sample[1]);

    }
}
