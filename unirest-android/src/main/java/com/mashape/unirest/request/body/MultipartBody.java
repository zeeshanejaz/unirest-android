/*
The MIT License

Copyright (c) 2013 Mashape (http://mashape.com)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.mashape.unirest.request.body;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import local.org.apache.http.HttpEntity;
import local.org.apache.http.client.entity.UrlEncodedFormEntity;
import local.org.apache.http.entity.ContentType;
import local.org.apache.http.entity.mime.MultipartEntityBuilder;
import local.org.apache.http.entity.mime.content.FileBody;
import local.org.apache.http.entity.mime.content.StringBody;

import com.mashape.unirest.http.utils.MapUtil;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;

public class MultipartBody extends BaseRequest implements Body {

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private boolean hasFile;
	private HttpRequest httpRequestObj;
	
	public MultipartBody(HttpRequest httpRequest) {
		super(httpRequest);
		this.httpRequestObj = httpRequest;
	}
	
	public MultipartBody field(String name, Object value) {
        if(null == value)
            return this;

		parameters.put(name, value.toString());
		return this;
	}
	
	public MultipartBody field(String name, File file) {
        if(null == file)
            return this;

		this.parameters.put(name, file);
		hasFile = true;
		return this;
	}
	
	public MultipartBody basicAuth(String username, String password) {
		httpRequestObj.basicAuth(username, password);
		return this;
	}
	
	public HttpEntity getEntity() {
		if (hasFile) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for(Entry<String, Object> part : parameters.entrySet()) {

                Object value = part.getValue();
                if(null == value)
                    continue;

                if (value instanceof File) {
					hasFile = true;
					builder.addPart(part.getKey(), new FileBody((File) value));
				} else {
					builder.addPart(part.getKey(), new StringBody(value.toString(), ContentType.APPLICATION_FORM_URLENCODED));
				}
			}
			return builder.build();
		} else {
			try {
				return new UrlEncodedFormEntity(MapUtil.getList(parameters), UTF_8);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
