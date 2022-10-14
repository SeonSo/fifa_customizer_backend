package com.exam.fifa.member;

import org.springframework.data.jpa.repository.JpaRepository;
import com.exam.fifa.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
