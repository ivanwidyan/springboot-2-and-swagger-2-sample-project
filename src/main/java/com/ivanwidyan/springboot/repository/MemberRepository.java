package com.ivanwidyan.springboot.repository;

import com.ivanwidyan.springboot.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findById(Integer id);
}
