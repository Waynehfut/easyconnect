package com.waynehfut.Lz77;

/**
 * Created by Wayne on 2016/5/22.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MsgLzHelper {
    static private int getIntLenght(int num) {
        if (num < 10)
            return 1;
        if (num < 100)
            return 2;
        if (num < 1000)
            return 3;
        return 0;
    }

    public String compressLZ77(String buffer) {
        int current, pointer, lenght;
        char newChar;
        String tags, dictionary, strToCompress;
        current = pointer = lenght = 0;
        tags = dictionary = strToCompress = "";
        if (buffer != "") {
            try {
                //First case when we are in first char of string
                tags += "11";
                tags += "00";
                newChar = buffer.charAt(current);
                tags += newChar;
                current++;
                //all cases
                while (current < buffer.length()) {
                    dictionary += buffer.substring(0, current);
                    strToCompress += buffer.charAt(current);
                    //finding in dictionary
                    int i = 1;
                    if (dictionary.contains(strToCompress)) {
                        while ((current + i/*-1*/ < buffer.length()) && (dictionary.contains(strToCompress))) {
                            strToCompress += buffer.charAt(current + i);
                            i++;
                            if (current + i >= buffer.length())
                                i++;
                        }
                        if ((current + i < buffer.length()) && (strToCompress != ""))
                            newChar = strToCompress.charAt(strToCompress.length() - 1);
                        else
                            newChar = '|';
                        String tempFre = "";
                        if (strToCompress.length() > 1) {
                            if (current + i < buffer.length())
                                tempFre += strToCompress.substring(0, strToCompress.length() - 1);
                            else
                                tempFre += strToCompress.substring(0, strToCompress.length());
                            int freChar = dictionary.lastIndexOf(tempFre);
                            pointer = current - freChar;
                        } else {
                            int freChar = dictionary.lastIndexOf(strToCompress);
                            pointer = current - freChar;
                        }
                        lenght = i - 1;
                        current += i;
                        //insert tag
                        tags += getIntLenght(pointer);
                        tags += getIntLenght(lenght);
                        tags += pointer;
                        tags += lenght;
                        tags += newChar;
                    } else {
                        //The Char is not in dictionary
                        tags += "11";
                        tags += "00";
                        newChar = buffer.charAt(current);
                        tags += newChar;
                        current++;
                    }
                    //cases of last char
                    if (current == buffer.length() - 1) {
                        tags += "11";
                        tags += "00";
                        newChar = buffer.charAt(current);
                        tags += newChar;
                        return tags;
                    }
                    strToCompress = "";
                    dictionary = "";
                }
            } catch (Exception e) {

            }
        }
        return tags;
    }

    public String DecompressLZ77(String tags) {
        String buffer = "";
        char newChar;
        int pointer = 0, lenght = 0, pointerLen = 0, lenghtLen = 0;
        //First tag in tags
        int current = 0;
        try {
            while (current < tags.length()) {
                pointerLen = Integer.parseInt(tags.substring(current, current + 1));
                current++;
                String tete = tags.substring(current, current + 1);
                lenghtLen = Integer.parseInt(tags.substring(current, current + 1));
                current++;
                //String strToDec="";
                String pointerStr = "", lenghtStr = "";
                for (int j = current; j < current + pointerLen; j++) {
                    pointerStr += tags.charAt(j);
                }
                current += pointerLen;
                for (int j = current; j < current + lenghtLen; j++) {
                    lenghtStr += tags.charAt(j);
                }
                current += lenghtLen;
                //get tag
                pointer = Integer.parseInt(pointerStr);
                lenght = Integer.parseInt(lenghtStr);
                newChar = tags.charAt(current);
                current++;
                //decompress the tag
                if ((pointer == 0) && (lenght == 0)) {
                    buffer += newChar;
                } else {
                    int temp = buffer.length() - pointer;
                    String strToDecom = "";
                    strToDecom += buffer.substring(temp, temp + lenght);
                    buffer += strToDecom;
                    if (newChar != '|')
                        buffer += newChar;
                }
            }
        } catch (Exception e) {

        }
        return buffer;
    }
}
