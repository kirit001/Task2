package com.example.checklist.RestApi;

import java.util.ArrayList;

public class Model {

    private final int page;
    private final int per_page;
    private final int total;
    private final int total_pages;
    ArrayList<Data> data;


    public Model(int page, int per_page, int total, int total_pages, ArrayList<Data> data) {
        this.page = page;
        this.per_page = per_page;
        this.total = total;
        this.total_pages = total_pages;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public int getPer_page() {
        return per_page;
    }

    public int getTotal() {
        return total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public class Data {

        private final int id;
        private final String email;
        private final String first_name;
        private final String last_name;
        private final String avatar;

        public Data(int id, String email, String first_name, String last_name, String avatar) {
            this.id = id;
            this.email = email;
            this.first_name = first_name;
            this.last_name = last_name;
            this.avatar = avatar;
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
