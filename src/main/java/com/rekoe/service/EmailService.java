package com.rekoe.service;

public interface EmailService {

	boolean send(String to, String subject, String html);

}
