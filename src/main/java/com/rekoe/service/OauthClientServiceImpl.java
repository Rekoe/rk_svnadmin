package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.OAuthClient;

@IocBean(name = "oAuthClientService", fields = { "dao" })
public class OauthClientServiceImpl extends BaseService<OAuthClient> implements OAuthClientService {

	@Override
	public OAuthClient createClient(OAuthClient client) {
		client.setClientId(R.UU32());
		client.setClientSecret(R.UU32());
		return dao().insert(client);
	}

	@Override
	public OAuthClient updateClient(OAuthClient client) {
		dao().update(client, "^(clientId|clientSecret)$");
		return client;
	}

	@Override
	public void deleteClient(Long clientId) {
		dao().delete(getEntityClass(), clientId);
	}

	@Override
	public OAuthClient findOne(Long id) {
		return dao().fetch(getEntityClass(), id);
	}

	@Override
	public List<OAuthClient> findAll() {
		return dao().query(getEntityClass(), null);
	}

	@Override
	public OAuthClient findByClientId(String clientId) {
		return dao().fetch(getEntityClass(), Cnd.where("clientId", "=", clientId));
	}

	@Override
	public OAuthClient findByClientSecret(String clientSecret) {
		return dao().fetch(getEntityClass(), Cnd.where("clientSecret", "=", clientSecret));
	}

	public Pagination getListPager(int pageNumber) {
		return getObjListByPager(pageNumber, DEFAULT_PAGE_NUMBER, null);
	}

	@Override
	public boolean check(String name) {
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("clientName", "=", name)));
	}
}
