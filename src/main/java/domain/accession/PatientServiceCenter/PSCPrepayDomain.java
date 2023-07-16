package domain.accession.PatientServiceCenter;

import com.xifin.utils.SeleniumBaseTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PSCPrepayDomain extends SeleniumBaseTest
{
    private String orderId;
    private String amount;

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

}