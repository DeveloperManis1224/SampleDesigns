package com.app.adinn.outdoors.square_brace.adinn_outdoors.Bus;

import com.squareup.otto.Bus;

public class BusProvider {

    private static Bus bus;


    public static Bus getInstance()
    {


        return  bus =new Bus();
    }
}
