package com.dndev.signupfirbase;

import com.dndev.signupfirbase.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onFetchRecommendationsData(List<NewsHeadlines> list, String message);
    void onError(String message);
}
