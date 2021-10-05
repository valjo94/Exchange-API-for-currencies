package com.currency.foreignexchange.data.entities;

import com.currency.foreignexchange.validators.CurrencyConstraint;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Entity class for all currency exchange transactions, corresponding to the 'transaction' table in db.
 */
@Entity
@Table
public class CurrencyTransaction {
    private Long transactionId;
    private String sourceCurrency;
    private String targetCurrency;
    private double amount;
    private Date date; 

    public CurrencyTransaction() {}

    @Id
    @GeneratedValue
    @Column(name="id")
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Column
    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(@CurrencyConstraint(message = "sourceCurrency") String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    @Column
    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(@CurrencyConstraint(message = "targetCurrency") String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column
    @CreationTimestamp
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
