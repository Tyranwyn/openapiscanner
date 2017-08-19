package org.zaproxy.zap.extension.openapiscanner;

import org.zaproxy.zap.extension.openapiscanner.network.RequestModel;

import java.util.ArrayList;
import java.util.List;

public class MergeLibReqModel {
    private ArrayList<String> fuzzList;
    private List<RequestModel> requestModels;
    private List<RequestModel> convertedRequestModels = new ArrayList<>();

    public MergeLibReqModel(ArrayList<String> fuzzList, List<RequestModel> requestModels) {
        this.fuzzList = fuzzList;
        this.requestModels = requestModels;
        loopRequestModels();
    }

    public List<RequestModel> getConvertedRequestModels() {
        return convertedRequestModels;
    }

    private void loopRequestModels() {
        for (RequestModel requestModel : requestModels)
            for (String fuzzItem : fuzzList) {
                RequestModel newReqMod = requestModel.copyModel();
                newReqMod.setUrl(convertUrl(newReqMod.getUrl(), fuzzItem));
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
