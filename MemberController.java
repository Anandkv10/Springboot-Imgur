package com.example.demo.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Member;
import com.example.demo.service.ImgurService;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ImgurService imgurService;

	@PostMapping("/register")
	public ResponseEntity<Member> registerUser(@RequestParam String username, @RequestParam String password) {
		Member newUser = memberService.registerUser(username, password);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	@PostMapping("/{memberId}/upload-image")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {

		byte[] imageData;
		try {
			imageData = file.getBytes();
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to read file");
		}

		String imageId = imgurService.uploadImage(imageData);

		if (imageId != null) {

			return ResponseEntity.status(HttpStatus.CREATED).body(imageId);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
		}
	}

	@GetMapping("/{memberId}/images/{imageId}")

	public ResponseEntity<byte[]> getImage(@PathVariable String imageId) {
		byte[] imageData = imgurService.getImageData(imageId);

		if (imageData != null) {
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.IMAGE_JPEG); //

			return new ResponseEntity<byte[]>(imageData, headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{memberId}/images/{imageId}")
	public ResponseEntity<String> deleteImage(@PathVariable String imageId) {

		ResponseEntity<String> deleteResponse = imgurService.deleteImage(imageId);

		if (deleteResponse.getStatusCode() == HttpStatus.OK) {

			return ResponseEntity.ok("Image deleted successfully");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image");
		}
	}
}