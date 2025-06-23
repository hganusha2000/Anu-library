package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.MemberResponseDTO;
import Hutechlibrary.Anu.Library.entity.Member;
import jakarta.validation.Valid;

public interface MemberService {

	DataResponse createMember(Member member);

	Member getMemberById(Long id);

	Member updateMember(Long id, Member memberDetails);

	void deleteMember(Long id);
	
	Page<Member> getAllMembersPaginated(Pageable pageable);
	
	Page<Member> searchMembers(String firstName, String lastName, String email, String phone, Pageable pageable);

	Member createMemberEntity(@Valid Member member);

	MemberResponseDTO convertToDTO(Member member);



}
