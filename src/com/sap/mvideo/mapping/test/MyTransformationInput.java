package com.sap.mvideo.mapping.test;

import com.sap.aii.mapping.api.*;

/**
 *
 * @author rassakhatsky
 */
public class MyTransformationInput extends TransformationInput {

    private String filename;
    MyInputParameters inputParameters;

    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public InputHeader getInputHeader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputParameters getInputParameters() {
        return inputParameters;
    }

    @Override
    public InputPayload getInputPayload() {
        MyInputPayload inputPayload = new MyInputPayload();
        inputPayload.setFileName(filename);
        inputPayload.getInputStream();
        return inputPayload;
    }

    @Override
    public InputAttachments getInputAttachments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
