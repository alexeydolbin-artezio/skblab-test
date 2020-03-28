package com.lab.skb.dto

import com.lab.skb.Country

class CountryFilterResult {

    private List<Country> result
    private Long total

    List<Country> getResult() {
        return result
    }

    void setResult(List<Country> result) {
        this.result = result
    }

    Long getTotal() {
        return total
    }

    void setTotal(Long total) {
        this.total = total
    }

    @Override
    String toString() {
        return "CountryFilterResult{" +
                "result=" + result +
                ", total=" + total +
                '}';
    }
}
