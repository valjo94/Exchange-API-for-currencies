package com.currency.foreignexchange.data.dto;

import com.currency.foreignexchange.validators.CurrencyConstraint;

/**
 * Data transfer object class, used for for creating currency exchange transactions.
 */
public class CurrencyTransactionDTO {
    private String sourceCurrency;
    private String targetCurrency;
    private double amount;

    public CurrencyTransactionDTO() {
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(@CurrencyConstraint(message = "sourceCurrency") String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(@CurrencyConstraint(message = "targetCurrency") String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
