package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired

	MemberRepository memberRepository;

	public Member registerUser(String username, String password) {

		Member newUser = new Member(0, username, password);
		newUser.setUsername(username);

		newUser.setPassword(password);

		return memberRepository.save(newUser);
	}

}
