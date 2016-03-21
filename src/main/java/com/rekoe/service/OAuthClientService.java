package com.rekoe.service;

import java.util.List;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.OAuthClient;

public interface OAuthClientService {

	public OAuthClient createClient(OAuthClient client);

	public OAuthClient updateClient(OAuthClient client);

	public void deleteClient(Long clientId);

	public OAuthClient findOne(Long clientId);

	public List<OAuthClient> findAll();

	public OAuthClient findByClientId(String clientId);

	public OAuthClient findByClientSecret(String clientSecret);

	public Pagination getListPager(int pageNumber);

	public boolean check(String name);
}
