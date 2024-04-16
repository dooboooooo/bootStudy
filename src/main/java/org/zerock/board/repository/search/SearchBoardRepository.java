package org.zerock.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.zerock.board.entity.Board;

public interface SearchBoardRepository {
    // 여러 엔티티가 섞여있는 검색처리를 위한 repository
    // 여기 인터페이스 내의 메서드 이름은 가능하면 쿼리 메서드와 구별이 가능하도록 작성
    // 구현클래스의 이름은 반드시 인터페이스이름+Impl로 작성

    Board search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
