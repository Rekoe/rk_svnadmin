package com.rekoe.module;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.rekoe.domain.ReportAds;
import com.rekoe.filter.OauthCrossOriginFilter;
import com.rekoe.service.ReportAdsService;

@At("/report")
@IocBean
@Filters(@By(type = OauthCrossOriginFilter.class))
public class ReportAdsModule {

	@Inject
	private ReportAdsService reportAdsService;

	/**
	 * @api {POST} /report/ads 数据汇报
	 * @apiSampleRequest http://warlogin.shanggame.com/report/ads
	 * @apiGroup report
	 * @apiVersion 1.0.0
	 *
	 *
	 * @apiParam {Number} pid 申请的游戏编号
	 * @apiParam {String} pfid 接入的平台编号
	 * @apiParam {String} idfa idfa
	 * @apiParam {String} idfv idfv
	 * @apiParam {String} talkingDataId talkingDataId
	 * 
	 * @apiSuccess {json} msg OK
	 *
	 */
	
	@At
	@Ok("raw:json")
	@POST
	public String ads(@Param("pid") int pid, @Param("pfid") String pfid, @Param("idfa") String idfa, 
			@Param("idfv") String idfv, @Param("talkingDataId") String talkingDataId) {
		reportAdsService.add(new ReportAds(pid, pfid, idfa, idfv, talkingDataId));
		return "{\"msg\":\"ok\"}";
	}
}
