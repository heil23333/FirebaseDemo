package fsc.com.firebasedemo.bean;

public class Channel {
    private int channelId;
    private String channelName;
    private String channelCreatorId;
    private long channelCreateTime;

    private String lastMsgContent;

    public Channel() {}

    public Channel(int channelId, String channelName, String channelCreatorId, long channelCreateTime, String lastMsgContent) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelCreatorId = channelCreatorId;
        this.channelCreateTime = channelCreateTime;
        this.lastMsgContent = lastMsgContent;
    }

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public long getChannelCreateTime() {
        return channelCreateTime;
    }

    public void setChannelCreateTime(long channelCreateTime) {
        this.channelCreateTime = channelCreateTime;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCreatorId() {
        return channelCreatorId;
    }

    public void setChannelCreatorId(String channelCreatorId) {
        this.channelCreatorId = channelCreatorId;
    }
}
