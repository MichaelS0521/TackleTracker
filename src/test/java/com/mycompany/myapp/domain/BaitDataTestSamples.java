package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BaitDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BaitData getBaitDataSample1() {
        return new BaitData().id(1L).type("type1").color("color1");
    }

    public static BaitData getBaitDataSample2() {
        return new BaitData().id(2L).type("type2").color("color2");
    }

    public static BaitData getBaitDataRandomSampleGenerator() {
        return new BaitData().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString()).color(UUID.randomUUID().toString());
    }
}
