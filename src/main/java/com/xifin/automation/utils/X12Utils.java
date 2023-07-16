package com.xifin.automation.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class X12Utils
{
    private static final Set<String> CLEAN_SEGMENTS = new HashSet<>(java.util.Arrays.asList("ISA", "GS", "ST", "BHT", "HL", "SE", "GE", "IEA"));

    public static String clearSegments(String x12Data)
    {
        StringBuilder newString = new StringBuilder();

        String segmentDelimiter = String.valueOf(x12Data.charAt(105));
        System.out.println("segment delimiter: " + segmentDelimiter);

        String elementDelimiter = String.valueOf(x12Data.charAt(3));
        System.out.println("element delimiter: " + elementDelimiter);

        for (String segm : x12Data.split(Pattern.quote(segmentDelimiter)))
        {
            String[] elements = segm.split(Pattern.quote(elementDelimiter));
            if (!CLEAN_SEGMENTS.contains(elements[0]))
            {
                newString.append(segm);
                newString.append(segmentDelimiter);
            }
        }
        return newString.toString();
    }
}
