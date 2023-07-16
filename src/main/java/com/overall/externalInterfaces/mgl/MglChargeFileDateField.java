package com.overall.externalInterfaces.mgl;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MglChargeFileDateField extends MglChargeFileField
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy");

    private final Date date;

    public MglChargeFileDateField(int length, Date date)
    {
        super(length);
        this.date = date;
    }

    @Override
    public String getData()
    {
        return date != null ? DATE_FORMAT.format(new java.util.Date(date.getTime())) : StringUtils.EMPTY;
    }
}
