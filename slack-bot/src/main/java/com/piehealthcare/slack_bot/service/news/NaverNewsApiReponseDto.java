package com.piehealthcare.slack_bot.service.news;

import lombok.Data;

import java.util.List;

@Data
public class NaverNewsApiReponseDto {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverNewsApiDto> items;
}
