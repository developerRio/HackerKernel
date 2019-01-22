package com.originalstocks.demotestone;

public class Profile {

    private String status;
    private String file;
    private String pitch;
    private String lips;
    private String maleConfidence;
    private int pos;

    public Profile(int pos) {
        this.pos = pos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getLips() {
        return lips;
    }

    public void setLips(String lips) {
        this.lips = lips;
    }

    public String getMaleConfidence() {
        return maleConfidence;
    }

    public void setMaleConfidence(String maleConfidence) {
        this.maleConfidence = maleConfidence;
    }
}
