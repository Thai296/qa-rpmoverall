package domain.accession.PatientServiceCenter.patientestimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderInformation
{
    private String orderId;
    private String clientId;
    private String dos;
    private final List<String> testIds = new ArrayList<>();

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getDos()
    {
        return dos;
    }

    public void setDos(String dos)
    {
        this.dos = dos;
    }

    public List<String> getTestIds()
    {
        return testIds;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderInformation that = (OrderInformation) o;

        if (!Objects.equals(orderId, that.orderId)) return false;
        if (!Objects.equals(clientId, that.clientId)) return false;
        if (!Objects.equals(dos, that.dos)) return false;
        return testIds.equals(that.testIds);
    }

    @Override
    public int hashCode()
    {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        result = 31 * result + (dos != null ? dos.hashCode() : 0);
        result = 31 * result + testIds.hashCode();
        return result;
    }
}
