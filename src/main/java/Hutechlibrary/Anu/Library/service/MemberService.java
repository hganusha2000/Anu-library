package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Member;

public interface MemberService {

	DataResponse createMember(Member member);

	Member getMemberById(Long id);

	Member updateMember(Long id, Member memberDetails);

	void deleteMember(Long id);
	
	Page<Member> getAllMembersPaginated(Pageable pageable);


}
