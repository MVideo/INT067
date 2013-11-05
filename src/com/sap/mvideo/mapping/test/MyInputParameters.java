package com.sap.mvideo.mapping.test;

import com.sap.aii.mapping.api.InputParameters;
import com.sap.aii.mapping.lookup.Channel;

/**
 *
 * @author rassakhatsky
 */
class MyInputParameters extends InputParameters {

    @Override
    public Object getValue(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getString(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Channel getChannel(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
