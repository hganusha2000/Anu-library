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
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.MemberDetails;
import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.service.MemberService;
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;
    

    // CREATE a new member – LIBRARIAN or ADMIN
    @PostMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createMember(@RequestBody Member member) {
        DataResponse responseData = memberService.createMember(member);

        // Directly use the responseData from service
        ApiResponse response = new ApiResponse(responseData);

        // Return appropriate status based on the content
        if (responseData.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // LIST all members – LIBRARIAN or ADMIN
    @GetMapping("/librarian/members")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Member> memberPage = memberService.getAllMembersPaginated(PageRequest.of(page, size));

        List<DataResponse> memberDataList = memberPage.getContent().stream()
                .map(member -> new DataResponse(HttpStatus.OK.value(), "Member data", member))
                .toList();

        MemberDetails memberDetails = new MemberDetails();
        memberDetails.setData(memberDataList);
        memberDetails.setTotalPages(memberPage.getTotalPages());
        memberDetails.setTotalElements(memberPage.getTotalElements());

        DataResponse wrapper = new DataResponse(HttpStatus.OK.value(), "Members fetched successfully", memberDetails);
        ApiResponse apiResponse = new ApiResponse(wrapper);

        return ResponseEntity.ok(apiResponse);
    }


    // GET a single member by ID – LIBRARIAN or ADMIN
    @GetMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getMemberById(@PathVariable("id") Long id) {
        Member member = memberService.getMemberById(id);

        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Member fetched successfully", member);
        ApiResponse apiResponse = new ApiResponse(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }

    // UPDATE an existing member – LIBRARIAN or ADMIN
    @PutMapping("/librarian/members/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateMember(@PathVariable("id") Long id, @RequestBody Member memberDetails) {
        Member updatedMember = memberService.updateMember(id, memberDetails);

        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Member updated successfully", updatedMember);
        ApiResponse apiResponse = new ApiResponse(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }

    // DELETE a member – ADMIN only
    @DeleteMapping("/admin/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);

        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Member deleted successfully", null);
        ApiResponse apiResponse = new ApiResponse(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }
}