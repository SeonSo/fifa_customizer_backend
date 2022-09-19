package com.exam.fifa.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    // Nickname으로 멤버 조회
    Optional<Member> findByUsername(String username);

    // Email로 멤버 존재 여부 확인
    boolean existsByEmail(String email);
    // Nickname으로 멤버 존재 여부 확인
    boolean existsByUsername(String username);
}
