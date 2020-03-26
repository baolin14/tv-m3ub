package com.zhaojianyun.tvm3ub.crawler;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuptEduCrawler {

    private static String url = "http://ivi.bupt.edu.cn";

    public List<ImmutablePair<String, String>> get() throws Exception {
        String html = HttpUtil.get(url, 10000);
        Document document = Jsoup.parse(html);
        document.setBaseUri(url);
        Elements select = document.select("div.row  div.2u");
        return select.stream()
                .map(this::parseSource)
                .collect(Collectors.toList());
    }

    private ImmutablePair<String, String> parseSource(Element element) {
        String name = element.select("p").text();
        String m3u8Url = element.baseUri() + element.select("a").get(1).attr("href");
        return ImmutablePair.of(name, m3u8Url);
    }

    public static void main(String[] args) throws Exception {
        BuptEduCrawler buptEduCrawler = new BuptEduCrawler();
        List<ImmutablePair<String, String>> immutablePairs = buptEduCrawler.get();
        immutablePairs.forEach(System.out::println);

        StringBuilder stringBuilder = new StringBuilder("#EXTM3U\n");
        immutablePairs.forEach(pair -> {
            stringBuilder.append("#EXTINF:-1 ,").append(pair.left).append("\n");
            stringBuilder.append(pair.right).append("\n");
        });

        System.out.println(stringBuilder);
        Path path = Paths.get("release", "国内卫视.m3u");
        Files.write(path, stringBuilder.toString().getBytes());
    }
}
