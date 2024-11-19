package com.piehealthcare.slack_bot.service;

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
public class UtilService {

    @Value(value = "${slack.token}")
    String slackToken;

    public void sendSlackMessage(String message, String channel) {
        String channelAddress = SlackChannelCostant.NEWS_CHANNEL;

        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelAddress)
                    .text(message)
                    .unfurlLinks(true)
                    .unfurlMedia(true)
                    .build();

            methods.chatPostMessage(request);

            log.info("Slack " + channel + " 에 메세지 보냄");
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }

    }
}
