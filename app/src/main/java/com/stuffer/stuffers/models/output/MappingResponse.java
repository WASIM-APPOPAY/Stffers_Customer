
package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MappingResponse {

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
        private String mobilenumber;
        @SerializedName("uniquenumber")
        @Expose
        private String uniquenumber;
        @SerializedName("usertype")
        @Expose
        private String usertype;
        @SerializedName("userid")
        @Expose
        private String userid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getUniquenumber() {
            return uniquenumber;
        }

        public void setUniquenumber(String uniquenumber) {
            this.uniquenumber = uniquenumber;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}

