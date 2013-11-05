package com.sap.mvideo.mapping;

import com.sap.aii.mapping.api.*;
import com.sap.mvideo.mapping.test.TestMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import sun.misc.IOUtils;

/**
 *
 * @author rassakhatsky
 */
public class INT067_Mapping extends AbstractTransformation {

    private Map param;

    public static void main(String arg[]) throws FileNotFoundException, StreamTransformationException, IOException, ParserConfigurationException, SAXException {
        String folder = "/Users/rassakhatsky/Desktop/";
        String source = "xml1 copy.xml";
        String target = "Datac.xml";
        String target_old = "Data_old.xml";

        //test mapping
        INT067_Mapping mapping = new INT067_Mapping();
        TestMessage test = new TestMessage();
        TransformationInput ti = test.buildTransformationInput(folder + source);
        TransformationOutput to = test.buildTransformationOutput(folder + target);
        mapping.transform(ti, to);
    }

    @Override
    public void transform(TransformationInput ti, TransformationOutput to) throws StreamTransformationException {
        String MyFileName = "Noname";

        try {
            DynamicConfiguration dynamicconfiguration = (DynamicConfiguration) param.get("DynamicConfiguration");
            DynamicConfigurationKey key = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileName");
            MyFileName = dynamicconfiguration.get(key);
        } catch (Exception exception) {

        }
        try {
            InputStream is = ti.getInputPayload().getInputStream();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = factory.newSAXParser();
            INT067_SAXHandler handlerXI = new INT067_SAXHandler();
            parser.parse(is, handlerXI);
            StringBuffer sb_out = handlerXI.getOutput();
            OutputStream os = to.getOutputPayload().getOutputStream();
            os.write(sb_out.toString().getBytes("UTF-8"));
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(INT067_Mapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(INT067_Mapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(INT067_Mapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
