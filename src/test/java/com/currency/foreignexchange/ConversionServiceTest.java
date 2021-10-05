package com.currency.foreignexchange;

import com.currency.foreignexchange.data.TransactionRepository;
import com.currency.foreignexchange.data.dto.CurrencyTransactionDTO;
import com.currency.foreignexchange.data.dto.external.CurrencyLayerDTO;
import com.currency.foreignexchange.data.entities.CurrencyTransaction;
import com.currency.foreignexchange.exceptionhandling.custom.ElementNotFoundException;
import com.currency.foreignexchange.services.ConversionService;
import com.currency.foreignexchange.services.CurrencyExchangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConversionServiceTest {

    @Autowired
    ConversionService conversionService;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeAll
    public void init() throws ServiceUnavailableException {
        CurrencyLayerDTO mockedCurrencyLayerDTO = getMockedCurrencyLayerDTO();

        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenReturn(mockedCurrencyLayerDTO);

        CurrencyExchangeService mockedCurrencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);

        conversionService = new ConversionService(mockedCurrencyExchangeService, transactionRepository);

        CurrencyTransactionDTO transaction = getMockedCurrencyTransactionDTO(10);
        conversionService.convertCurrency(transaction);
    }

    @Test
    @Order(1)
    void testConvertCurrency() throws ServiceUnavailableException {
        CurrencyTransactionDTO transaction = getMockedCurrencyTransactionDTO(5);
        double expectedResult = 9.7789;
        double expectedResultRounded = Math.round(expectedResult * 100.0) / 100.0;

        CurrencyTransaction resultCurrencyTransaction = conversionService.convertCurrency(transaction);
        double realResult = Math.round(resultCurrencyTransaction.getAmount() * 100.0) / 100.0;

        Assertions.assertEquals(expectedResultRounded, realResult);
    }

    @Test
    @Order(1)
    void testConvertCurrencyShouldThrowServiceUnavailableException() {
        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenThrow(RestClientException.class);

        CurrencyExchangeService mockedCurrencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);

        conversionService = new ConversionService(mockedCurrencyExchangeService, transactionRepository);

        Assertions.assertThrows(ServiceUnavailableException.class,
                () -> conversionService.convertCurrency(getMockedCurrencyTransactionDTO(5)));
    }

    @Test
    @Order(2)
    void testGetCurrencyById() throws ElementNotFoundException {
        CurrencyTransaction currencyById = conversionService.getTransactionById(2);
        Assertions.assertEquals(LocalDate.now(), currencyById.getDate().toLocalDate());
        Assertions.assertEquals(9.77806463563585, currencyById.getAmount());
    }

    @Test
    @Order(3)
    void testGetCurrencyByIdShouldThrowElementNotFoundException() {
        Assertions.assertThrows(ElementNotFoundException.class, () -> conversionService.getTransactionById(100));
    }

    @Test
    @Order(4)
    void testGetCurrencyByDate() throws ElementNotFoundException {

        List<CurrencyTransaction> currencyById = conversionService.getTransactionByDate(Date.valueOf(LocalDate.now()).toString(), 0, 10);

        // Assert list size is as expected from all tests 
        Assertions.assertEquals(2, currencyById.size());
    }

    @Test
    @Order(4)
    void testGetCurrencyByDateShouldThrowElementNotFoundException() {
        Assertions.assertThrows(ElementNotFoundException.class,
                () -> conversionService.getTransactionByDate(
                        Date.valueOf(LocalDate.of(1994, 11, 20)).toString(), 0, 10));
    }

    private static CurrencyTransactionDTO getMockedCurrencyTransactionDTO(double amount) {
        CurrencyTransactionDTO transaction = new CurrencyTransactionDTO();
        transaction.setSourceCurrency("EUR");
        transaction.setTargetCurrency("BGN");
        transaction.setAmount(amount);
        return transaction;
    }

    private static CurrencyLayerDTO getMockedCurrencyLayerDTO() {
        CurrencyLayerDTO mockedCurrencyLayerDTO = new CurrencyLayerDTO();
        mockedCurrencyLayerDTO.setQuotes(new HashMap<>() {{
            put("USDEUR", 0.863765);
            put("USDBGN", 1.68919);
            put("USDUSD", 1.0);
            put("USDGBP", 0.743145);
        }});
        return mockedCurrencyLayerDTO;
    }

}
