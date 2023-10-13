package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}
