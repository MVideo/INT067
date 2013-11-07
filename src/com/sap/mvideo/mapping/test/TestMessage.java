package com.sap.mvideo.mapping.test;

import com.sap.aii.mapping.api.InputPayload;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

/**
 *
 * @author rassakhatsky
 */
public class TestMessage {

    //For test - inbound flow
    public TransformationInput buildTransformationInput(String fileName) {
        MyTransformationInput ti = new MyTransformationInput();
        ti.setFileName(fileName);
        //InputPayload inputPayload = ti.getInputPayload();
        return ti;
    }

    //For test - outbound flow
    public TransformationOutput buildTransformationOutput(String fileName) {
        MyTransformationOutput to = new MyTransformationOutput();
        to.setFileName(fileName);
        return to;
    }
}
