package com.example.projecthomework;

import java.util.List;

public class PlacesFilter {
    private String name;
    private String description;
    private Double rating;
    private String city;
    private String town;
    private String category;
    private List<String> keywords;
    private Double latitude;
    private Double longitude;

    public PlacesFilter() {}

    public PlacesFilter(String name, String description, Double rating, String city, String town, String category, List<String> keywords, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.city = city;
        this.town = town;
        this.category = category;
        this.keywords = keywords;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getRating() {
        return rating;
    }

    public String getCity() {
        return city;
    }

    public String getTown() {
        return town;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }


}

