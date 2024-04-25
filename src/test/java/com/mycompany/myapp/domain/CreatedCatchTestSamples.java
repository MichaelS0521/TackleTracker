package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CreatedCatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CreatedCatch getCreatedCatchSample1() {
        return new CreatedCatch().id(1L).location("location1");
    }

    public static CreatedCatch getCreatedCatchSample2() {
        return new CreatedCatch().id(2L).location("location2");
    }

    public static CreatedCatch getCreatedCatchRandomSampleGenerator() {
        return new CreatedCatch().id(longCount.incrementAndGet()).location(UUID.randomUUID().toString());
    }
}
