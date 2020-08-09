package com.example.one17;

public class BlogPost {
    public String imageThumb, userId, imageUrl, desc;

    public BlogPost() {}

    public BlogPost(String imageThumb, String userId, String imageUrl, String desc) {
        this.imageThumb = imageThumb;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.desc = desc;
    }

    public String getImageThumb() {return imageThumb;}
    public String getUserId() {return userId;}
    public String getImageUrl() {return imageUrl;}
    public String getDesc() {return desc;}
}
