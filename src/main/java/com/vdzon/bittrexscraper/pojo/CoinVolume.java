package com.vdzon.bittrexscraper.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(indexes = @Index(columnList = "marketUuid,timestamp"))
public class CoinVolume {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long uuid;
    private Long marketUuid;
    private double volume;
    private long timestamp;

    public CoinVolume() {
    }

    public CoinVolume(Long marketUuid, double volume) {
        this.marketUuid = marketUuid;
        this.volume = volume;
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

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
