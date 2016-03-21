package com.rekoe.module.facebook;

import org.nutz.json.Json;
public class FacebookSignedRequest {

	private String algorithm;
	private Long expires;
	private Long issued_at;
	private String oauth_token;
	private Long user_id;
	private FacebookSignedRequestUser user;

	public static <T extends FacebookSignedRequest> T getFacebookSignedRequest(String signedRequest, Class<T> clazz) throws Exception {
		String payload = signedRequest.split("[.]", 2)[1];
		payload = payload.replace("-", "+").replace("_", "/").trim();
		String jsonString = new String(org.nutz.repo.Base64.decodeFast(payload.getBytes()));
		return Json.fromJson(clazz, jsonString);

	}

	public static void main(String[] args) {
		String payload = "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjE0MDI5MDIwMDAsImlzc3VlZF9hdCI6MTQwMjg5Njk3MSwib2F1dGhfdG9rZW4iOiJDQUFVZVZjb25FWkJrQkFIdFJtdjJOTVpBRUdvU3NGOENzb1lNYlZxZTVmNXRtYU9jRHdGWkF4QklKUHRaQWVYM3JEajlJVmVZVE5OcmJ6SkdaQmhNUU1oRW80enpXUGI2QTlLTnJVWkFXWkFjUjF2S0Y2WGhFdlNmWkJJbUNoSERHOEt4SVhiRlQ0VkNNSEFTZXNoblVnWkFSc3p3R3RqS0hodHJKZlVjM0ZaQkRoT295OGJUeEEzbFpCbG1xRlhaQU44REUyWVA4aUFxcFhWTlB3WkRaRCIsInVzZXIiOnsiY291bnRyeSI6InVzIiwibG9jYWxlIjoiemhfQ04iLCJhZ2UiOnsibWluIjoyMX19LCJ1c2VyX2lkIjoiMTAwMDAxNzI4NTc5ODU4In0";
		//{"algorithm":"HMAC-SHA256","expires":1402902000,"issued_at":1402896971,"oauth_token":"CAAUeVconEZBkBAHtRmv2NMZAEGoSsF8CsoYMbVqe5f5tmaOcDwFZAxBIJPtZAeX3rDj9IVeYTNNrbzJGZBhMQMhEo4zzWPb6A9KNrUZAWZAcR1vKF6XhEvSfZBImChHDG8KxIXbFT4VCMHASeshnUgZARszwGtjKHhtrJfUc3FZBDhOoy8bTxA3lZBlmqFXZAN8DE2YP8iAqpXVNPwZDZD","user":{"country":"us","locale":"zh_CN","age":{"min":21}},"user_id":"100001728579858"}
		String jsonString = new String(org.nutz.repo.Base64.decodeFast(payload.getBytes()));
		System.out.println(jsonString);}
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	public Long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(Long issued_at) {
		this.issued_at = issued_at;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public FacebookSignedRequestUser getUser() {
		return user;
	}

	public void setUser(FacebookSignedRequestUser user) {
		this.user = user;
	}

	public static class FacebookSignedRequestUser {

		private String country;
		private String locale;
		private FacebookSignedRequestUserAge age;

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public FacebookSignedRequestUserAge getAge() {
			return age;
		}

		public void setAge(FacebookSignedRequestUserAge age) {
			this.age = age;
		}

		public static class FacebookSignedRequestUserAge {
			private int min;
			private int max;

			public int getMin() {
				return min;
			}

			public void setMin(int min) {
				this.min = min;
			}

			public int getMax() {
				return max;
			}

			public void setMax(int max) {
				this.max = max;
			}
		}
	}
}
