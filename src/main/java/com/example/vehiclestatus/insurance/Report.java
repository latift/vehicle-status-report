package com.example.vehiclestatus.insurance;

public class Report {
    private int claims;

    public Report() {
    }

    public Report(int claims) {
        this.claims = claims;
    }

    public int getClaims() {
        return claims;
    }

    public void setClaims(int claims) {
        this.claims = claims;
    }
}
