package com.jimune.perks;

public enum Constants {
    TAG_DURATION(0);
    private int constValue;
    Constants(int constValue) {
        this.constValue = constValue;
    }
    public int getConstValue() {
        return this.constValue;
    }
}
