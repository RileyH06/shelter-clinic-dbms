package com.shelterclinic.RepositoriesAndEntities;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Animal {
    private int animal_id;
    private String name;
    private Integer age;
    private String description;
    private String species;
    private List<Tag> tags = new ArrayList<>();

    public Animal(int id, String n, Integer a, String d, String s){
        animal_id = id;
        name = n;
        age = a;
        description = d;
        species = s;
    }

    public int getAnimalId(){
        return animal_id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecies() {
        return species;
    }
    public void setTags(List<Tag> t){
        tags = t;
    }
    public String getTagsTextString() {
        return tags.stream().map(Tag::getTagName).collect(Collectors.joining(", "));
    }

    public List<Tag> getTags() {
        return tags;
    }
}
