package domain.fileMaintenance.ptNotifLtrConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PtNotificationLetterConfig
{
    private int seqId;
    private String letterId;
    private boolean isDeleted;
    private boolean isDirty;
    private final List<Link> links = new ArrayList<>();

    public int getSeqId()
    {
        return seqId;
    }

    public void setSeqId(int seqId)
    {
        this.seqId = seqId;
    }

    public String getLetterId()
    {
        return letterId;
    }

    public void setLetterId(String letterId)
    {
        this.letterId = letterId;
    }

    public boolean isDeleted()
    {
        return isDeleted;
    }

    public void setDeleted(boolean deleted)
    {
        isDeleted = deleted;
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public void setDirty(boolean dirty)
    {
        isDirty = dirty;
    }

    public List<Link> getLinks()
    {
        return links;
    }

    public void addLink(int type, String id)
    {
        this.getLinks().add(new Link(type, id));
    }

    public Key getKey()
    {
        return new Key(links);
    }

    public static class Link
    {
        private final int type;
        private final String id;

        public Link(int linkType, String id)
        {
            this.type = linkType;
            this.id = id;
        }

        public int getType()
        {
            return type;
        }

        public String getId()
        {
            return id;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Link link = (Link) o;
            return getType() == link.getType() && Objects.equals(getId(), link.getId());
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(getType(), getId());
        }
    }

    public class Key
    {
        private Set<Link> linkSet = new HashSet<>();

        Key(List<Link> links)
        {
            this.linkSet.addAll(links);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(linkSet, key.linkSet);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(linkSet);
        }
    }
}
