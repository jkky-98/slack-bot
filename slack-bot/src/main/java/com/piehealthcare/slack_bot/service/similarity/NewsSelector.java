package com.piehealthcare.slack_bot.service.similarity;

import com.piehealthcare.slack_bot.service.news.NaverNewsApiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.piehealthcare.slack_bot.service.similarity.JaccardSimilarity.calculate;

@Service
@Slf4j
public class NewsSelector {

    private static final double TITLE_WEIGHT = 0.7;  // 가중치: title
    private static final double DESCRIPTION_WEIGHT = 0.3;  // 가중치: description
    private static final double THRESHOLD = 0.1;  // 유사도 임계값

    //getTopNews 에서 사용되는 값
    private final List<NaverNewsApiDto> topNews = new ArrayList<>();
    private NaverNewsApiDto index = new NaverNewsApiDto();


    private double calculateWeightedSimilarity(NaverNewsApiDto news1, NaverNewsApiDto news2) {
        double titleSimilarity = calculate(news1.getTitle(), news2.getTitle());
        double descriptionSimilarity = calculate(news1.getDescription(), news2.getDescription());

        // 평균 가중치 유사도 계산
        double weightedSimilarity = (titleSimilarity * TITLE_WEIGHT) + (descriptionSimilarity * DESCRIPTION_WEIGHT);

        // 유사도 계산 로깅
//        log.info("Comparing News1: [{}] with News2: [{}]", news1.getTitle(), news2.getTitle());
//        log.info("Title Similarity: {}, Description Similarity: {}, Weighted Similarity: {}",
//                titleSimilarity, descriptionSimilarity, weightedSimilarity);

        return weightedSimilarity;
    }

    public List<NaverNewsApiDto> getTopNews(List<NaverNewsApiDto> newsList, int count) {

        if (newsList.isEmpty()) {
            throw new IllegalStateException("파싱된 뉴스가 없습니다. 네이버 API Parsing 기능 이상");
        }

        topNews.add(newsList.get(0));
        index = newsList.get(0);

        List<Integer> removeTmp = new ArrayList<>();

        filtering(newsList, count, removeTmp);


        return topNews;
    }

    private void filtering(List<NaverNewsApiDto> newsList, int count, List<Integer> removeTmp) {
        while (topNews.size() != count) {
            if (newsList.isEmpty()) {
                log.info("소스 부족");
                break;
            }

            List<Integer> canTopNewsIndex = new ArrayList<>();
            for (int i = 0; i < newsList.size(); i++) {
                double similarityValue = calculateWeightedSimilarity(index, newsList.get(i));
                if (similarityValue > THRESHOLD) {
                    removeTmp.add(i);
                } else {
                    canTopNewsIndex.add(i);
                }
            }
            Integer topNewsSourceIndex = canTopNewsIndex.get(0);
            NaverNewsApiDto topNewsSource = newsList.get(topNewsSourceIndex);
            topNews.add(topNewsSource);
            index = topNewsSource;

            newsList = removeIndices(newsList, removeTmp);
            removeTmp.clear();
        }
    }


    public static <T> List<T> removeIndices(List<T> list, List<Integer> indices) {
        return IntStream.range(0, list.size())
                .filter(i -> !indices.contains(i))
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

}
