package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.MarketSummary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CoinRateRepository extends CrudRepository<CoinRate, Long> {
    List<CoinRate> findBymarketUuidOrderByTimestampDesc(Long marketUuid);
}
