/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sap.mvideo.mapping;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author rassakhatsky
 */
public class INT067_MessageCleaner {

    private StringBuffer tmpBuf = new StringBuffer(32);;

    public InputStream messageCleaner(InputStream is) throws IOException {
        
        
        
        //convert InputStrean into StringBuffer
        //Create BufferedReader object
        BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
        StringBuffer arg = new StringBuffer();
        String line = null;
        
         //read file line by line
                while( (line = bReader.readLine()) != null){
                        arg.append(line);
                }
                
        tmpBuf.setLength(0);
        for (int i = 0; i < arg.length(); i++) {
            char ch = arg.charAt(i);

            /**
             * append only: 1 - number from '0' to '9' 2 - letters from 'A' to
             * 'Z' and 'a' to 'z' 3 - letters from 'А' to 'Я' and 'а' to 'я' 4 -
             * Standart unicode symbols
             */
            if (ch >= '0' && ch <= '9'
                    || ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'
                    || ch >= '\u0410' && ch <= '\u042F' || ch >= '\u0430' && ch <= '\u044F'
                    || ch == '<' || ch == '>' || ch == '?' || ch == '!' || ch == '@' || ch == '/'
                    || ch == '#' || ch == '$' || ch == '&' || ch == '*' || ch == '(' || ch == ')'
                    || ch == '-' || ch == '_' || ch == '=' || ch == '[' || ch == ']' || ch == '{'
                    || ch == '}' || ch == '+' || ch == '.') {
                tmpBuf.append(arg.charAt(i));
            }
        }
        byte[] result = tmpBuf.toString().getBytes();
        is = new ByteArrayInputStream(result);
        return is;
    }
}
