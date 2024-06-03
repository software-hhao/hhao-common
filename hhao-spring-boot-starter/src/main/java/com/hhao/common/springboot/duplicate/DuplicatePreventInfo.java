package com.hhao.common.springboot.duplicate;

public class DuplicatePreventInfo {
    private String uniqueKey;
    private String contentHash;
    private int expirationTime;
    private long timeStamp;

    public DuplicatePreventInfo(String uniqueKey, String contentHash, int expirationTime) {
        this.uniqueKey = uniqueKey;
        this.contentHash = contentHash;
        this.expirationTime = expirationTime;
        this.timeStamp=System.currentTimeMillis();
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
