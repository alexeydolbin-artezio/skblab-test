package com.lab.skb.dto

class Filter {

    private Integer offset
    private Integer limit
    private String searchTerm

    Integer getOffset() {
        return offset
    }

    void setOffset(Integer offset) {
        this.offset = offset
    }

    Integer getLimit() {
        return limit
    }

    void setLimit(Integer limit) {
        this.limit = limit
    }

    String getSearchTerm() {
        return searchTerm
    }

    void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm
    }

    @Override
    String toString() {
        return "Filter{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", searchTerm='" + searchTerm + '\'' +
                '}';
    }
}
