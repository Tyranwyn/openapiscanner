package org.zaproxy.zap.extension.openapiscanner;

import org.zaproxy.zap.extension.openapiscanner.network.RequestModel;

import java.util.ArrayList;
import java.util.List;

public class MergeLibReqModel {
    private ArrayList<String> fuzzList;
    private List<RequestModel> requestModels;
    private List<RequestModel> convertedRequestModels = new ArrayList<>();
    private String type;

    public MergeLibReqModel(ArrayList<String> fuzzList, List<RequestModel> requestModels, String type) {
        this.fuzzList = fuzzList;
        this.requestModels = requestModels;
        this.type = type;
        loopRequestModels();
    }

    public List<RequestModel> getConvertedRequestModels() {
        return convertedRequestModels;
    }

    private void loopRequestModels() {
        for (RequestModel requestModel : requestModels)
            for (String fuzzItem : fuzzList) {
                RequestModel newReqMod = requestModel.copyModel();
                switch (type) {
                    case "url":
                        newReqMod.setUrl(convertUrl(newReqMod.getUrl(), fuzzItem));
                        break;
                    case "body":
                        newReqMod.setBody(fuzzItem);
                        break;
                }
                convertedRequestModels.add(newReqMod);
            }
    }

    private String convertUrl(String url, String fuzz) {
        int endingSlash = url.lastIndexOf("/");
        String newUrl;

        if (endingSlash <= 10)
            newUrl = url + "/";
        else
            newUrl = url.substring(0, endingSlash + 1);

        newUrl += fuzz;

        return newUrl;
    }
}
