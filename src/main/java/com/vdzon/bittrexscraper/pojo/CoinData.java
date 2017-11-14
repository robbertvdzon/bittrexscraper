package com.vdzon.bittrexscraper.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(indexes = @Index(columnList = "marketUuid,timestamp"))
public class CoinData {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long uuid;
    @JsonIgnore
    private Long marketUuid;
    @JsonProperty(value="r")
    private double rate;
    @JsonProperty(value="v")
    private double volume;
    @JsonProperty(value="t")
    private long timestamp;
    @JsonProperty(value="md1h1m")
    private double macd1h1m;
    @JsonProperty(value="s1h1m")
    private double signald1h1m;
    @JsonProperty(value="rsi1h")
    private double rsi1h;
    @JsonProperty(value="stc1h1m")
    private double stc1h1m;
    @JsonProperty(value="hi24h")
    private double highest24h;
    @JsonProperty(value="lo24h")
    private double lowest24h;
    @JsonProperty(value="hi26h")
    private double highest26h;
    @JsonProperty(value="lo26h")
    private double lowest26h;
    @JsonProperty(value="hi12h")
    private double highest12h;
    @JsonProperty(value="lo12h")
    private double lowest12h;

    public CoinData() {
    }

    public CoinData(Long marketUuid, double rate) {
        this(marketUuid, rate, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public CoinData(Long marketUuid, double rate, long timestamp) {
        this.marketUuid = marketUuid;
        this.rate = rate;
        this.timestamp = timestamp;
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

    public double getMacd1h1m() {
        return macd1h1m;
    }

    public void setMacd1h1m(double macd1h1m) {
        this.macd1h1m = macd1h1m;
    }

    public double getSignald1h1m() {
        return signald1h1m;
    }

    public void setSignald1h1m(double signald1h1m) {
        this.signald1h1m = signald1h1m;
    }

    public double getRsi1h() {
        return rsi1h;
    }

    public void setRsi1h(double rsi1h) {
        this.rsi1h = rsi1h;
    }

    public double getStc1h1m() {
        return stc1h1m;
    }

    public void setStc1h1m(double stc1h1m) {
        this.stc1h1m = stc1h1m;
    }

    public double getHighest24h() {
        return highest24h;
    }

    public void setHighest24h(double highest24h) {
        this.highest24h = highest24h;
    }

    public double getLowest24h() {
        return lowest24h;
    }

    public void setLowest24h(double lowest24h) {
        this.lowest24h = lowest24h;
    }

    public double getHighest26h() {
        return highest26h;
    }

    public void setHighest26h(double highest26h) {
        this.highest26h = highest26h;
    }

    public double getLowest26h() {
        return lowest26h;
    }

    public void setLowest26h(double lowest26h) {
        this.lowest26h = lowest26h;
    }

    public double getHighest12h() {
        return highest12h;
    }

    public void setHighest12h(double highest12h) {
        this.highest12h = highest12h;
    }

    public double getLowest12h() {
        return lowest12h;
    }

    public void setLowest12h(double lowest12h) {
        this.lowest12h = lowest12h;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
