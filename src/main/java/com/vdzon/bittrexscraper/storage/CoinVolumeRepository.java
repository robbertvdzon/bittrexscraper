package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinVolumeRepository extends JpaRepository<CoinVolume, Long> {
    List<CoinVolume> findBymarketUuidOrderByTimestampDesc(Long marketUuid);

    @Query("select u from CoinVolume u where marketUuid = :marketUuid and timestamp <= :timestamp order by timestamp desc")
    public List<CoinVolume> findCoinvolumeAfter(@Param("marketUuid") long marketUuid, @Param("timestamp") long timestamp);


}
