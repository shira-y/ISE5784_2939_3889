package renderer;

public class SoftShadowsSettings {
    private boolean softShadowsEnabled = false;
    private int numSampleRays = 81;

    public boolean isSoftShadowsEnabled() {
        return softShadowsEnabled;
    }

    public void setSoftShadowsEnabled(boolean softShadowsEnabled) {
        this.softShadowsEnabled = softShadowsEnabled;
    }

    public int getNumSampleRays() {
        return numSampleRays;
    }

    public void setNumSampleRays(int numSampleRays) {
//        if (numSampleRays < 50) {
//            throw new IllegalArgumentException("Number of sample rays must be at least 50");
//        }
        this.numSampleRays = numSampleRays;
    }
}