package com.example.projecthomework;

import java.io.Serializable;

public class Comment implements Serializable {
    public String comment;

    public Comment(String comment) {
        this.comment = comment;
    }
}
