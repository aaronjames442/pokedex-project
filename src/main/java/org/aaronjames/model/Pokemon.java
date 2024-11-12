package org.aaronjames.model;

import java.util.List;
import java.util.Map;

public class Pokemon {
    private int id;
    private String name;
    private String description;
    private String height;
    private String weight;
    private  List<String> gender;

    public Pokemon(int id, String name, String description, String height, String weight, List<String> gender) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.gender = gender;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {

        return weight;
    }

    public void setWeight(String weight) {

        this.weight = weight;
    }

    public List<String> getGender() {

        return gender;
    }

    public void setGender(List<String> gender) {

        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", gender=" + gender +
                '}';
    }
}
