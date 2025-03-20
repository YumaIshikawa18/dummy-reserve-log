package com.yuzukiku.dummy_reserve_log.config;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class TimeConfig extends Clock {

    private final Instant baseInstant;
    private final Instant systemStartInstant;
    private final ZoneId zoneId;
    private final long speedFactor;

    public TimeConfig(Instant baseInstant, ZoneId zoneId, long speedFactor) {
        this.baseInstant = baseInstant;
        this.systemStartInstant = Instant.now();
        this.zoneId = zoneId;
        this.speedFactor = speedFactor;
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zoneId) {
        return new TimeConfig(baseInstant, zoneId, speedFactor);
    }

    @Override
    public Instant instant() {
        long realElapsedMillis = Duration.between(systemStartInstant, Instant.now()).toMillis();
        long acceleratedMillis = realElapsedMillis * speedFactor;
        return baseInstant.plusMillis(acceleratedMillis);
    }
}
