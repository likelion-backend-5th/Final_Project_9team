package com.example.Final_Project_9team.service;

import com.example.Final_Project_9team.dto.*;
import com.example.Final_Project_9team.entity.item.Accommodation;
import com.example.Final_Project_9team.entity.item.Attraction;
import com.example.Final_Project_9team.entity.item.Item;
import com.example.Final_Project_9team.entity.item.Restaurant;
import com.example.Final_Project_9team.exception.CustomException;
import com.example.Final_Project_9team.exception.ErrorCode;
import com.example.Final_Project_9team.repository.AccommodationRepository;
import com.example.Final_Project_9team.repository.AttractionRepository;
import com.example.Final_Project_9team.repository.ItemRepository;
import com.example.Final_Project_9team.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemListService {
    private final ItemRepository itemRepository;
    private final AttractionRepository attractionRepository;
    private final AccommodationRepository accommodationRepository;
    private final RestaurantRepository restaurantRepository;

    private static final int PAGE_SIZE = 5;
    //TODO : 전체 리스트 조회, 페이징
    public Page<ItemPartResponseDto> readItemAllPaged(int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id"));
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return itemPage.map(ItemPartResponseDto::fromEntity);
    }
    //TODO : 시도별 아이템 조회 (서울, 경기, 인천, 부산, 등등)
    public Page<ItemPartResponseDto> readItemSidoPaged(int page, String sido, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id"));
        Page<Item> itemPage = itemRepository.findBySido(sido, pageable);

        return itemPage.map(ItemPartResponseDto::fromEntity);
    }
    //TODO : 시 + 구 별 아이템 조회
    public Page<ItemPartResponseDto> readItemSidoAndSigungu(int page, String sido, String sigungu) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id"));
        Page<Item> itemPage = itemRepository.findBySidoAndSigungu(sido, sigungu, pageable);

        return itemPage.map(ItemPartResponseDto::fromEntity);
    }
    //TODO: 상세정보 조회
//    public ResponseEntity<?> readItem(Long itemId) throws IOException {
//        Optional<Item> optionalItem = itemRepository.findById(itemId);
//
//        if (optionalItem.isPresent()) {
//            Item item = optionalItem.get();
//            String contentTypeId = item.getContentTypeId();
//
//            switch (contentTypeId) {
//                case "12", "14", "32", "39" -> {
//                    String baseUrl = "https://apis.data.go.kr/B551011/KorService1/detailIntro1?";
//                    String apiKey = "vqoXwkq9RMCWANTOOUUOJVPQ%2FDtls8Z099FreqNacdFobJPBCviYv10hegz5KtPrVxci7OYYwEBNv%2ByS7hZ9%2Fw%3D%3D";
//                    String contentId = item.getContentId();
//                    String urlStr = baseUrl +
//                            "serviceKey=" + apiKey +
//                            "&MobileOS=ETC" +
//                            "&MobileApp=AppTest" +
//                            "&_type=json" +
//                            "&contentId=" + contentId +
//                            "&contentTypeId=" + contentTypeId + //12, 14, 32, 39
//                            "&numOfRows=10" +
//                            "&pageNo=1";
//
//                    URL url = new URL(urlStr);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//
//                    int responseCode = urlConnection.getResponseCode();
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
//                        StringBuilder resultBuilder = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            resultBuilder.append(line).append("\n");
//                        }
//                        br.close();
//
//                        String resultData = resultBuilder.toString();
//
//                        return ResponseEntity.ok(resultData);
//                    } else {
//
//                        throw new CustomException(ErrorCode.ERROR_NOT_FOUND);
//                    }
//                }
//                default -> throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
//            }
//        } else {
//            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
//        }
//    }

    public ResponseEntity<?> readItem(Long itemId) throws IOException {
        String result = "";
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isPresent()) {
            Item item = optionalItem.get();
            String contentTypeId = item.getContentTypeId();

            switch (contentTypeId) {
                case "12", "14", "32", "39" -> {
                    String baseUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?";
                    String apiKey = "vqoXwkq9RMCWANTOOUUOJVPQ%2FDtls8Z099FreqNacdFobJPBCviYv10hegz5KtPrVxci7OYYwEBNv%2ByS7hZ9%2Fw%3D%3D";
                    String contentId = item.getContentId();
                    String urlStr = baseUrl +
                            "serviceKey=" + apiKey +
                            "&MobileOS=ETC" +
                            "&MobileApp=AppTest" +
                            "&_type=json" +
                            "&contentId=" + contentId +
                            "&contentTypeId=" + contentTypeId + //12, 14, 32, 39
                            "&defaultYN=Y"+
                            "&firstImageYN=Y"+
                            "&areacodeYN=Y"+
                            "&catcodeYN=Y"+
                            "&addrinfoYN=Y"+
                            "&mapinfoYN=Y"+
                            "&overviewYN=Y"+
                            "&numOfRows=10" +
                            "&pageNo=1";
                    URL url = new URL(urlStr);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    StringBuilder resultBuilder = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        resultBuilder.append(line).append("\n");
                    }
                    result = resultBuilder.toString();
                    return ResponseEntity.ok(result);
                }
                default -> throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
            }

        } else throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
    }
}