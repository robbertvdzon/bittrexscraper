package com.vdzon.bittrexscraper.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class MarketSummary {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long uuid;
    @JsonProperty("MarketName")
    public String marketName;
    @JsonProperty("Volume")
    public double volume;
    @JsonProperty("Last")
    public double last;
    @JsonProperty("LastData")
    transient public CoinData lastData;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public CoinData getLastData() {
        return lastData;
    }

    public void setLastData(CoinData lastData) {
        this.lastData = lastData;
    }

    @Override
    public String toString() {
        return "MarketSummary{" +
                "uuid=" + uuid +
                ", marketName='" + marketName + '\'' +
                ", volume=" + volume +
                ", last=" + last +
                '}';
    }
}
