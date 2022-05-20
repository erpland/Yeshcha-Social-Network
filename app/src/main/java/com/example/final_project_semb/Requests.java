package com.example.final_project_semb;

import java.util.ArrayList;

public class Requests {
    int requestAmount;
    ArrayList<Post>posts;

    public Requests() {
        this.requestAmount=0;
        posts=new ArrayList<>();
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void increaseRequestAmount() {
        this.requestAmount++;
    }

    public ArrayList<Post> getPostList() {
        return this.posts;
    }

    public void SetPosts(Post pst) {
        if (pst!=null) {
            posts.add(pst);
        }
        else{
            return;
        }
    }
}
