package live.codeland.petsguidesbackend.phoneVerification;

public class PhoneVerificationResponse {

    public PhoneVerificationResponse(String token) {
        this.token = token;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
