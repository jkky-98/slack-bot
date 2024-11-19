package com.piehealthcare.slack_bot.service.news;

import com.piehealthcare.slack_bot.service.similarity.NewsSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverAPI {

    private final NewsSelector newsSelector;

    public List<NaverNewsApiDto> fetchNews() {
        String url = "https://openapi.naver.com/v1/search/news.json?query=" +
                NaverConstant.searchName +
                "&display=" +
                NaverConstant.displayCount +
                "&start=1&sort=sim";

        // 헤더 설정
        HttpEntity<String> entity = getEntity();

        // API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverNewsApiReponseDto> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                entity,
                NaverNewsApiReponseDto.class
        );

        // 반환된 뉴스 리스트 반환
        List<NaverNewsApiDto> naverNewsApiDtos = response.getBody().getItems();

        log.info("Parsing news size : {}", naverNewsApiDtos.size());

        // 유사도 비교 로직
        List<NaverNewsApiDto> topNews = newsSelector.getTopNews(naverNewsApiDtos, 5);

        log.info("Filtering news size : {}", topNews.size());

        return topNews;

    }

    private static @NotNull HttpEntity<String> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", NaverConstant.XNaverClientId);
        headers.set("X-Naver-Client-Secret", NaverConstant.XNaverClientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return entity;
    }
}
