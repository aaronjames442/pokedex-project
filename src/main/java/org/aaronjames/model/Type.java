package org.aaronjames.model;

import java.util.List;

public class Type {

    private int id;
    private String name;
    private List<String> strengths;
    private List<String> weaknesses;
    private String description;
    private String color;

    public Type(String name, List<String> strengths, List<String> weaknesses, String description, String color) {
        this.name = name;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.description = description;
        this.color = color;
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

    public List<String> getStrengths() {

        return strengths;
    }

    public void setStrengths(List<String> strengths) {

        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {

        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {

        this.weaknesses = weaknesses;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getColor() {

        return color;
    }

    public void setColor(String color) {

        this.color = color;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", strengths=" + strengths +
                ", weaknesses=" + weaknesses +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
