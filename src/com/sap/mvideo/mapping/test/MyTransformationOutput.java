package com.sap.mvideo.mapping.test;

import com.sap.aii.mapping.api.*;

/**
 *
 * @author rassakhatsky
 */
public class MyTransformationOutput extends TransformationOutput {

    private String filename;

    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public OutputHeader getOutputHeader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputParameters getOutputParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputPayload getOutputPayload() {
        MyOutputPayload outputPayload = new MyOutputPayload();
        outputPayload.setFileName(filename);
        return outputPayload;
    }

    @Override
    public OutputAttachments getOutputAttachments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyInputAttachments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
