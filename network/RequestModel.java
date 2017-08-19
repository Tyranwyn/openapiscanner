/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2017 The ZAP Development Team
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.extension.openapiscanner.network;

import com.sun.org.apache.regexp.internal.RE;
import org.parosproxy.paros.network.HttpHeaderField;

import java.util.List;

public class RequestModel {

    private String url;
    private List<HttpHeaderField> headers;
    private String body;
    private RequestMethod method;
    private String bodyType; // parameter: consumes
    private FormData formData;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public List<HttpHeaderField> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpHeaderField> headers) {
        this.headers = headers;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    public RequestModel copyModel() {
        RequestModel newReqMod = new RequestModel();
        newReqMod.setUrl(this.getUrl());
        newReqMod.setBody(this.getBody());
        newReqMod.setFormData(this.getFormData());
        newReqMod.setHeaders(this.getHeaders());
        newReqMod.setMethod(this.getMethod());
        newReqMod.setBodyType(this.getBodyType());
        return newReqMod;
    }

}
