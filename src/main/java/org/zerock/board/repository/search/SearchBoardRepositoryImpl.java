package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    // 여러 엔티티가 섞여있는 검색처리를 위한 repository
    // QuerydslRepositorySupport : Spring Data JPA에 포함된 클래스로 Querydsl 라이브러리를 이용해서 직접 무언가를 구현할 때 사용

    // QuerydslRepositorySupport는 생성자가 존재하므로 클래스 내에서 super()를 이용해서 호출해야한다.
    public SearchBoardRepositoryImpl(){
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // Tuple : 엔티티 객체 단위가 아니라 각각의 데이터를 추출할 때 사용!!!
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

//    @Override
//    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
//        // PageRequestDTO 자체를 파라미터로 처리하지 않는 이유 : DTO를 왠만하면 repository에서 다루지 않도록 한다.
//
//        log.info("searchPage........................");
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//        QMember member = QMember.member;
//
//        JPQLQuery<Board> jpqlQuery = from(board);
//        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
//        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
//
//        //SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
//        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
//
//        BooleanBuilder booleanBuilder = new BooleanBuilder();
//        BooleanExpression expression = board.bno.gt(0L); // bno > 0
//
//        booleanBuilder.and(expression);
//
//        if(type != null){ // 검색 타입이 비어있지 않으면
//            String[] typeArr = type.split(""); // 배열로 저장
//
//            //검색 조건 작성
//            BooleanBuilder conditionBuilder = new BooleanBuilder();
//            for (String t:typeArr) {
//                switch (t){
//                    case "t":
//                        conditionBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "w":
//                        conditionBuilder.or(member.email.contains(keyword));
//                        break;
//                    case "c":
//                        conditionBuilder.or(board.content.contains(keyword));
//                        break;
//                }
//            }
//            booleanBuilder.and(conditionBuilder); // bno > 0 and (검색조건 or ..)
//        }
//
//        tuple.where(booleanBuilder); // 조인한것에 where 지정
//
//        // pageable 객체의 정렬을 가져온다
//        Sort sort = pageable.getSort();
//        // tuple.orderBy(board.bno.desc()); // 직접 코드로 처리한다면 ..
//
//        sort.stream().forEach(order -> { // 정렬이 여러개일수도 있음
//            // 해당 정렬객체가 오름차순이면 오름차순 저장, 아니면 내림차순 저장?
//            // Sort 객체의 정렬 관련 정보 처리 : OrderSpecifier에는 정렬이 필요하다.
//            Order direction = order.isAscending()? Order.ASC: Order.DESC;
//            // Sort 객체의 속성 저장 .. (bno, title .. ) ?
//            String prop = order.getProperty();
//            // PathBuilder를 생성할 때 문자열로 된 이름은 JPQLQuery를 생성할 때 이용하는 변수명과 동일해야한다.
//            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
//            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
//
//        });
//        tuple.groupBy(board); // bno로 묶는다.
//
//        // page 처리
//        // Pageable을 파라미터로 전달받아 JPQLQuery의 offset(), limit()을 이용해 페이지처리함
//        tuple.offset(pageable.getOffset());
//        tuple.limit(pageable.getPageSize());
//
//        List<Tuple> result = tuple.fetch(); // 실행
//        log.info(result);
//
//        // 위의 복잡한 정렬방법은 count 쿼리도 같이 처리할 수 있다
//        long count = tuple.fetchCount();
//        log.info("COUNT: " +count);
//
//        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),pageable,count);
//    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        // PageRequestDTO 자체를 파라미터로 처리하지 않는 이유 : DTO를 왠만하면 repository에서 다루지 않도록 한다.
        log.info("searchPage.............................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L); // bno > 0

        booleanBuilder.and(expression);

        if(type != null){ // 검색 타입이 비어있지 않으면
            String[] typeArr = type.split(""); // 배열로 저장
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) { // 정렬이 여러개일수도 있음
                switch (t){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder); // bno > 0 and (검색조건 or ..)
        }

        tuple.where(booleanBuilder); // 조인한것에 where 지정

        // pageable 객체의 정렬을 가져온다
        Sort sort = pageable.getSort();
        //tuple.orderBy(board.bno.desc()); // 직접 코드로 처리한다면 ..

        sort.stream().forEach(order -> {
            // 해당 정렬객체가 오름차순이면 오름차순 저장, 아니면 내림차순 저장?
            // Sort 객체의 정렬 관련 정보 처리 : OrderSpecifier에는 정렬이 필요하다.
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            // Sort 객체의 속성 저장 .. (bno, title .. ) ?
            String prop = order.getProperty();
            // PathBuilder를 생성할 때 문자열로 된 이름은 JPQLQuery를 생성할 때 이용하는 변수명과 동일해야한다.
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(board); // bno로 묶는다.

        // page 처리
        // Pageable을 파라미터로 전달받아 JPQLQuery의 offset(), limit()을 이용해 페이지처리함
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch(); // 실행
        log.info(result);

        // 위의 복잡한 정렬방법은 count 쿼리도 같이 처리할 수 있다
        long count = tuple.fetchCount();
        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
