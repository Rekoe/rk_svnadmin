package com.rekoe.module;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.util.Daos;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.web.ajax.AjaxReturn;

import com.rekoe.common.Message;

public class BaseAction {
	private final static Log log = Logs.get();

	protected Message responseMessage(AjaxReturn result, HttpServletRequest req) {
		return result.isOk() ? Message.success("admin.common.ok", req) : Message.error(StringUtils.defaultString(result.getMsg(), "admin.common.resopnse.error"), req);
	}

	public int getPage(Integer page) {
		return page == null ? 1 : page.intValue();
	}

	public boolean checkTableExist(final Dao dao, final Class<?> classOfT, String time) {
		return Daos.ext(dao, time).exists(classOfT);
	}

	public class down {
		private ByteArrayOutputStream bout = new ByteArrayOutputStream();

		public ByteArrayOutputStream invoke(String[] heads, List<Record> records, DownCallBack callBack) throws IOException {
			StringBuffer sb = new StringBuffer();
			for (String head : heads) {
				sb.append(head).append(",");
			}
			sb.delete(sb.length() - 1, sb.length());
			bout.write(sb.toString().getBytes());
			bout.write(new byte[] { 13, 10 });
			for (final Record record : records) {
				bout.write(download(callBack, record));
			}
			bout.close();
			return bout;
		}

		public ByteArrayOutputStream invoke(List<Record> records, DownCallBack callBack) throws IOException {
			for (final Record record : records) {
				bout.write(download(callBack, record));
			}
			bout.close();
			return bout;
		}

		private byte[] download(DownCallBack callBack, Record record) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(bout);
			try {
				callBack.invoke(dout, record);
				dout.write(new byte[] { 13, 10 });
				bout.close();
				dout.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			return bout.toByteArray();
		}
	}

	public interface DownCallBack {
		public void invoke(DataOutputStream dout, Record record) throws IOException;
	}

	public class DefaultDownCallBack implements DownCallBack {

		private String[] heads;

		public DefaultDownCallBack(String[] heads) {
			this.heads = heads;
		}

		@Override
		public void invoke(DataOutputStream dout, org.nutz.dao.entity.Record record) throws IOException {
			StringBuffer sb = new StringBuffer();
			for (String str : heads) {
				sb.append(record.getString(str)).append(",");
			}
			dout.write(sb.toString().getBytes());
		}
	}

	public ByteArrayOutputStream loadDown(List<Record> records) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if (!records.isEmpty()) {
			Record r = records.get(0);
			Set<String> set = r.keySet();
			List<String> keys = new ArrayList<>(set);
			String[] heads = keys.toArray(new String[keys.size()]);
			DownCallBack callBack = new DefaultDownCallBack(heads);
			bout = new down().invoke(heads, records, callBack);
		}
		return bout;
	}
}
