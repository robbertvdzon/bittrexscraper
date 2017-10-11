package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinVolume;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoinVolumeRepository extends CrudRepository<CoinVolume, Long> {
    List<CoinVolume> findBymarketUuid(Long marketUuid);

}
