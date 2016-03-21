package com.rekoe.mvc.view;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Encoding;
import org.nutz.lang.Streams;
import org.nutz.lang.random.R;
import org.nutz.mvc.View;

public class DataDownView implements View {

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		if (obj instanceof ByteArrayOutputStream) {
			ByteArrayOutputStream bout = (ByteArrayOutputStream) obj;
			String filename = URLEncoder.encode(R.sg(5, 10).next() + ".csv", Encoding.UTF8);
			resp.setHeader("Content-Length", "" + bout.size());
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			Streams.writeAndClose(resp.getOutputStream(), bout.toByteArray());
		}
	}
}