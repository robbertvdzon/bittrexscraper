package com.vdzon.bittrexscraper.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
public class Summary {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long uuid;
    @JsonProperty(value="e")
    private double btcEuroPrice;
    @JsonProperty(value="a")
    private double averageRate;
    @JsonProperty(value="tv")
    private double totalVolume;
    @JsonProperty(value="t")
    private long timestamp;

    public Summary() {
    }

    public Summary(double btcEuroPrice, double averageRate, double totalVolume,double btcVolume) {
        this.btcEuroPrice = btcEuroPrice;
        this.averageRate = averageRate;
        this.totalVolume = totalVolume;
        this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

    }

    public Long getUuid() {
        return uuid;
    }

    public double getBtcEuroPrice() {
        return btcEuroPrice;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
