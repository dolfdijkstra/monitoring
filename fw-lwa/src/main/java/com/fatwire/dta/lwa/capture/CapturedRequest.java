package com.fatwire.dta.lwa.capture;

import java.util.UUID;

public interface CapturedRequest {
    UUID getUUID();

    long getTimestamp();
}
