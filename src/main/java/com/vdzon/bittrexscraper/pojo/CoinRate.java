package com.vdzon.bittrexscraper.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
public class CoinRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long uuid;
    private Long marketUuid;
    private double rate;
    private long timestamp;

    public CoinRate() {
    }

    public CoinRate(Long marketUuid, double rate) {
        this.marketUuid = marketUuid;
        this.rate = rate;
        this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getMarketUuid() {
        return marketUuid;
    }

    public void setMarketUuid(Long marketUuid) {
        this.marketUuid = marketUuid;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
