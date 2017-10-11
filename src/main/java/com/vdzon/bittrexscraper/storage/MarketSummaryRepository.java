package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.MarketSummary;
import org.springframework.data.repository.CrudRepository;

public interface MarketSummaryRepository extends CrudRepository<MarketSummary, Long> {
}
