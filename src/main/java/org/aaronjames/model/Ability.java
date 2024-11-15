package org.aaronjames.model;

public class Ability {

    private int id;
    private String name;
    private String type;
    private String description;
    private String effect;

    public Ability(int id, String name, String type, String description, String effect) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.effect = effect;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", effect='" + effect + '\'' +
                '}';
    }
}
