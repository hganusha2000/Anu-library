package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.MemberResponseDTO;
import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.MemberRepository;


@Service
public class MemberServiceIMPL implements MemberService {
	
	@Autowired
    private MemberRepository memberRepository;
    
	@Override
	public DataResponse createMember(Member member) {
	    if (memberRepository.existsByEmail(member.getEmail())) {
	        return new DataResponse(HttpStatus.CONFLICT.value(), "Email already exists", null);
	    }

	    if (memberRepository.existsByPhone(member.getPhone())) {
	        return new DataResponse(HttpStatus.CONFLICT.value(), "Phone number already exists", null);
	    }

	    Member savedMember = memberRepository.save(member);
	    MemberResponseDTO dto = convertToDTO(savedMember);
	    return new DataResponse(HttpStatus.CREATED.value(), "Member created successfully", dto);
	}

    @Override
    public Page<Member> getAllMembersPaginated(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
    
    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }
    
    @Override
    public Page<Member> searchMembers(String firstName, String lastName, String email, String phone, Pageable pageable) {
        firstName = firstName != null ? firstName : "";
        lastName = lastName != null ? lastName : "";
        email = email != null ? email : "";
        phone = phone != null ? phone : "";
        
        return memberRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneContainingIgnoreCase(
            firstName, lastName, email, phone, pageable);
    }

    @Override
    public Member updateMember(Long id, Member memberDetails) {
        Member member = getMemberById(id);
        member.setFirstName(memberDetails.getFirstName());
        member.setLastName(memberDetails.getLastName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }

    @Override
    public Member createMemberEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public MemberResponseDTO convertToDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setCreatedAt(member.getCreatedAt());
        dto.setUpdatedAt(member.getUpdatedAt());
        return dto;
    }
}
