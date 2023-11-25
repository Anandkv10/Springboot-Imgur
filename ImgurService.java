package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ImgurService {
	private final String IMGUR_API_BASE_URL = "https://api.imgur.com/3";
	private final String CLIENT_ID = "f7be22089a857d8";

	@Autowired
	RestTemplate restTemplate;

	public String uploadImage(byte[] imageData) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Client-ID " + CLIENT_ID);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("image", new HttpEntity<>(imageData, headers));

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(IMGUR_API_BASE_URL + "/image", HttpMethod.POST,
				requestEntity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {

			return response.getBody(); // Modify this according to Imgur's API response
		} else {

			return null;
		}
	}

	public byte[] getImageForUser(Long userId, String imageId) {

		return null;
	}

	public ResponseEntity<String> deleteImage(String imageId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Client-ID " + CLIENT_ID);

		HttpEntity<?> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(IMGUR_API_BASE_URL + "/image/" + imageId,
				HttpMethod.DELETE, requestEntity, String.class);

		return response;
	}

	private Map<String, byte[]> imageStore = new HashMap<>();

	public byte[] getImageData(String imageId) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(IMGUR_API_BASE_URL + "/image", HttpMethod.GET,
				requestEntity, String.class);
		return imageStore.get(imageId);

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}