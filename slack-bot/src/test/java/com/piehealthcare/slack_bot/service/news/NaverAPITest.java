package com.piehealthcare.slack_bot.service.news;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class NaverAPITest {

    @Autowired NaverAPI naverAPI;

    @Test
    @DisplayName("네이버 뉴스 탑 5 FETCH")
    void fetchNaverNews50() {
        List<NaverNewsApiDto> naverNewsApiDtos = naverAPI.fetchNews();
        for (NaverNewsApiDto naverNewsApiDto : naverNewsApiDtos) {
            log.info("NEWS : {}", naverNewsApiDto);
        }
        assertThat(naverNewsApiDtos.size()).isEqualTo(5);
    }

    @Test
    void fetchNewsTime() {
        long startTime = System.currentTimeMillis();
        List<NaverNewsApiDto> naverNewsApiDtos = naverAPI.fetchNews();
        long endTime = System.currentTimeMillis();
        log.info("파싱 & 필터링 시간 경과 : {}ms", endTime - startTime);
    }
}