package packet;

import java.io.Serializable;

public class ServerClosure implements Serializable {
    private static final long serialVersionUID = 1L;

    public String message;

    public ServerClosure() {

    }

    public ServerClosure(String message) {
        this.message = message;
    }

}
