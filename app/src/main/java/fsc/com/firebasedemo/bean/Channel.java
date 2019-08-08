package fsc.com.firebasedemo.bean;

public class Channel {
    private int channelId;
    private String channelName;
    private String channelCreatorId;

    public Channel() {}

    public Channel(int channelId, String channelName, String channelCreatorId) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelCreatorId = channelCreatorId;
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
