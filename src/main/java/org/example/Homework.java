package org.example;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;


public class Homework {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String JSON_HOLDER = "https://jsonplaceholder.typicode.com";
    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        User myUser = createDefaultUser();


        printResponse(
                postNewUser(JSON_HOLDER, myUser));


        myUser.setName("C. Bauch");
        printResponse(
                putUser(JSON_HOLDER, myUser));


        printResponse(
                deleteUser(JSON_HOLDER, myUser));


        printResponse(
                getAllUsers(JSON_HOLDER));


        printResponse(
                getUserById(JSON_HOLDER,5));


        printResponse(
                getUserByUsername(JSON_HOLDER,"Antonio Banders"));




        writeLastCommentsToAFile(JSON_HOLDER, 3);


        printAllOpenTasks(JSON_HOLDER, 3);

    }

    private static void printAllOpenTasks(String host, int userId) throws IOException,
            InterruptedException {
        ArrayList<Task> taskList = getListOfAllTasks(host, userId);

        for (Task t : taskList) {
            if (!t.isCompleted()) {
                System.out.println(t.getTitle() + "\n");
            }
        }
    }


    private static ArrayList<Task> getListOfAllTasks(String host, int userId) throws IOException,
            InterruptedException {
        return GSON.fromJson(
                getAllUsersTasks(host, userId).body(),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
    }


    private static HttpResponse<String> getAllUsersTasks(String host, int userId) throws IOException,
            InterruptedException {
        return getAll(host + "/users/" + userId + "/todos");
    }


    private static void writeLastCommentsToAFile(String host, int userId) throws IOException,
            InterruptedException {
        ArrayList<Comment> listOfLastComments = getListOfLastComments(host, userId);

        for (Comment comment : listOfLastComments) {
            System.out.println(comment.getId() + "   ---   " + comment.getName());
            System.out.println(comment.getBody() + "\n");
        }

        String fileName = "user-" + userId + "-post-" + getLastPostId(host, userId) + "-comments.json";
        File file = new File(fileName);
        file.createNewFile();

        try (FileWriter writer = new FileWriter(file)) {
            for (Comment comment : listOfLastComments) {
                writer.write(comment.getId() + "   ---   " + comment.getName() + "\n");
                writer.write(comment.getBody() + "\n\n");
            }
        }
    }

    private static HttpResponse<String> getAllCommentsForPost(String host, int postId) throws IOException,
            InterruptedException {
        return getAll(host + "/posts/" + postId + "/comments");

    }


    private static ArrayList<Post> getListOfPosts(String host, int userId) throws IOException,
            InterruptedException {
        return GSON.fromJson(
                getAllUsersPosts(host, userId).body(),
                new TypeToken<ArrayList<Post>>() {
                }.getType());
    }


    private static ArrayList<Comment> getListOfLastComments(String host, int userId) throws IOException,
            InterruptedException {
        return GSON.fromJson(
                getAllCommentsForPost(
                        host,
                        getLastPostId(host, userId)).body(),
                new TypeToken<ArrayList<Comment>>() {
                }.getType());
    }


    private static int getLastPostId(String host, int userId) throws IOException,
            InterruptedException {
        ArrayList<Post> postList = getListOfPosts(host, userId);
        return postList.get(postList.size() - 1).getId();
    }


    private static HttpResponse<String> getAllUsersPosts(String host, int userId) throws IOException,
            InterruptedException {
        return getAll(host + "/users/" + userId + "/posts");
    }


    private static HttpResponse<String> getUserByUsername(String host, String username) throws IOException,
            InterruptedException {
        return getAll(host + "/users?username=" + username);
    }


    private static HttpResponse<String> getUserById(String host, int id) throws IOException,
            InterruptedException {
        return getAll(host + "/users/" + id);
    }


    private static HttpResponse<String> getAllUsers(String host) throws IOException,
            InterruptedException {
        return getAll(host + "/users");
    }


    private static HttpResponse<String> getAll(String uri) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-type", "application/json; charset=UTF-8")
                .GET()
                .build();
        return CLIENT.send(requestGet, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> deleteUser(String host, User user) throws IOException,
            InterruptedException {
        String requestBody = GSON.toJson(user);

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(URI.create(host + "/posts/1"))
                .header("Content-type", "application/json; charset=UTF-8")
                .DELETE()
                .build();

        return CLIENT.send(requestDelete, HttpResponse.BodyHandlers.ofString());
    }


    private static HttpResponse<String> putUser(String host, User user) throws IOException,
            InterruptedException {
        String requestBody = GSON.toJson(user);

        HttpRequest requestPut = HttpRequest.newBuilder()
                .uri(URI.create(host + "/posts/1"))
                .header("Content-type", "application/json; charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return CLIENT.send(requestPut, HttpResponse.BodyHandlers.ofString());
    }


    private static HttpResponse<String> postNewUser(String host, User user) throws IOException,
            InterruptedException {
        String requestBody = GSON.toJson(user);

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(host + "/users"))
                .header("Content-type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return CLIENT.send(requestPost, HttpResponse.BodyHandlers.ofString());


    }


    private static void printResponse(HttpResponse<String> response) {
        System.out.println("\n" + response.toString());
        System.out.println("\n" + "response.body() = \n" + response.body());
        System.out.println("\n===============================================================");
    }


    private static User createDefaultUser() {
        User user = new User();
        user.setId(3);
        user.setName("Clementine Bauch");
        user.setEmail("Nathan@yesenia.net");
        user.setAddress(new Address("Fasta 69",
                "588",
                "Stockholm",
                "14030-2425",
                new Geo("-01.0000",
                        "-02.0000")));
        user.setPhone("3-8-0-67-550-0428");
        user.setWebsite("ramiro.info");
        user.setCompany(new Company(
                "Luci Lee",
                "It is normal",
                "e-enable strategic"));
        return user;
    }

}
