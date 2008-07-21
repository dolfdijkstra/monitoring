package com.fatwire.dta.lwa.capture;

public interface RequestEncoder<T extends CapturedRequest, E> {

    E encode(T message);

}
