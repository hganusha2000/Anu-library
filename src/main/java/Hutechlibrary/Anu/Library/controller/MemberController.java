package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.service.MemberService;
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // LIST all members – LIBRARIAN or ADMIN
    @GetMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // GET a single member by ID – LIBRARIAN or ADMIN
    @GetMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Member> getMemberById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // CREATE a new member – LIBRARIAN or ADMIN
    @PostMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.createMember(member));
    }

    // UPDATE an existing member – LIBRARIAN or ADMIN
    @PutMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Member> updateMember(@PathVariable("id") Long id,
                                               @RequestBody Member memberDetails) {
        return ResponseEntity.ok(memberService.updateMember(id, memberDetails));
    }

    // DELETE a member – ADMIN only
    @DeleteMapping("/admin/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully");
    }
}