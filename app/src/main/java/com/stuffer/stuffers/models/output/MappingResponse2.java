
package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MappingResponse2 {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("mobilenumber")
        @Expose
        private String mobile;


        @SerializedName("uniquenumber")
        @Expose
        private String uniqueNumber;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("userId")
        @Expose
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }



        public String getUniqueNumber() {
            return uniqueNumber;
        }

        public String getUserType() {
            return userType;
        }

        public String getUserId() {
            return userId;
        }
    }
}

