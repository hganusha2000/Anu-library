package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponseMember;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.MemberDetails;
import Hutechlibrary.Anu.Library.dto.MemberResponseDTO;
import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.service.MemberService;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // ✅ CREATE MEMBER
    @PostMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> createMember(@Valid @RequestBody Member member) {
        Member saved = memberService.createMemberEntity(member);
        MemberResponseDTO dto = memberService.convertToDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // ✅ GET ALL MEMBERS (PAGINATED)
    @GetMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMember> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Member> memberPage = memberService.getAllMembersPaginated(PageRequest.of(page, size));
        List<MemberResponseDTO> dtoList = memberPage.getContent()
                .stream()
                .map(memberService::convertToDTO)
                .collect(Collectors.toList());

        ApiResponseMember response = new ApiResponseMember(
                HttpStatus.OK.value(),
                "fetched successfully",
                dtoList,
                memberPage.getTotalPages(),
                memberPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    // ✅ SEARCH MEMBERS
    @GetMapping("/librarian/members/search")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMember> searchMembers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Member> memberPage = memberService.searchMembers(firstName, lastName, email, phone, PageRequest.of(page, size));
        List<MemberResponseDTO> dtoList = memberPage.getContent()
                .stream()
                .map(memberService::convertToDTO)
                .collect(Collectors.toList());

        ApiResponseMember response = new ApiResponseMember(
                HttpStatus.OK.value(),
                "fetched successfully",
                dtoList,
                memberPage.getTotalPages(),
                memberPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    // ✅ GET MEMBER BY ID
    @GetMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        MemberResponseDTO dto = memberService.convertToDTO(member);
        return ResponseEntity.ok(dto);
    }

    // ✅ UPDATE MEMBER
    @PutMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable Long id,
                                                          @Valid @RequestBody Member memberDetails) {
        Member updated = memberService.updateMember(id, memberDetails);
        MemberResponseDTO dto = memberService.convertToDTO(updated);
        return ResponseEntity.ok(dto);
    }

    // ✅ DELETE MEMBER
    @DeleteMapping("/admin/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully.");
    }
}