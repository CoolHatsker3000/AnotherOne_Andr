package com.tryone.dyplomtest1.views;

import java.util.LinkedList;
import java.util.List;

public class Ticket {
    public String name,description,id, uid;
    public int status,adminArea,locality;
    public List<String> imageUrls;
    public String address;
    public double latitude,longitude;

    public Ticket(String name, String description, String id, String uid, String address, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.uid=uid;
        this.status = 0;
        this.imageUrls = new LinkedList<>();
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Ticket(String name, String description, String id, int status, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.imageUrls = imageUrls;
    }

    public Ticket(String name, String description, String id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status=0;
        imageUrls=new LinkedList<>();
    }

    public Ticket(String name, String description, String id, int status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        imageUrls=new LinkedList<>();
    }

    public Ticket() {
        status=0;
        imageUrls=new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addUrl(String url){
        this.imageUrls.add(url);
    }
    public void clearUrls(){
        this.imageUrls.clear();
    }
}
