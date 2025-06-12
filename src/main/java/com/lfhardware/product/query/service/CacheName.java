package com.lfhardware.product.query.service;

import lombok.Getter;

@Getter
public enum CacheName {

    PRODUCT("product");

    private final String name;

    CacheName(String name) {
        this.name = name;
    }
}
