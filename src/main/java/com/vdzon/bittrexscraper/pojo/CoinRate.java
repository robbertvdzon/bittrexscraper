package com.vdzon.bittrexscraper.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "marketUuid,timestamp"))
public class CoinRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long uuid;
    @JsonIgnore
    private Long marketUuid;
    @JsonProperty(value="r")
    private double rate;
    @JsonProperty(value="b")
    private double bid;
    @JsonProperty(value="a")
    private double ask;
    @JsonProperty(value="t")
    private long timestamp;

    public CoinRate() {
    }

    public CoinRate(Long marketUuid, double rate, double bid, double ask) {
        this.marketUuid = marketUuid;
        this.rate = rate;
        this.bid = bid;
        this.ask = ask;
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

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
