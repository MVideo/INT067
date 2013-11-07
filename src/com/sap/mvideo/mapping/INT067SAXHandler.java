package com.sap.mvideo.mapping;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author rassakhatsky
 */
public class INT067SAXHandler extends DefaultHandler {

    /**
     * flagItem - current node is <Item>.
     * flagSKU - current node is <SKU>.
     * flagSTORE - current node is <STORE>.
     * flagDATE - current node is <DATE>.
     * flagSSTCK - current node is <SSTCK>.
     * flagFCST - current node is <FCST>.
     * flagPROMFL - current node is <PROMFL>.
     */
    private boolean flagItem, flagSKU, flagSTORE, flagDATE, flagSSTCK, flagFCST, flagPROMFL;

    /**
     * fileName - File name.
     * tagContent - current tag content.
     * type - current type of content.
     * valueSKU - <SKU> value for current record.
     * valueSTORE - <STORE> value for current record.
     * valueDATE - <DATE> value for current record.
     * valueSSTCK - <SSTCK> value for current record.
     * valueFCST - <FCST> value for current record.
     * valuePROMFL - <PROMFL> value for current record.
     */
    private String fileName, tagContent, type, valueSTORE, valueSKU, valueDATE, valueSSTCK, valueFCST, valuePROMFL;

    /**
     * Row's counter.
     */
    private int counter;

    /**
     * Result message.
     */
    private StringBuilder resultMessage;

