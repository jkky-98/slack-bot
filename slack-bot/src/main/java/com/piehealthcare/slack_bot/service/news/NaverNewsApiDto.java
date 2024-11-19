package com.piehealthcare.slack_bot.service.news;

import lombok.Data;

@Data
public class NaverNewsApiDto {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
}
