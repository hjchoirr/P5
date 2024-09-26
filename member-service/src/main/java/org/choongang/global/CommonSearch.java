package org.choongang.global;

import lombok.Data;

@Data
public class CommonSearch {
    private int page = 1;
    private int limit = 10;

    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드

    private String sort; // 정렬
}
