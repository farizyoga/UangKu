package com.example.farizsyahputra.uangku.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MoneyTraffic extends RealmObject {

    @PrimaryKey
    private Long ID;
    private Integer Amount;
    private String Type;
    private String Description;
    private Long CreatedAt;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Long createdAt) {
        CreatedAt = createdAt;
    }
}