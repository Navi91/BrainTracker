package com.braintracker.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class StreamConverter {

	public static JSONObject convertStreamToJsonObject(InputStream inputStream) throws JSONException {
		return new JSONObject(convertStreamToString(inputStream));
	}

	public static JSONObject convertHttpResponseToJsonObject(HttpResponse response) throws IllegalStateException, IOException, JSONException {
		return convertStreamToJsonObject(response.getEntity().getContent());
	}

	public static String convertStreamToString(InputStream inputStream) {
		if (inputStream != null) {
			try {
				Writer writer = new StringWriter();

				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} catch (UnsupportedEncodingException e) {
					Tracer.error("convertStreamToString", e);
				} finally {
					inputStream.close();
				}
				return writer.toString();
			} catch (IOException e) {
				Tracer.error("IOException returning blank string", e);
				return "";
			}
		} else {
			return "";
		}
	}

	public static byte[] convertStreamToByteArray(InputStream is) {
		if (is != null) {
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = is.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				return outputStream.toByteArray();
			} catch (IOException e) {
				Tracer.error("IOException returning blank array", e);
				return new byte[0];
			}
		} else {
			return new byte[0];
		}
	}
}
