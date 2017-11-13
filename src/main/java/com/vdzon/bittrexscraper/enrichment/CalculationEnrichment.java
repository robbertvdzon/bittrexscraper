package com.vdzon.bittrexscraper.enrichment;

import com.vdzon.bittrexscraper.pojo.CoinRate;

import java.util.List;

public interface CalculationEnrichment {
    void enrich(final long marketUuid, final List<CoinRate> history, CoinRate newValue);
}
