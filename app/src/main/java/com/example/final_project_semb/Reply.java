package com.example.final_project_semb;

import java.util.ArrayList;

public class Reply {
    int replyAmount;


    public Reply() {
        this.replyAmount=0;

    }

    public Reply(int replyAmount) {
        this.replyAmount=replyAmount;
    }


    public int getReplyAmount() {
        return replyAmount;
    }

    public void increaseReplyAmount() {
        this.replyAmount++;
    }



}
