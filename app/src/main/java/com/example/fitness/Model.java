package com.example.fitness;

public class Model {

    String id, name, desc, url;
    public Model(){}

    public Model(String id, String name, String desc, String url) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUrl(String url){this.url = url;}

    public String getUrl() { return url;}
}
