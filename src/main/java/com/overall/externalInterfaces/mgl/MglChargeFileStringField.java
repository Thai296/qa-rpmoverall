package com.overall.externalInterfaces.mgl;

import org.apache.commons.lang3.StringUtils;

public class MglChargeFileStringField extends MglChargeFileField
{
    private final String data;

    public MglChargeFileStringField(int length, String data)
    {
        super(length);
        this.data = StringUtils.defaultString(data);
    }

    @Override
    public String getData()
    {
        return data;
    }
}
