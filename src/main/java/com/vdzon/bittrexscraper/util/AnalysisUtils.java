package com.vdzon.bittrexscraper.util;


import com.vdzon.bittrexscraper.pojo.CoinRate;

import java.util.ArrayList;
import java.util.List;

public class AnalysisUtils {

    private AnalysisUtils() {
    }

    /**
     * Returns the rate of the coin at the specified timestamp
     * @param history The coin history with the rate deltas
     * @param timestamp The timestamp (seconds since epoch) of the rate to retrieve
     * @param index The start index used to iterate the history (use 0 if not known). This is only used for
     *              performance improvement. If you know the index of the entry that the provided timestamp
     *              should be close to.
     * @return The requested rate
     */
    private static CoinRate getCoinInfoAtTimestamp(List<CoinRate> history, long timestamp, IntHolder index) {
        //A (not so thorough) check to see if the history is in the right order
        if (history.get(0).getTimestamp() < history.get(history.size()-1).getTimestamp()) {
            throw new RuntimeException("The rate history should be sorted by timestamp, most recent first");
        }

        //Travel down (if needed) to find the last known rate for the provided timestamp
        while (index.value < history.size() && history.get(index.value).getTimestamp() > timestamp) {
            index.value++;
        }

        //Not found, return the oldest rate we know
        if (index.value >= history.size()) {
            return history.get(history.size()-1);
        }

        //Travel up (if needed) to find the last known rate for the provided timestamp
        while (index.value-1 > 0 && history.get(index.value-1).getTimestamp() <= timestamp) {
            index.value--;
        }

        //Not found, return the latest rate we know
        if (index.value <= 0) {
            return history.get(0);
        }

        return history.get(index.value);
    }

    public static double getRateAtTimestamp(List<CoinRate> history, long timestamp, IntHolder index) {
        //If there is no history, there is no value (maybe treat as error?)
        if (history == null || history.size() == 0) {
            return 0;
        }

        return getCoinInfoAtTimestamp(history, timestamp, index).getRate();
    }

    /**
     * Returns the rate of the coin at the specified timestamp
     * @param history The coin history with the rate deltas
     * @param timestamp The timestamp (seconds since epoch) of the rate to retrieve
     * @return The requested rate
     */
    public static double getRateAtTimestamp(List<CoinRate> history, long timestamp) {
        return getRateAtTimestamp(history, timestamp, new IntHolder(0));
    }

    /**
     * Calculates the moving average on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param end The end timestamp (seconds since epoch) of the period
     * @param step The number of seconds to use to step from start to end
     * @return The calculated moving average
     */
    public static double movingAverage(final List<CoinRate> history, long start, long end, final long step) {
        int cnt = 0;
        double total = 0;

        //Make sure start is more recent than end
        if (start < end) {
            start = start ^ end ^ (end = start);
        }

        final IntHolder historyIndex = new IntHolder(0);
        for (long i=start; i>=end; i-=step) {
            total += getRateAtTimestamp(history, i, historyIndex);
            cnt++;
        }

        return cnt==0 ? 0 : total/cnt;
    }

    /**
     * Calculates the exponential moving average on the specified period. The EMA is basically the same
     * as the regular moving average, but the more recent values have slightly more weight.
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param end The end timestamp (seconds since epoch) of the period
     * @param step The number of seconds to use to step from start to end
     * @return The calculated moving average
     */
    public static double exponentialMovingAverage(final List<CoinRate> history, long start, long end, final long step) {
        //Make sure start is more recent than end
        if (start < end) {
            start = start ^ end ^ (end = start);
        }

        double alpha = 2.0 / ((start-end)/step + 1);
        double ema = getRateAtTimestamp(history, end);
        double previousEma = ema;
        final IntHolder historyIndex = new IntHolder(0);
        for (long i=end+step; i<start; i+=step) {
            ema = getRateAtTimestamp(history, i, historyIndex) * alpha + previousEma * (1.0 - alpha);
            previousEma = ema;
        }

        return ema;
    }

    /**
     * Returns the calculated Moving Average Convergence/Divergence on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param period The length of the period in seconds to use in the calculation. In conventional trading
     *               this period is one day.
     * @param step The number of seconds to use to step through the historical data
     * @return A double array with 2 elements where index 0 represents the macd value, and index 1 the signal value
     */
    public static double[] macd(final long marketUuid, final List<CoinRate> history, long start, long period, final long step) {
        //http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_convergence_divergence_macd
        List<CoinRate> macd = new ArrayList<>((int)(period/step+0.5));

        for (long i=start; i>(start-period); i-=step) {
            double ma1 = movingAverage(history, i, i-period*12, step);
            double ma2 = movingAverage(history, i, i-period*26, step);
            macd.add(new CoinRate(marketUuid, ma1 - ma2, i));
        }

        double signal = movingAverage(macd, start, start-period*9, step);

        return new double[]{macd.get(0).getRate(), signal};
    }

