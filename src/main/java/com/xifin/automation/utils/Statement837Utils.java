package com.xifin.automation.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Statement837Utils
{
    private static final Set<String> CLEAN_SEGMENTS = new HashSet<>(java.util.Arrays.asList("ISA","GS","ST","BHT","NM1","PER","REF","OI","HL","N3","N4", "SBR","SE","GE","IEA"));

    public static String clearSegments(String statementData)
    {
        StringBuilder newString = new StringBuilder();

        String segmentDelimiter = String.valueOf(statementData.charAt(105));
        System.out.println("segment delimiter: " + segmentDelimiter);

        String elementDelimiter = String.valueOf(statementData.charAt(3));
        System.out.println("element delimiter: " + elementDelimiter);

        for (String segm : statementData.split(Pattern.quote(segmentDelimiter)))
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
