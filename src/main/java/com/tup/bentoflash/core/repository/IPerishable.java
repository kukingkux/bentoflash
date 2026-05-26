package com.tup.bentoflash.core.repository;

public interface IPerishable {
    void applyEndOfDayDiscount();
    boolean isExpired();
}
