package com.stuffer.stuffers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionModel {
    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Prediction {

        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("structured_formatting")
        @Expose
        private StructuredFormatting structuredFormatting;
        @SerializedName("types")
        @Expose
        private List<String> types = null;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public StructuredFormatting getStructuredFormatting() {
            return structuredFormatting;
        }

        public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
            this.structuredFormatting = structuredFormatting;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public class StructuredFormatting {

        @SerializedName("main_text")
        @Expose
        private String mainText;
        @SerializedName("main_text_matched_substrings")
        @Expose
        private List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
        @SerializedName("secondary_text")
        @Expose
        private String secondaryText;

        public String getMainText() {
            return mainText;
        }

        public void setMainText(String mainText) {
            this.mainText = mainText;
        }

        public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
            return mainTextMatchedSubstrings;
        }

        public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstring> mainTextMatchedSubstrings) {
            this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        public void setSecondaryText(String secondaryText) {
            this.secondaryText = secondaryText;
        }
    }

    public class MainTextMatchedSubstring {

        @SerializedName("length")
        @Expose
        private Integer length;
        @SerializedName("offset")
        @Expose
        private Integer offset;

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

    }
}
