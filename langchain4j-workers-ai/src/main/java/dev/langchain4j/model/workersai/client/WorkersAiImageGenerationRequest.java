/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import java.util.Arrays;

public class WorkersAiImageGenerationRequest {
    String prompt;
    int[] image;
    int[] mask;
    Integer num_steps;
    Integer strength;
    String destinationFile;

    public WorkersAiImageGenerationRequest() {
    }

    public WorkersAiImageGenerationRequest(String prompt, int[] image, int[] mask, Integer num_steps, Integer strength, String destinationFile) {
        this.prompt = prompt;
        this.image = image;
        this.mask = mask;
        this.num_steps = num_steps;
        this.strength = strength;
        this.destinationFile = destinationFile;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public int[] getImage() {
        return this.image;
    }

    public int[] getMask() {
        return this.mask;
    }

    public Integer getNum_steps() {
        return this.num_steps;
    }

    public Integer getStrength() {
        return this.strength;
    }

    public String getDestinationFile() {
        return this.destinationFile;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setImage(int[] image) {
        this.image = image;
    }

    public void setMask(int[] mask) {
        this.mask = mask;
    }

    public void setNum_steps(Integer num_steps) {
        this.num_steps = num_steps;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorkersAiImageGenerationRequest)) {
            return false;
        }
        WorkersAiImageGenerationRequest other = (WorkersAiImageGenerationRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$prompt = this.getPrompt();
        String other$prompt = other.getPrompt();
        if (this$prompt == null ? other$prompt != null : !this$prompt.equals(other$prompt)) {
            return false;
        }
        if (!Arrays.equals(this.getImage(), other.getImage())) {
            return false;
        }
        if (!Arrays.equals(this.getMask(), other.getMask())) {
            return false;
        }
        Integer this$num_steps = this.getNum_steps();
        Integer other$num_steps = other.getNum_steps();
        if (this$num_steps == null ? other$num_steps != null : !((Object)this$num_steps).equals(other$num_steps)) {
            return false;
        }
        Integer this$strength = this.getStrength();
        Integer other$strength = other.getStrength();
        if (this$strength == null ? other$strength != null : !((Object)this$strength).equals(other$strength)) {
            return false;
        }
        String this$destinationFile = this.getDestinationFile();
        String other$destinationFile = other.getDestinationFile();
        return !(this$destinationFile == null ? other$destinationFile != null : !this$destinationFile.equals(other$destinationFile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorkersAiImageGenerationRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $prompt = this.getPrompt();
        result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
        result = result * 59 + Arrays.hashCode(this.getImage());
        result = result * 59 + Arrays.hashCode(this.getMask());
        Integer $num_steps = this.getNum_steps();
        result = result * 59 + ($num_steps == null ? 43 : ((Object)$num_steps).hashCode());
        Integer $strength = this.getStrength();
        result = result * 59 + ($strength == null ? 43 : ((Object)$strength).hashCode());
        String $destinationFile = this.getDestinationFile();
        result = result * 59 + ($destinationFile == null ? 43 : $destinationFile.hashCode());
        return result;
    }

    public String toString() {
        return "WorkersAiImageGenerationRequest(prompt=" + this.getPrompt() + ", image=" + Arrays.toString(this.getImage()) + ", mask=" + Arrays.toString(this.getMask()) + ", num_steps=" + this.getNum_steps() + ", strength=" + this.getStrength() + ", destinationFile=" + this.getDestinationFile() + ")";
    }
}

