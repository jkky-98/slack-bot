package com.piehealthcare.slack_bot.service.news;


import com.piehealthcare.slack_bot.config.SlackChannelCostant;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class NewsService {
    @Value(value = "${slack.token}")
    String slackToken;

    public void newsViewNaver(NaverNewsApiDto naverNewsApiDto) {

    }
}