    /**
     * Calulates the Relative Strength Index on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param period The length of the period in seconds to use in the calculation. In conventional trading
     *               this period is one day.
     * @return The calculated RSI
     */
    public static double rsi(final List<CoinRate> history, long start, long period) {

        double firstAverageGain = 0;  //First Average Gain = Sum of Gains over the past 14 periods / 14
        double firstAverageLoss = 0;  //First Average Loss = Sum of Losses over the past 14 periods / 14
        IntHolder indexHolder = new IntHolder(0);
        double previousRate = getRateAtTimestamp(history, start-period*14, indexHolder);
        for (long i=start-period*13; i<start-period; i+=period) {
            double rate = getRateAtTimestamp(history, i, indexHolder);
            double delta = rate - previousRate;
            if (delta > 0) {
                //Gain
                firstAverageGain += delta;
            } else {
                //Loss
                firstAverageLoss += -delta;
            }
            previousRate = rate;
        }
        firstAverageGain /= 14.0;
        firstAverageLoss /= 14.0;

        previousRate = getRateAtTimestamp(history, start-period, indexHolder);
        double currentRate = getRateAtTimestamp(history, start, indexHolder);
        double currentDelta = currentRate - previousRate;
        double currentGain = currentDelta > 0 ? currentDelta : 0;
        double currentLoss = currentDelta < 0 ? -currentDelta : 0;

        //Average Gain = [(previous Average Gain) x 13 + current Gain] / 14
        //Average Loss = [(previous Average Loss) x 13 + current Loss] / 14
        double averageGain = (firstAverageGain * 13.0 + currentGain) / 14.0;
        double averageLoss = (firstAverageLoss * 13.0 + currentLoss) / 14.0;

        if (averageGain == 0 && averageLoss == 0) {
            return 50;
        } else if (averageGain == 0) {
            return 0;
        } else if (averageLoss == 0) {
            return 100;
        }

        double relativeStrength = averageGain / averageLoss;
        return 100 - (100 / (1+relativeStrength));
    }

    /**
     * Calculates both the highest and lowest value on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param end The end timestamp (seconds since epoch) of the period
     * @return A double array with 2 elements where index 0 represents the highest value and index 1 the lowest
     */
    public static double[] highestAndLowest(final List<CoinRate> history, long start, long end) {
        //If there is no history, there is no highest and lowest (maybe treat as error?)
        if (history == null || history.size() == 0) {
            return new double[]{0, 0};
        }

        //Make sure start is more recent than end
        if (start < end) {
            start = start ^ end ^ (end = start);
        }

        int index = 0;

        //Travel down (if needed) to find the last known rate for the provided timestamp
        while (index < history.size() && history.get(index).getTimestamp() > start) {
            index++;
        }

        //No rates found for the specified period, return 0, 0
        if (index >= history.size()) {
            return new double[]{0, 0};
        }

        double highest = history.get(index).getRate();
        double lowest = highest;

        CoinRate cr;
        while (index < history.size() && (cr=history.get(index)).getTimestamp() >= end) {
            double rate = cr.getRate();
            if (rate > highest) {
                highest = rate;
            }
            if (rate < lowest) {
                lowest = rate;
            }
            index++;
        }

        return new double[]{highest, lowest};
    }

    /**
     * Calculates the highest value on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param end The end timestamp (seconds since epoch) of the period
     * @return The highest value (or rate) of the coin
     */
    public static double highest(final List<CoinRate> history, long start, long period, final long end) {
        return highestAndLowest(history, start, period)[0];
    }

    /**
     * Calculates the lowest value on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param end The end timestamp (seconds since epoch) of the period
     * @return The lowest value (or rate) of the coin
     */
    public static double lowest(final List<CoinRate> history, long start, long period, final long end) {
        return highestAndLowest(history, start, period)[1];
    }

    /**
     * Returns the calculated SchaffTrendCycle on the specified period
     * @param history The coin history with the rate deltas
     * @param start The start timestamp (seconds since epoch) of the period
     * @param period The length of the period in seconds to use in the calculation. In conventional trading
     *               this period is one day.
     * @param step The number of seconds to use to step through the historical data
     * @return The STC value (0..100)
     */
    public static double schaffTrendCycle(final List<CoinRate> history, long start, long period, final long step) {
        int cycle = 10;
        double factor = 0.5;

        double[] macd = new double[(int) (period * cycle / step + 0.5)];
        double[] pf = new double[macd.length];
        double[] pff = new double[macd.length];

        double f1 = 0;
        double f2 = 0;

        double lowestMacd = 0;
        double highestMacd = 0;
        double lowestPf = 0;
        double highestPf = 0;

        int index = 0;
        for (long i = start - period * cycle; i < start; i += step, index++) {
            double ma1 = movingAverage(history, i, i - period * 9, step);
            double ma2 = movingAverage(history, i, i - period * 30, step);
            macd[index] = ma1 - ma2;

            if (index == 0 || macd[index] < lowestMacd) {
                lowestMacd = macd[index];
            }
            if (index == 0 || macd[index] > highestMacd) {
                highestMacd = macd[index];
            }

            double v1 = lowestMacd;
            double v2 = highestMacd - v1;
            f1 = v2 > 0 ? (macd[index] - v1) / v2 * 100 : f1;
            pf[index] = index == 0 ? f1 : pf[index - 1] + (factor * (f1 - pf[index - 1]));

            if (index == 0 || pf[index] < lowestPf) {
                lowestPf = pf[index];
            }
            if (index == 0 || pf[index] > highestPf) {
                highestPf = pf[index];
            }

            double v3 = lowestPf;
            double v4 = highestPf - v3;
            f2 = v4 > 0 ? (pf[index] - v3) / v4 * 100 : f2;
            pff[index] = index == 0 ? f2 : pff[index - 1] + (factor * (f2 - pff[index - 1]));
        }

        return pff[pff.length - 1];
    }

    /*
    public static double onBalanceVolume(final List<CoinRate> history, long start, long period, final long step) {
        double total = 0;
        final IntHolder historyIndex = new IntHolder(0);
        double previousRate = getRateAtTimestamp(history, start - period, historyIndex);
        for (long i = start - period + step; i < start; i += step) {
            CoinRate info = getCoinInfoAtTimestamp(history, i, historyIndex);
            double newRate = info.getRate();
            double change = previousRate - newRate;
            if (change > 0) {
                total += info.getVolume();
            } else if (change < 0) {
                total -= info.getVolume();
            }
            previousRate = newRate;
        }
        return total;
    }
    */
}
