package com.rekoe.valueadaptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

public abstract class JsonValueAdaptor implements ValueAdaptor {

	@Override
	public Object get(ResultSet rs, String colName) throws SQLException {
		String data = rs.getString(colName);
		if (Strings.isBlank(data) || Lang.equals("null", data) || !Strings.startsWithChar(data, '[')) {
			data = "[]";
		}
		return Json.fromJsonAsList(JsonClassType(), data);
	}

	@Override
	public void set(PreparedStatement stat, Object obj, int index) throws SQLException {
		stat.setString(index, (obj == null) ? "[]" : Json.toJson(obj, JsonFormat.compact()));
	}

	public abstract Class<?> JsonClassType();
}
