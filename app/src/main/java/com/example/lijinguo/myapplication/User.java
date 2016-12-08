package com.example.lijinguo.myapplication;

/**
 * Created by lijinguo on 11/9/16.
 */

public class User {


        private long id;
        private String username;
        private String password;
        private int score;
        private int hints;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getHints() {
            return hints;
        }

        public void setHints(int hints) {
            this.hints = hints;
        }




}
