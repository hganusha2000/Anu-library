package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, Member memberDetails) {
        Member member = getMemberById(id);
        member.setFirstName(memberDetails.getFirstName());
        member.setLastName(memberDetails.getLastName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}
