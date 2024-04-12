package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    // 목록 페이지를 요청할 때 사용하는 데이터를 재사용하기 쉽게 만든 클래스
    // 화면에서 전달되는 page, size 파라미터를 수집하는 역할
    // JPA에서 사용하는 Pageable 타입의 객체를 생성할 때 사용
    private int page;
    private int size;

    public PageRequestDTO(){ // 페이지번호 등은 기본값을 가지는것이 좋음
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page - 1, size, sort);
        // JPA를 이용하는 경우 페이지 번호가 0부터 시작하여 page - 1
        // 정렬은 다양한 상황에서 재사용 가능하도로고 파라미터로 받음
    }


}
