DROP TABLE IF EXISTS currencyTransaction;

CREATE TABLE currencyTransaction(id INT AUTO_INCREMENT  PRIMARY KEY, sourceCurrency VARCHAR(250) NOT NULL, targetCurrency VARCHAR(250) NOT NULL, amount DOUBLE, date DATE);