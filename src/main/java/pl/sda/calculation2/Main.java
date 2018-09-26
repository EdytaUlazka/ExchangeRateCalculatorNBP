package pl.sda.calculation2;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        DecimalFormat df = new DecimalFormat("#.##");//Musi być #, a nie inny String
        ExchangeRates[] er = downloadAverageExchangeRate();


        ExchangeSingleRate[] rates = er[0].rates;
        for (int i = 0; i < rates.length; i++) {
            if (rates[i].code.equals("EUR") || rates[i].code.equals("CHF") || rates[i].code.equals("USD") || rates[i].code.equals("GBP")) {
                System.out.println("Average exchange PLN rate to is  " + rates[i].code + "  " + rates[i].mid);
                System.out.println("100PLN is worth   " + df.format(100 / rates[i].mid) + rates[i].code);
            }
        }

        SellingRates[] sr = downloadSellingRate();

        SellingSingleRate[] sellingRates = sr[0].rates;
        for (int i = 0; i < sellingRates.length; i++) {
            if (sellingRates[i].code.equals("EUR") || sellingRates[i].code.equals("CHF") || sellingRates[i].code.equals("USD") || sellingRates[i].code.equals("GBP")) {
                System.out.println("Average selling PLN rate  is  " + sellingRates[i].code + "  " + sellingRates[i].bid);
                System.out.println("100PLN (in selling rate) is worth   " + df.format(100 / sellingRates[i].bid) + sellingRates[i].code);
            }
        }
        SellingRates[] sr30D = downloadSellingRate30D();

        SellingSingleRate[] sellingRates30D = sr30D[0].rates;
        for (int i = 0; i < sellingRates30D.length; i++) {
            if (sellingRates30D[i].code.equals("EUR") || sellingRates30D[i].code.equals("CHF") || sellingRates30D[i].code.equals("USD") || sellingRates30D[i].code.equals("GBP")) {
                System.out.println("Average selling PLN rate 30 days ago was  " + sellingRates30D[i].code + "  " + sellingRates30D[i].bid);
                System.out.println("100PLN (in selling rate) was worth   " + df.format(100 / sellingRates30D[i].bid) + sellingRates30D[i].code);
                System.out.println("It's worth today " + df.format(100 / sellingRates30D[i].bid * sellingRates[i].ask) + " PLN");
                System.out.println("Profit could be equal " + df.format((100 / sellingRates30D[i].bid) * sellingRates[i].ask - 100) + " PLN");
            }
        }

    }

    private static SellingRates[] downloadSellingRate30Days() throws IOException {

        String a = "http://api.nbp.pl/api/exchangerates/tables/c/2018-07-02/";

        return downloadData(a, SellingRates[].class);
    }

    private static ExchangeRates[] downloadAverageExchangeRate() throws IOException {
        String a = "http://api.nbp.pl/api/exchangerates/tables/a/";
        URLConnection connection = new URL(a).openConnection();
        HttpURLConnection httpConnection = null;

        try {
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
            connection.connect();
            Scanner sc = new Scanner(connection.getInputStream());
            String rate = sc.nextLine();//String z całym Jsonem
            Gson gson = new Gson();
            System.out.println(rate);


            return gson.fromJson(rate, ExchangeRates[].class);//informacja o pobraniu tabeli
        } finally {
            httpConnection = (HttpURLConnection) connection;
            httpConnection.disconnect();
        }
    }


    private static SellingRates[] downloadSellingRate() throws IOException {
        String a = "http://api.nbp.pl/api/exchangerates/tables/c/";
        URLConnection connection = new URL(a).openConnection();
        connection
                .setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        connection.connect();
        Scanner sc = new Scanner(connection.getInputStream());
        String rate = sc.nextLine();//String z całym Jsonem
        Gson gson = new Gson();
        System.out.println(rate);

        return gson.fromJson(rate, SellingRates[].class);//informacja o pobraniu tabeli
    }

    private static SellingRates[] downloadSellingRate30D() throws IOException {
        String a = "http://api.nbp.pl/api/exchangerates/tables/c/2018-07-02/";
        URLConnection connection = new URL(a).openConnection();
        connection
                .setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        connection.connect();
        Scanner sc = new Scanner(connection.getInputStream());
        String rate = sc.nextLine();//String z całym Jsonem
        Gson gson = new Gson();
        System.out.println(rate);

        return gson.fromJson(rate, SellingRates[].class);//informacja o pobraniu tabeli
    }


    private static <T> T downloadData(String a, Class<T> type) throws IOException {

        // a = "http://api.nbp.pl/api/exchangerates/tables/a/";
        URLConnection urlConnection = new URL(a).openConnection();
        HttpURLConnection httpConnection = null;

        try {
            urlConnection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
            urlConnection.connect();
            Scanner sc = new Scanner(urlConnection.getInputStream());
            String rate = sc.nextLine();//String z całym Jsonem
            Gson gson = new Gson();
            System.out.println(rate);


            return gson.fromJson(rate, type);//informacja o pobraniu tabeli
        } finally {
            httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.disconnect();

        }
    }



}