package live.codeland.petsguidesbackend.helpers;
import java.security.SecureRandom;

public class VerificationCodeGenerator {
    private static final String VALID_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateVerificationCode(){
        StringBuilder verificationCode = new StringBuilder();
        for(int i = 0; i <6 ; i++){
            int index = secureRandom.nextInt(VALID_CHARACTERS.length());
            char randomChar = VALID_CHARACTERS.charAt(index);
            verificationCode.append(randomChar);
        }
        return verificationCode.toString();
    }
}
