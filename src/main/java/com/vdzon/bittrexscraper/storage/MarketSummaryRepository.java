package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.MarketSummary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarketSummaryRepository extends CrudRepository<MarketSummary, Long> {
    MarketSummary findFirstByMarketName(String marketName);
    List<MarketSummary> findAll();
}
