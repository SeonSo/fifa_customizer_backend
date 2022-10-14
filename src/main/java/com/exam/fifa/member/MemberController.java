package com.exam.fifa.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    @Value("${resource.path}")
    private String path;

    @PostMapping("/v1/signup")
    public void signup(@RequestPart(value = "dto") Member member,
                       @RequestPart(value = "profile") MultipartFile profileImg) throws IOException {
        String fileName = profileImg.getOriginalFilename();

        if(!new File(path).exists()) {
            try {
                new File(path).mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String saveFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String filePath = path + File.separator + saveFileName;
        profileImg.transferTo(new File(filePath));

        String fileUrl = "http://localhost:8088/" + saveFileName;
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRoles("ROLE_MEMBER");
        memberRepository.save(member);
    }

    @GetMapping("/v1/member")
    public String member() {
        return "member";
    }

    @GetMapping("/v1/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/v1/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("session")
    public String getSession() {
        String contextHolder = SecurityContextHolder.getContext().getAuthentication().toString();
        System.out.println(contextHolder);
        return "session";
    }
}
