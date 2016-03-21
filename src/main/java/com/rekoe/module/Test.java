package com.rekoe.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Streams;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import com.rekoe.filter.OauthCrossOriginFilter;

@IocBean
@At("/api/v2")
@Filters(@By(type = OauthCrossOriginFilter.class))
public class Test {

	/**
	 * @api {POST} /api/v2/hello/:id
	 * @apiGroup Account
	 * @apiVersion 1.0.3
	 * @apiDescription just a test
	 * @apiPermission anyone
	 * @apiSampleRequest http://106.2.185.120:8080/api/v2/hello/:id
	 *
	 * @apiParam {String} name object
	 * @apiParam {Number} age object
	 * @apiSuccess {json} result User object
	 * 
	 * @apiSuccessExample {json} Success-Response: 
	 * { "firstname": "test", lastname": "sails", "country":"cn" }
	 *
	 * @apiParamExample {json} Request
	 * { "firstname": "test", "lastname": "sails", "country":"cn" }
	 *
	 */

	/**
	 * @api {POST} /api/v2/hello/:id
	 * @apiGroup Account
	 * @apiVersion 1.0.4
	 * @apiDescription just a test
	 * @apiPermission anyone
	 * @apiSampleRequest http://106.2.185.120:8080/api/v2/hello/:id
	 *
	 * @apiParam {String} name object
	 * @apiParam {Number} age object
	 * @apiSuccess {json} result User object
	 * 
	 * @apiSuccessExample {json} Success-Response: 
	 * { "firstname": "test", lastname": "sails", "country":"cn" }
	 *
	 * @apiParamExample {json} Request
	 * { "firstname": "test", "lastname": "sails", "country":"cn" }
	 *
	 */
	@At("/hello/?")
	@Ok("raw:json")
	public String hello(long id,InputStream is) throws IOException {
		System.out.println(id);
		Reader reader = Streams.utf8r(is);
		return Streams.readAndClose(reader);
	}
}
