package com.piehealthcare.slack_bot.service.news;

import com.piehealthcare.slack_bot.config.SlackChannelCostant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {
    @Value(value = "${slack.token}")
    String slackToken;

    private final RestTemplate restTemplate;

    private final String SLACK_CHANNEL = SlackChannelCostant.NEWS_CHANNEL;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSlackMessage(List<NaverNewsApiDto> newsList) {
        String url = "https://slack.com/api/chat.postMessage";

        // JSON 메시지 작성
        String payload = """
        {
          "channel": "%s",
          "text": "뉴스 업데이트입니다.",
          "blocks": [
            %s
          ]
        }
        """.formatted(SLACK_CHANNEL, generateBlocks(newsList));

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + slackToken);
        headers.set("Content-Type", "application/json");

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 결과 출력
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Slack 메시지 전송 성공: {}", response.getBody());
        } else {
            log.info("Slack 메시지 전송 실패: {}", response.getBody());
        }
    }

    private String generateBlocks(List<NaverNewsApiDto> newsList) {
        StringBuilder blocks = new StringBuilder();
        int count = 0;

        for (NaverNewsApiDto news : newsList) {
            if (count % 2 == 0 && count != 0) {
                blocks.append(", { \"type\": \"divider\" },");
            }

            blocks.append("""
                {
                  "type": "section",
                  "fields": [
                    {
                      "type": "mrkdwn",
                      "text": "*[%s](%s)*\\n%s"
                    }
                  ]
                }
            """.formatted(news.getTitle(), news.getOriginallink(), news.getDescription()));

            count++;
        }

        return blocks.toString();
    }

}
