package com.example.final_project_semb;

import java.util.ArrayList;

public class Reply {
    int replyAmount;
    ArrayList<Post> posts;

    public Reply() {
        this.replyAmount=0;
        posts=new ArrayList<>();
    }

    public int getReplyAmount() {
        return replyAmount;
    }

    public void increaseReplyAmount() {
        this.replyAmount++;
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
