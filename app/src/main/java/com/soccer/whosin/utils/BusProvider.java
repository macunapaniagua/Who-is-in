package com.soccer.whosin.utils;

import com.squareup.otto.Bus;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class BusProvider {

    private static Bus mBus;

    private BusProvider() {

    }

    public static Bus getBus() {
        if (mBus == null)
            mBus = new Bus();
        return mBus;
    }
}
