package com.vdzon.bittrexscraper.rest;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.CoinVolume;
import com.vdzon.bittrexscraper.pojo.MarketSummary;
import com.vdzon.bittrexscraper.storage.CoinRateRepository;
import com.vdzon.bittrexscraper.storage.CoinVolumeRepository;
import com.vdzon.bittrexscraper.storage.MarketSummaryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class BittrexResource {

    @Inject
    MarketSummaryRepository marketSummaryRepository;

    @Inject
    CoinRateRepository coinRateRepository;

    @Inject
    CoinVolumeRepository coinVolumeRepository;



    @GetMapping(path="/getmarketsummaries")
    public @ResponseBody
    List<MarketSummary> getMarketSummaries() {
        Iterable<MarketSummary> all = marketSummaryRepository.findAll();
        List<MarketSummary> coins = new ArrayList<>();
        all.forEach(summ->coins.add(summ));
        return coins;
    }

    @GetMapping(path="/coinvolumes")
    public @ResponseBody
    List<CoinVolume> getCoinVolumes(@RequestParam(value="coinid", required=false, defaultValue="0") Long coinid) {
        return coinVolumeRepository.findBymarketUuid(coinid);
    }

    @GetMapping(path="/coinrates")
    public @ResponseBody
    List<CoinRate> getCoinRatges(@RequestParam(value="coinid", required=false, defaultValue="0") Long coinid) {
        return coinRateRepository.findBymarketUuid(coinid);
    }

}
