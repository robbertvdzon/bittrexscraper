package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.CoinVolume;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinVolumeRepository extends JpaRepository<CoinVolume, Long> {
    List<CoinVolume> findBymarketUuidOrderByTimestampDesc(Long marketUuid);

    @Query("select u from CoinVolume u where marketUuid = :marketUuid and timestamp <= :timestamp order by timestamp desc")
    public List<CoinVolume> findCoinvolumeAfter(@Param("marketUuid") long marketUuid, @Param("timestamp") long timestamp, Pageable pageable);

    default CoinVolume findFirstCoinvolumeAfter(long marketUuid, @Param("timestamp") long timestamp) {
        List<CoinVolume> coinvolumeAfter = findCoinvolumeAfter(marketUuid, timestamp, new PageRequest(0, 1));
        return coinvolumeAfter.isEmpty() ? null : coinvolumeAfter.get(0);
    }


}
