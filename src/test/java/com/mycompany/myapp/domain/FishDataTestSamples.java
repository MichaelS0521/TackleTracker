package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FishDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FishData getFishDataSample1() {
        return new FishData().id(1L).name("name1").waterType("waterType1");
    }

    public static FishData getFishDataSample2() {
        return new FishData().id(2L).name("name2").waterType("waterType2");
    }

    public static FishData getFishDataRandomSampleGenerator() {
        return new FishData().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).waterType(UUID.randomUUID().toString());
    }
}
