package com.qa.account.accountapi.persistence.domain;

public class SentPrize {


    private Long prizeId;

    private int prizeAmount;

    private String time;

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public int getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(int prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SentPrize(){}

    public SentPrize(Prize prize){

        this.prizeId = prize.getId();
        this.prizeAmount = prize.getPrizeAmount();
        this.time = prize.getTime();
    }


}