    private String digest_type, data_type, tagSTORE_end, tagSTORE_begin, tagItem_end, tagItem_begin,
            tagSKU_end, tagSKU_begin, tagDATE_end, tagDATE_begin, tagSSTCK_end, tagSSTCK_begin,
            tagFCST_end, tagFCST_begin, tagPROMFL_end, tagPROMFL_begin, tagLINES_end, tagLINES_begin;

//The document begining
    @Override
    public void startDocument()
            throws SAXException {
        try {
            digest_type = "DIGEST";
            data_type = "DATA";
            tagSTORE_end = "</STORE>";
            tagSTORE_begin = "<STORE>";
            tagItem_end = "</Item>";
            tagItem_begin = "<Item>";
            tagSKU_begin = "<SKU>";
            tagSKU_end = "</SKU>";
            tagDATE_begin = "<DATE>";
            tagDATE_end = "</DATE>";
            tagSSTCK_begin = "<SSTCK>";
            tagSSTCK_end = "</SSTCK>";
            tagFCST_begin = "<FCST>";
            tagFCST_end = "</FCST>";
            tagPROMFL_begin = "<PROMFL>";
            tagPROMFL_end = "</PROMFL>";
            tagLINES_begin = "<LINES>";
            tagLINES_end = "</LINES>";

            flagItem = false;
            flagSKU = false;
            flagSTORE = false;
            flagDATE = false;
            flagSSTCK = false;
            flagFCST = false;
            flagPROMFL = false;
            counter = 0;
            type = digest_type;
            valueSTORE = "";
            resultMessage = new StringBuilder(250000);

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
        if (type.equalsIgnoreCase(data_type)) {
            //Not empty message
            resultMessage.append("</ExportData><ExportDidgest>").append(tagItem_begin).append(tagSTORE_begin);
            resultMessage.append(valueSTORE);
            resultMessage.append(tagSTORE_end).append("<LINES_F>").append(counter - 1).append("</LINES_F>").append(tagItem_end).append("</ExportDidgest>"); //stores quantity
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
        if (pqName.equalsIgnoreCase("Item")) {
            flagItem = true;
            counter++;

            /**
             * skip 1st line - it is the message header.
             */
            if (counter > 1) {
                //the message summary
                if (type.equalsIgnoreCase(digest_type)) {
                    resultMessage.append(tagItem_begin);
                }
                //the message line
                if (type.equalsIgnoreCase(data_type)) {
                    resultMessage.append("<Line>");
                }
            }
        }

        if (pqName.equalsIgnoreCase("SKU")) {
            flagSKU = true;
            valueSKU = "";
        } else if (pqName.equalsIgnoreCase("STORE")) {
            flagSTORE = true;
            valueSTORE = "";
        } else if (pqName.equalsIgnoreCase("DATE")) {
            flagDATE = true;
            valueDATE = "";
        } else if (pqName.equalsIgnoreCase("SSTCK")) {
            flagSSTCK = true;
            valueSSTCK = "";
        } else if (pqName.equalsIgnoreCase("FCST")) {
            flagFCST = true;
            valueFCST = "";
        } else if (pqName.equalsIgnoreCase("PROMFL")) {
            flagPROMFL = true;
            valuePROMFL = "";
        }
        tagContent = null;
    }

    //the element ending
    @Override
    public void endElement(String uri, String localName, String pqName) throws SAXException {
//Header - 1st line
        if (counter == 1) {
            if (flagFCST && tagContent.length() > 1) {
                type = data_type;
            }
            if (pqName.equalsIgnoreCase("Item")) {
                if (type.equalsIgnoreCase(data_type)) {
                    resultMessage.append("<ExportData>");
                } else {
                    resultMessage.append("<ExportData></ExportData><ExportDidgest>");
                }
            }
        }

        if (counter > 1) {
            if (pqName.equalsIgnoreCase("Item")) {
                if (type.equalsIgnoreCase(data_type)) {
                    resultMessage.append("</Line>");
                }
                if (type.equalsIgnoreCase(digest_type)) {
                    resultMessage.append(tagItem_end);
                }
            } else {
                if (type.equalsIgnoreCase(data_type)) {
                    if (flagSKU || flagSTORE || flagDATE || flagSSTCK || flagFCST) {

                        if (flagSKU) {
                            resultMessage.append(tagSKU_begin);
                            resultMessage.append(tagContent);
                            resultMessage.append(tagSKU_end);
                        } else if (flagSTORE && valueSTORE.length() == 0) {
                            resultMessage.append(tagSTORE_begin);
                            valueSTORE = tagContent;
                            resultMessage.append(tagContent);
                            resultMessage.append(tagSTORE_end);
                        } else if (flagDATE) {
                            resultMessage.append(tagDATE_begin);
                            resultMessage.append(tagContent);
                            resultMessage.append(tagDATE_end);
                        } else if (flagSSTCK) {
                            resultMessage.append(tagSSTCK_begin);
                            resultMessage.append(tagContent);
                            resultMessage.append(tagSSTCK_end);
                        } else if (flagFCST) {
                            resultMessage.append(tagFCST_begin);
                            resultMessage.append(tagContent);
                            resultMessage.append(tagFCST_end);
                        }

                    } else if (flagPROMFL) {
                        valuePROMFL = tagContent;
                        resultMessage.append(tagPROMFL_begin);
                        if (tagContent.length() > 0 && (tagContent.charAt(0) == '1' || tagContent.charAt(0) == '2')) {
                            resultMessage.append(tagContent.charAt(0));
                        } else {
                            resultMessage.append(' ');
                        }
                        resultMessage.append(tagPROMFL_end);

                    } else if (type.equalsIgnoreCase(digest_type)) {
                        if (flagSKU) {
                            resultMessage.append(tagSTORE_begin).append(tagContent).append(tagSTORE_end);
                        }
                        if (flagSTORE) {
                            resultMessage.append(tagLINES_begin).append(tagContent).append(tagLINES_end);
                        }
                    }
                }
            }
        }

        //change element to false
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
    public void characters(char character[], int start, int length) throws SAXException {
        for (int i = start; i < length; i++) {
            char ch;
            StringBuilder tempCharacters = new StringBuilder();
            
            ch = character[i];
            if (ch >= '0' && ch <= '9'
                    || ch >= 'A' && ch <= 'Z'
                    || ch >= 'a' && ch <= 'z'
                    || ch >= '\u0410' && ch <= '\u042F'
                    || ch >= '\u0430' && ch <= '\u044F'
                    || ch == '<' || ch == '>' || ch == '?'
                    || ch == '!' || ch == '@' || ch == '/'
                    || ch == '#' || ch == '$' || ch == '&'
                    || ch == '*' || ch == '(' || ch == ')'
                    || ch == '-' || ch == '_' || ch == '='
                    || ch == '[' || ch == ']' || ch == '{'
                    || ch == '}' || ch == '+' || ch == '.') {
                
                tempCharacters.append(ch);
            }
            character = tempCharacters.toString().toCharArray();
        }
        
        
        try {
            if (tagContent == null) {
                tagContent = new String(character, start, length);
            } else {
                tagContent += new String(character, start, length);
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    //return the result message
    public StringBuilder getOutput() {
        return resultMessage;
    }

    public void setFileName(String name) {
        fileName = name;
    }
}
