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
        name = n == null || n.isBlank() ? "Unnamed" : n;
        age = a;
        description = d == null || d.isBlank() ? "No description" : d;
        species = s == null || s.isBlank() ? "Unknown" : s;
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

    public static String formatAge(Integer age) {
        return age == null ? "Unknown" : age.toString();
    }

    public String getDescription() {
        return description;
    }

    public String getSpecies() {
        return species;
    }
    public void setTags(List<Tag> t){
        tags = t == null ? new ArrayList<>() : t;
    }
    public String getTagsTextString() {
        String text = tags.stream().map(Tag::getTagName).collect(Collectors.joining(", "));
        return text.isBlank() ? "None" : text;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
