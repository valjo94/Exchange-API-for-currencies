package com.currency.foreignexchange;

import com.currency.foreignexchange.data.dto.external.CurrencyLayerDTO;
import com.currency.foreignexchange.services.CurrencyExchangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CurrencyExchangeServiceTest {

    CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void init() {
        CurrencyLayerDTO mockedCurrencyLayerDTO = getMockedCurrencyLayerDTO();

        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenReturn(mockedCurrencyLayerDTO);

        currencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);
    }

    @Test
    void testGetCurrencyExchangeRate() throws ServiceUnavailableException {
        double currencyExchangeRate = currencyExchangeService.getCurrencyExchangeRate("EUR", "BGN");

        double expectedExchangeRate = 1.95561292712717;
        Assertions.assertEquals(expectedExchangeRate, currencyExchangeRate);
    }

    @Test
    void testGetCurrencyExchangeRateShouldReturnZero() throws ServiceUnavailableException {
        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenReturn(null);
        currencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);
        double currencyExchangeRate = currencyExchangeService.getCurrencyExchangeRate("EUR", "BGN");

        Assertions.assertEquals(0, currencyExchangeRate);
    }

    @Test
    void testGetCurrencyExchangeRateWithWrongCurrency() {
        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenThrow(new RestClientException("error"));
        currencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);

        Assertions.assertThrows(ServiceUnavailableException.class,
                () -> currencyExchangeService.getCurrencyExchangeRate("EUR", "BGNN"));
    }

    @Test
    void testConvertCurrency() throws ServiceUnavailableException {
        double expectedResultRounded = Math.round(9.77806463563585 * 100.0) / 100.0;

        double realResult = currencyExchangeService.convertCurrency("EUR", 5.0, "BGN");
        double realResultRounded = Math.round(realResult * 100.0) / 100.0;

        Assertions.assertEquals(expectedResultRounded, realResultRounded);
    }

    @Test
    void testConvertCurrencyShouldReturnZero() throws ServiceUnavailableException {
        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenReturn(null);
        currencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);
        double realResult = currencyExchangeService.convertCurrency("EUR", 5.0, "BGN");

        Assertions.assertEquals(0, realResult);
    }

    @Test
    void testConvertCurrencyWithWrongCurrencyException() {
        RestTemplate mockedRestTemplate = Mockito.mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject(anyString(), any(Class.class))).thenThrow(new RestClientException("error"));
        currencyExchangeService = new CurrencyExchangeService(mockedRestTemplate);

        Assertions.assertThrows(ServiceUnavailableException.class,
                () -> currencyExchangeService.convertCurrency("EUR", 5.0, "BGNN"));
    }

    private CurrencyLayerDTO getMockedCurrencyLayerDTO() {
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
