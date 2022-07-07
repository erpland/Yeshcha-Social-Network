package com.example.final_project_semb;

import java.util.ArrayList;

public class Requests {
    int requestAmount;
    ArrayList<Post> posts;

    public Requests() {
        this.requestAmount = 0;
        posts = new ArrayList<>();
    }

    public Requests(int requestAmount, ArrayList<Post> postsParam) {
        this.requestAmount = requestAmount;
        for (int i = 0; i < postsParam.size(); i++) {
            addPosts(postsParam.get(i));
        }
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void increaseRequestAmount() {
        this.requestAmount++;
    }

    public ArrayList<Post> getPosts() {
        return this.posts;
    }


    public void addPosts(Post pst) {
        if (pst != null) {
            this.posts.add(pst);
        } else {
            return;
        }
    }

    public void setPosts(ArrayList<Post> pst) {

        this.posts = pst;


    }
}
