package com.overall.externalInterfaces.mgl;

public abstract class MglChargeFileField
{
    private final int length;

    public MglChargeFileField(int length)
    {
        this.length = length;
    }

    protected abstract String getData();

    @Override
    public String toString()
    {
        return String.format("%-"+length+"."+length+"s", getData());
    }
}
