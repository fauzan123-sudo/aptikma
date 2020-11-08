package com.example.aptikma;

public class ExampleItem {
    private String mCreator;
    private String mLikes;
    private String mPotongan;
    public ExampleItem(String creator, String likes, String potongan) {
        mCreator = creator;
        mLikes = likes;
        mPotongan = potongan;
    }

    public String getCreator() {
        return mCreator;
    }
    public String getLikeCount() {
        return mLikes;
    }

    public String getPotongan() {
        return mPotongan;
    }
}