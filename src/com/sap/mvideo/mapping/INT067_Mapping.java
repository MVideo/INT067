package com.sap.mvideo.mapping;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;
import com.sap.mvideo.mapping.test.TestMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author rassakhatsky
 */
public class INT067_Mapping extends AbstractTransformation {

    private Map param;

    public static void main(String arg[]) throws FileNotFoundException, StreamTransformationException, IOException, ParserConfigurationException, SAXException {
        String folder, source, target;
        folder = "/Users/rassakhatsky/Desktop/";
        source = "xml1.xml";
        target = "Data.xml";

        //test mapping
        INT067_Mapping mapping;
        TestMessage test;
        TransformationInput tis;
        TransformationOutput tos;
        mapping = new INT067_Mapping();
        test = new TestMessage();
        tis = test.buildTransformationInput(folder + source);
        tos = test.buildTransformationOutput(folder + target);
        mapping.transform(tis, tos);
    }

    @Override
    public void transform(TransformationInput ti, TransformationOutput to) throws StreamTransformationException {
        String MyFileName;
        MyFileName = "Noname";

        try {
            DynamicConfiguration dynamicconfiguration = (DynamicConfiguration) param.get("DynamicConfiguration");
            DynamicConfigurationKey key = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileName");
            MyFileName = dynamicconfiguration.get(key);
        } catch (Exception ex) {
        }
        try {
            InputStream is;
            SAXParserFactory factory;
            is = ti.getInputPayload().getInputStream();
            factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = factory.newSAXParser();
            INT067SAXHandler handlerXI = new INT067SAXHandler();
            parser.parse(is, handlerXI);
            StringBuilder sb_out = handlerXI.getOutput();
            OutputStream os = to.getOutputPayload().getOutputStream();
            os.write(sb_out.toString().getBytes("UTF-8"));

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(INT067_Mapping.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
