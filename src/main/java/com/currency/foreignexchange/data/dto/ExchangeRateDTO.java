package com.currency.foreignexchange.data.dto;

/**
 * This class is a POJO, used to return the live exchange rates for two currencies.
 */
public class ExchangeRateDTO {
    private String sourceCurrency;
    private String targetCurrency;
    private double exchangeRate;

    public ExchangeRateDTO(String sourceCurrency, String targetCurrency, double exchangeRate) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
