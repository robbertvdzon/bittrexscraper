package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinRateRepository extends JpaRepository<CoinRate, Long> {
    List<CoinRate> findBymarketUuidOrderByTimestampDesc(Long marketUuid);


    @Query("select u from CoinRate u where marketUuid = :marketUuid and timestamp <= :timestamp order by timestamp desc")
    public List<CoinRate> findCoinrateAfter(@Param("marketUuid") long marketUuid, @Param("timestamp") long timestamp);
}
