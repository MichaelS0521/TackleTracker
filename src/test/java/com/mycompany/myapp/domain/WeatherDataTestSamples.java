package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WeatherDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WeatherData getWeatherDataSample1() {
        return new WeatherData().id(1L).condition("condition1");
    }

    public static WeatherData getWeatherDataSample2() {
        return new WeatherData().id(2L).condition("condition2");
    }

    public static WeatherData getWeatherDataRandomSampleGenerator() {
        return new WeatherData().id(longCount.incrementAndGet()).condition(UUID.randomUUID().toString());
    }
}
