package live.codeland.petsguidesbackend.helpers;

public class CustomJwtException extends RuntimeException{
    public CustomJwtException(String message){
        super(message);
    }

    public boolean isExpired(){
        return getMessage().contains("expired");
    }
}

