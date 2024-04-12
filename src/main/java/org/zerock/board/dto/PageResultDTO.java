package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    // JPA를 이용하는 Repository에서 페이지 처리 결과를 Page<Entity> 타입으로 반환함
    // service에서 Page<Entity> 타입을 처리하기 위해 클래스를 생성함
    // Page<Entity> 타입을 DTO 객체로 변환한 리스트와, 페이징 정보를 담고 있는 클래스!!

    // Page<Entity> 타입을 DTO 객체로 변환한 리스트
    private List<DTO> dtoList;

    // 페이징 정보
    private  int totalPage;     // 총 페이지 번호
    private int page;           // 현재 페이지 번호
    private int size;           // 목록 사이즈
    private int start, end;     // 시작 페이지 번호, 끝 페이지 번호
    private boolean prev, next; // 이전, 다음 표시 여부
    private List<Integer> pageList; // 페이지 번호 목록


    // Page<EN> : Page 처리된 entity 객체들
    // Function<EN, DTO> : entity 객체들을 DTO로 변환해주는 기능
    // 이러한 구조를 이용하면 나중에 어떤 종류의 Page<EN> 타입이 생성되더라도 클래스 재사용 가능
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){ // 생성자
        // 페이징 된 entity 객체들(result)를 fn 함수로 dto로 변환해서 List<DTO> 타입의 dtoList 변수에 넣는다.
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages(); // .getTotalPages() : Page<> 내장 메서드
        makePageList(result.getPageable()); // .getPageable() : Page<> 내장 메서드, Pageable을 가져온다.
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // JPA를 사용해서 0부터 시작하므로 1을 더함
        this.size = pageable.getPageSize();

        // 페이지 번호가 10개씩 출력됨
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10; //
        start = tempEnd - 9;
        prev = start > 1;
        // 임시로 계산한 끝 페이지 번호가 진짜 끝 페이지 번호보다 크면 진짜 끝 페이지 번호를 마지막 페이지로 end에 저장
        end = totalPage > tempEnd ? tempEnd : totalPage; // 총 페이지 수 > 임시 총 페이지 ? 임시 페이지 수 : 총 페이지 수
        next = totalPage > tempEnd; // 진짜 끝 페이지 번호보다 임시로 계산한 끝 페이지 번호가 적은 경우
        // start ~ end 까지의 int형 숫자를 IntStream으로 구하고 boxed(Integer 객체로 변환) 후 List형태로 List<Integer> pageList 변수에 넣는다.
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }


}
