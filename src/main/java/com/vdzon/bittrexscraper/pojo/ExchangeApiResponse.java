package com.vdzon.bittrexscraper.pojo;

import java.util.List;

public class ExchangeApiResponse {
    public boolean success;
    public String message;
    public List<MarketSummary> result;
}