package com.steven.Smartglass.XunFei;

/**
 * Created by Administrator on 2017/6/27.
 */

public class RecDeco {

    /**
     * sst : wakeup
     * score : 72
     * bos : 3480
     * eos : 4240
     * id : 0
     */

    private String sst;
    private int score;
    private int bos;
    private int eos;
    private int id;

    public String getSst() {
        return sst;
    }

    public void setSst(String sst) {
        this.sst = sst;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBos() {
        return bos;
    }

    public void setBos(int bos) {
        this.bos = bos;
    }

    public int getEos() {
        return eos;
    }

    public void setEos(int eos) {
        this.eos = eos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
