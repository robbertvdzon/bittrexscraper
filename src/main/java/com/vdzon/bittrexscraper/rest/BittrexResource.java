package com.vdzon.bittrexscraper.rest;

import com.vdzon.bittrexscraper.pojo.CoinRate;
import com.vdzon.bittrexscraper.pojo.CoinVolume;
import com.vdzon.bittrexscraper.pojo.MarketSummary;
import com.vdzon.bittrexscraper.pojo.Summary;
import com.vdzon.bittrexscraper.storage.CoinRateRepository;
import com.vdzon.bittrexscraper.storage.CoinVolumeRepository;
import com.vdzon.bittrexscraper.storage.MarketSummaryRepository;
import com.vdzon.bittrexscraper.storage.SummaryRateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

    @Inject
    SummaryRateRepository summaryRateRepository;


    @GetMapping(path = "/getcurrenttime")
    public @ResponseBody
    long getCurrentTime() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    @GetMapping(path = "/getsummaries")
    public @ResponseBody
    List<Summary> getSummaries() {
        Iterable<Summary> all = summaryRateRepository.findAll();
        List<Summary> summary = new ArrayList<>();
        all.forEach(summ -> summary.add(summ));
        return summary
                .stream()
                .sorted((c1, c2) -> Double.compare(c2.getTimestamp(), c1.getTimestamp()))
                .collect(Collectors.toList());
    }

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

    @GetMapping(path = "/getcoindata")
    public @ResponseBody
    List<MarketSummary> getMarketSummaries(@RequestParam(value = "timestamp") long timestamp) {
        Iterable<MarketSummary> currentMarkets = marketSummaryRepository.findAll();
        List<MarketSummary> currentCoins = new ArrayList<>();
        currentMarkets.forEach(summ -> currentCoins.add(summ));
        return currentCoins
                .stream()
                .filter(coin->coin.marketName.startsWith("BTC"))
                .map(coin -> getCoinAtTimestamp(coin, timestamp))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/coinvolumes")
    public @ResponseBody
    List<CoinVolume> getCoinVolumes(@RequestParam(value = "marketname") String marketname,
                                    @RequestParam(value = "datetimefrom", defaultValue = "0") long datetimefrom,
                                    @RequestParam(value = "datetimeto", defaultValue = ""+Long.MAX_VALUE) long datetimeto) {
        MarketSummary firstByMarketName = marketSummaryRepository.findFirstByMarketName(marketname);
        return firstByMarketName == null
                ? Collections.emptyList()
                : coinVolumeRepository
                .findBymarketUuidOrderByTimestampDesc(firstByMarketName.uuid)
                .stream()
                .filter(volume->volume.getTimestamp()>datetimefrom)
                .filter(volume->volume.getTimestamp()<datetimeto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/coinrates")
    public @ResponseBody
    List<CoinRate> getCoinRates(@RequestParam(value = "marketname") String marketname,
                                 @RequestParam(value = "datetimefrom", defaultValue = "0") long datetimefrom,
                                 @RequestParam(value = "datetimeto", defaultValue = ""+Long.MAX_VALUE) long datetimeto) {
        MarketSummary firstByMarketName = marketSummaryRepository.findFirstByMarketName(marketname);
        return firstByMarketName == null
                ? Collections.emptyList()
                : coinRateRepository.findBymarketUuidOrderByTimestampDesc(firstByMarketName.uuid)
                .stream()
                .filter(rate->rate.getTimestamp()>datetimefrom)
                .filter(rate->rate.getTimestamp()<datetimeto)
                .collect(Collectors.toList());
    }

    private MarketSummary getCoinAtTimestamp(MarketSummary coin, long timestamp) {
        MarketSummary result = new MarketSummary(
                coin.uuid,
                coin.getMarketName(),
                findVolumeOn(coin.uuid, timestamp),
                findRateOn(coin.uuid, timestamp)
        );
        return result;
    }

    private double findRateOn(long coinUuid, long timestamp) {
        Iterable<CoinRate> coinrateAfter = coinRateRepository.findCoinrateAfter(coinUuid, timestamp);
        Iterator<CoinRate> iterator = coinrateAfter.iterator();
        return iterator.hasNext()? iterator.next().getRate() : -1;
    }

    private double findVolumeOn(long coinUuid, long timestamp) {
        Iterable<CoinVolume> coinrateAfter = coinVolumeRepository.findCoinvolumeAfter(coinUuid, timestamp);
        Iterator<CoinVolume> iterator = coinrateAfter.iterator();
        return iterator.hasNext()? iterator.next().getVolume() : -1;
    }

}
