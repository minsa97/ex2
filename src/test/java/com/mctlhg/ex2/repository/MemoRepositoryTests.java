package com.mctlhg.ex2.repository;

import com.mctlhg.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){ // 등록작업 테스트
        IntStream.rangeClosed(1,100).forEach(i->{
            Memo memo= Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){ // 조회작업 테스트
        Long mno=100L;
        Optional<Memo> result=memoRepository.findById(mno);

        System.out.println("============================================");
        if(result.isPresent()){
            Memo memo=result.get();
            System.out.println(memo);
        }
    }

    @Test
    public void testUpdate(){ // 수정작업 테스트
        Memo memo=Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){
        Long mno=100L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){// 페이징 처리
        // 1페이지 10개
        Pageable pageable= PageRequest.of(0,10);
        Page<Memo> result=memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("------------------------------------------------");
        System.out.println("Total Pages: "+result.getTotalPages());
        System.out.println("Total Count: "+result.getTotalElements());
        System.out.println("Page Number: "+result.getNumber());
        System.out.println("Page size: "+result.getSize());
        System.out.println("has next pages? :"+result.hasNext());
        System.out.println("first page? : "+result.isFirst());

        System.out.println("------------------------------------------------");
        for(Memo memo: result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort(){ // 내림차순 정렬하기
        /*Sort sort1=Sort.by("mno").descending();
        Pageable pageble=PageRequest.of(0,10,sort1);
        Page<Memo> result=memoRepository.findAll(pageble);
        result.get().forEach(memo->{
            System.out.println(memo);
        });*/

        Sort sort1=Sort.by("mno").descending();
        Sort sort2=Sort.by("memoText").ascending();
        Sort sortAll=sort1.and(sort2);
        Pageable pageble=PageRequest.of(0,10,sortAll); // 결합된ㄷ 정렬 조건 사용
    }

    @Test
    public void testQueryMethos(){ // 쿼리 메서드 테스트
        List<Memo> list=memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for(Memo memo:list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){ // 쿼리메서드 + Pageable
        Pageable pageable=PageRequest.of(0,10, Sort.by("mno").descending());
        Page<Memo> result=memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo-> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteMethos(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
