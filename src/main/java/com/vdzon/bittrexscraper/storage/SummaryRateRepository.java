package com.vdzon.bittrexscraper.storage;


import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.Summary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SummaryRateRepository extends CrudRepository<Summary, Long> {

}
