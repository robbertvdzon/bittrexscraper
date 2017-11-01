package com.vdzon.bittrexscraper.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerData {
    @JsonProperty("15m")
    public double fifteenMinutesDelayed;
    public double last;
    public double buy;
    public double sell;
    public String symbol;
}
