package fsc.com.firebasedemo.bean;

public class Message {
    private String msgContent, msgFrom, msgFromId;
    private long msgSendTime;

    public Message() {}

    public Message(String msgContent, String msgFrom, long msgSendTime, String msgFromId) {
        this.msgContent = msgContent;
        this.msgFrom = msgFrom;
        this.msgSendTime = msgSendTime;
        this.msgFromId = msgFromId;
    }

    public String getMsgFromId() {
        return msgFromId;
    }

    public void setMsgFromId(String msgFromId) {
        this.msgFromId = msgFromId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public long getMsgSendTime() {
        return msgSendTime;
    }

    public void setMsgSendTime(long msgSendTime) {
        this.msgSendTime = msgSendTime;
    }
}
