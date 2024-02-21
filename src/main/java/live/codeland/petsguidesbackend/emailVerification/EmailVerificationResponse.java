package live.codeland.petsguidesbackend.emailVerification;

public class EmailVerificationResponse {
    public EmailVerificationResponse(String token) {
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
