package com.piehealthcare.slack_bot.schedule;

import com.piehealthcare.slack_bot.config.SlackChannelCostant;
import com.piehealthcare.slack_bot.service.news.NaverAPI;
import com.piehealthcare.slack_bot.service.news.NaverNewsApiDto;
import com.piehealthcare.slack_bot.service.news.NewsService;
import com.piehealthcare.slack_bot.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SlackBatch {

    private final NewsService newsService;
    private final UtilService utilService;
    private final NaverAPI naverAPI;

    @Scheduled(cron = "0 0 9 * * *") // every 1 minutes
    public void batchTest() {
        utilService.sendSlackMessage("테스트 :: 9시 뉴스 알람", SlackChannelCostant.NEWS_CHANNEL);
        List<NaverNewsApiDto> response = naverAPI.fetchNews();

        for (NaverNewsApiDto naverNewsApiDto : response) {
            utilService.sendSlackMessage(naverNewsApiDto.getOriginallink(), SlackChannelCostant.NEWS_CHANNEL);
        }

    }
}
