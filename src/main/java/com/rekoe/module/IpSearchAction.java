package com.rekoe.module;

import org.nutz.http.Http;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

@IocBean
@At("/api")
public class IpSearchAction {

	private final static String API_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	@At("/ip/search")
	@Ok("raw:json")
	@POST
	public String search(@Param("ip") String ip) {
		return Http.get(API_URL + ip).getContent();
	}
}
