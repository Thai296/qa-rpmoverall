package com.overall.externalInterfaces.mgl;

import org.apache.commons.lang3.StringUtils;

public class MglChargeFileNameField extends MglChargeFileField
{
    private final String firstName;
    private final String lastName;
    private final String middleName;

    public MglChargeFileNameField(int length, String firstName, String lastName, String middleName)
    {
        super(length);
        this.firstName = StringUtils.defaultString(firstName);
        this.lastName = StringUtils.defaultString(lastName);
        this.middleName = StringUtils.defaultString(middleName);
    }

    @Override
    public String getData()
    {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(lastName) || StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(middleName))
        {
            sb.append(lastName).append(",").append(firstName);
            if (StringUtils.isNotBlank(middleName))
            {
                sb.append(" ").append(middleName.substring(0, 1));
            }
        }
        return sb.toString();
    }
}
