package com.vdzon.bittrexscraper.rest;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.CoinVolume;
import com.vdzon.bittrexscraper.pojo.MarketSummary;
import com.vdzon.bittrexscraper.storage.CoinRateRepository;
import com.vdzon.bittrexscraper.storage.CoinVolumeRepository;
import com.vdzon.bittrexscraper.storage.MarketSummaryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class BittrexResource {

    @Inject
    MarketSummaryRepository marketSummaryRepository;

    @Inject
    CoinRateRepository coinRateRepository;

    @Inject
    CoinVolumeRepository coinVolumeRepository;


    @GetMapping(path = "/getmarketsummaries")
    public @ResponseBody
    List<MarketSummary> getMarketSummaries() {
        Iterable<MarketSummary> all = marketSummaryRepository.findAll();
        List<MarketSummary> coins = new ArrayList<>();
        all.forEach(summ -> coins.add(summ));
        return coins
                .stream()
                .filter(coin->coin.marketName.startsWith("BTC"))
                .sorted((c1, c2) -> Double.compare(c2.last, c1.last))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/coinvolumes")
    public @ResponseBody
    List<CoinVolume> getCoinVolumes(@RequestParam(value = "marketname") String marketname) {
        MarketSummary firstByMarketName = marketSummaryRepository.findFirstByMarketName(marketname);
        return firstByMarketName == null ? Collections.emptyList() : coinVolumeRepository.findBymarketUuid(firstByMarketName.uuid);
    }

    @GetMapping(path = "/coinrates")
    public @ResponseBody
    List<CoinRate> getCoinRatges(@RequestParam(value = "marketname") String marketname) {
        MarketSummary firstByMarketName = marketSummaryRepository.findFirstByMarketName(marketname);
        return firstByMarketName == null ? Collections.emptyList() : coinRateRepository.findBymarketUuid(firstByMarketName.uuid);
    }

}
