package com.lab.skb

class Country {

    static mapping = {
        id generator: 'identity'
        version false
    }

    static constraints = {
        code matches: "[A-Z]{2}", nullable: false, unique: true
        name blank: false, nullable: false
    }

    private String code
    private String name

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
