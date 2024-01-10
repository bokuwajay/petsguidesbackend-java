package live.codeland.petsguidesbackend.auth;

import jakarta.validation.constraints.NotNull;

public class AuthRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
