package org.zerock.board.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest    // 부트용 테스트 코드임을 명시한다.
public class MemoRepositoryTests {
    @Autowired     // 생성자 자동 주입
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        // 객체 주입 테스트(MemoRepository는 인터페이스임을 기억하기)
        // 원래 인터페이스는 구현객체가 있어야 한다.
        System.out.println(memoRepository.getClass().getName());
        // memoRepository에 생성된 객체의 클래스명과 이름을 알아보자.
        // 콘솔 결과 : jdk.proxy3.$Proxy115(동적 프록시 : 인터페이스 실행 구현 클래스임)
    }

    @Test
    public void testInsertCummies(){
        // memo 테이블에 더미 데이터 추가
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("Sample ... " + i)
                    .build();  // Memo 클래스에 memoText(1~100) 생성 반복
            memoRepository.save(memo); // .save() : jpa 상속으로 사용  // 없으면 insert, 있으면 update
        });
    }

    @Test
    public void testSelect(){
        // 있는 정보 가져오기(mno를 이용)
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        // import java.util.Optional;
        // .findById : select * from 표 where mno = 100;
        // .findById : 쿼리가 미리 실행됨, 결과 출력 나중에
        System.out.println("============================mno = 100");
        if(result.isPresent()){  // result가 null이 아니면
            Memo memo = result.get();
            System.out.println(memo);  // 엔티티가 toString 되어 있음
            // ============================mno = 100
            // Memo(mno=100, memoText=샘플 메모 ... 100)
        }
    }
    
    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno); // getOne은 현재 차단된 메서드(보안상)
        // getOne은 @Transactional 필수, 변수가 호출될 때 쿼리가 실행된다.
        System.out.println("============================mno = 100");
        System.out.println(memo);
        // [org.zerock.board.entity.Memo#100] - no Session : @Transactional 필요
    }

    @Test
    public void updateTest(){
        Memo memo = Memo.builder()
                .mno(300L)
                .memoText("수정된 텍스트 테스트 ... ")
                .build();
        System.out.println(memoRepository.save(memo));
        // .save(memo) -> 없으면 insert, 있으면 update
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno=?
//        Hibernate:
//        update
//                tbl_memo
//        set
//        memo_text=?
//        where
//        mno=?
    }

    @Test
    public void testDelete(){
        Long mno = 201L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10); // 내장된 페이징 처리
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result); // Page 1 of 20 containing org.zerock.board.entity.Memo instances
        // 쿼리 중 limit ?,? : 레코드 제한 개수(오라클의 between과 비슷함)
    }

    @Test
    public void testPageDefaults() {
        // jpa에 내장된 페이징, 정렬 기법 활용
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); // 내림차순 번호 & 메모텍스트 오름차순
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);


        //Hibernate:
        //    select
        //        m1_0.mno,
        //        m1_0.memo_text
        //    from
        //        tbl_memo m1_0
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(m1_0.mno)
        //    from
        //        tbl_memo m1_0
        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances

        System.out.println("---------------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages()); // 총 몇 페이지

        System.out.println("Total Count: "+result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: "+result.getNumber()); // 현재 페이지 번호

        System.out.println("Page Size: "+result.getSize()); // 페이지당 데이터 개수

        System.out.println("has next page?: "+result.hasNext());    // 다음 페이지 존재 여부

        System.out.println("first page?: "+result.isFirst());  //  시작페이지 여부

        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances
        //---------------------------------------
        //Total Pages: 10
        //Total Count: 99
        //Page Number: 0
        //Page Size: 10
        //has next page?: true
        //first page?: true

        System.out.println("-----------------------------------");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
            //-----------------------------------
            //Memo(mno=1, memoText=Sample....1)
            //Memo(mno=2, memoText=Sample....2)
            //Memo(mno=3, memoText=Sample....3)
            //Memo(mno=4, memoText=Sample....4)
            //Memo(mno=5, memoText=Sample....5)
            //Memo(mno=6, memoText=Sample....6)
            //Memo(mno=7, memoText=Sample....7)
            //Memo(mno=8, memoText=Sample....8)
            //Memo(mno=9, memoText=Sample....9)
            //Memo(mno=10, memoText=Sample....10)
        }

    }


}
