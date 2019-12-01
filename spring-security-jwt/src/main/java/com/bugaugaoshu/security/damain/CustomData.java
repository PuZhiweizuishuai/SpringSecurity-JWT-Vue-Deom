package com.bugaugaoshu.security.damain;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 19:05
 */
public class CustomData {
    private Integer id;
    private String data;

    public CustomData(Integer id, String data) {
        this.id = id;
        this.data = data;
    }

    public CustomData() {

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
