package com.sap.mvideo.mapping;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author rassakhatsky
 */
public class INT067_SAXHandler extends DefaultHandler {

    boolean flagTargetStock;
    boolean flagItem;
    boolean flagSKU;
    boolean flagSTORE;
    boolean flagDATE;
    boolean flagSSTCK;
    boolean flagFCST;
    boolean flagPROMFL;
    public StringBuffer resultMessage; //result message
    String fileName;
    String tagContent;
    String type;
    String STORE;
    String SKU;
    String DATE;
    String SSTCK;
    String FCST;
    String PROMFL;

    int counter;

    //The document begining
    @Override
    public void startDocument()
            throws SAXException {
        try {
            flagTargetStock = false;
            flagItem = false;
            flagSKU = false;
            flagSTORE = false;
            flagDATE = false;
            flagSSTCK = false;
            flagFCST = false;
            flagPROMFL = false;
            counter = 0;
            type = "DIGEST";
            tagContent = null;
            STORE = "";
            resultMessage = new StringBuffer(250000);

            //Message Header
            resultMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            resultMessage.append("<ns:MT_TargetStock_In xmlns:ns=\"http://mvideo.ru/PREDICTIX/TargetStock\">");

            //Add file name into the message
            resultMessage.append("<Header>").append(fileName).append("</Header>");
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    //The document ending
    @Override
    public void endDocument()
            throws SAXException {
        if (type.equalsIgnoreCase("DATA")) {
            //Not empty message
            resultMessage.append("</ExportData><ExportDidgest><Item><STORE>");
            resultMessage.append(STORE);
            resultMessage.append("</STORE><LINES_F>").append(counter - 1).append("</LINES_F></Item></ExportDidgest>"); //stores quantity
            resultMessage.append("</ns:MT_TargetStock_In>");
        } else {
            //Empty message
            resultMessage.append("</ExportDidgest></ns:MT_TargetStock_In>");
        }
    }

    //the element begining
    @Override
    public void startElement(String uri, String localName,
            String pqName, Attributes attributes)
            throws SAXException {

        //change element to false
        if (pqName.indexOf("TargetStock") != -1) {
            flagTargetStock = true; //root element of the message
        }

        if (pqName.equalsIgnoreCase("Item")) {
            flagItem = true;
            counter++;
            if (counter > 1) {//skip 1st line - it is the message header
                //the message summary
                if (type.equalsIgnoreCase("DIGEST")) {
                    resultMessage.append("<Item>");
                }
                //the message line
                if (type.equalsIgnoreCase("DATA")) {
                    resultMessage.append("<Line>");
                }
            }
        }

        if (pqName.equalsIgnoreCase("SKU")) {
            flagSKU = true;
            SKU = "";
        } else if (pqName.equalsIgnoreCase("STORE")) {
            flagSTORE = true;
            STORE = "";
        } else if (pqName.equalsIgnoreCase("DATE")) {
            flagDATE = true;
            DATE = "";
        } else if (pqName.equalsIgnoreCase("SSTCK")) {
            flagSSTCK = true;
            SSTCK = "";
        } else if (pqName.equalsIgnoreCase("FCST")) {
            flagFCST = true;
            FCST = "";
        } else if (pqName.equalsIgnoreCase("PROMFL")) {
            flagPROMFL = true;
            PROMFL = "";
        }
        tagContent = null;
    }

    //the element ending
    @Override
    public void endElement(String uri, String localName, String pqName) throws SAXException {
//Header - 1st line
        if (counter == 1) {
            if (flagFCST && tagContent.length() > 1) {
                type = "DATA";
            }
            if (pqName.equalsIgnoreCase("Item")) {
                if (type.equalsIgnoreCase("DATA")) {
                    resultMessage.append("<ExportData>");
                } else {
                    resultMessage.append("<ExportData></ExportData><ExportDidgest>");
                }
            }
        }

        if (counter > 1) {
            if (pqName.equalsIgnoreCase("Item")) {
                if (type.equalsIgnoreCase("DATA")) {
                    resultMessage.append("</Line>");
                }
                if (type.equalsIgnoreCase("DIGEST")) {
                    resultMessage.append("</Item>");
                }
            } else {
                if (type.equalsIgnoreCase("DATA")) {
                    if (flagSKU || flagSTORE || flagDATE || flagSSTCK || flagFCST) {

                        if (flagSKU) {
                            resultMessage.append("<SKU>");
                            resultMessage.append(tagContent);
                            resultMessage.append("</SKU>");
                        } else if (flagSTORE && STORE.length() == 0) {
                            resultMessage.append("<STORE>");
                            STORE = tagContent;
                            resultMessage.append(tagContent);
                            resultMessage.append("</STORE>");
                        } else if (flagDATE) {
                            resultMessage.append("<DATE>");
                            resultMessage.append(tagContent);
                            resultMessage.append("</DATE>");
                        } else if (flagSSTCK) {
                            resultMessage.append("<SSTCK>");
                            resultMessage.append(tagContent);
                            resultMessage.append("</SSTCK>");
                        } else if (flagFCST) {
                            resultMessage.append("<FCST>");
                            resultMessage.append(tagContent);
                            resultMessage.append("</FCST>");
                        }

                    } else if (flagPROMFL) {
                        PROMFL = tagContent;
                        resultMessage.append("<PROMFL>");
                        if (tagContent.length() > 0 && (tagContent.charAt(0) == '1' || tagContent.charAt(0) == '2')) {
                            resultMessage.append(tagContent.charAt(0));
                        } else {
                            resultMessage.append(' ');
                        }
                        resultMessage.append("</PROMFL>");

                    } else if (type.equalsIgnoreCase("DIGEST")) {
                        if (flagSKU) {
                            resultMessage.append("<STORE>").append(tagContent).append("</STORE>");
                        }
                        if (flagSTORE) {
                            resultMessage.append("<LINES>" + tagContent + "</LINES>");
                        }
                    }
                }
            }
        }

        //change element to false
        if (pqName.equalsIgnoreCase("TargetStock")) {
            flagTargetStock = false;
        }
        if (pqName.equalsIgnoreCase("Item")) {
            flagItem = false;
        }
        if (pqName.equalsIgnoreCase("SKU")) {
            flagSKU = false;
        } else if (pqName.equalsIgnoreCase("STORE")) {
            flagSTORE = false;
        } else if (pqName.equalsIgnoreCase("DATE")) {
            flagDATE = false;
        } else if (pqName.equalsIgnoreCase("SSTCK")) {
            flagSSTCK = false;
        } else if (pqName.equalsIgnoreCase("FCST")) {
            flagFCST = false;
        } else if (pqName.equalsIgnoreCase("PROMFL")) {
            flagPROMFL = false;
        }
    }

    //tag content
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        try {

            if (tagContent == null) {
                tagContent = new String(ch, start, length);
            } else {
                tagContent += new String(ch, start, length);
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    //return the result message
    public StringBuffer getOutput() {
        return resultMessage;
    }

    public void setFileName(String name) {
        fileName = name;
    }

}
