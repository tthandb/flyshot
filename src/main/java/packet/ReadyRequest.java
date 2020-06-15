package packet;

import java.io.Serializable;

public class ReadyRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public boolean isReady;
    public int position;
    public ReadyRequest(int id, boolean isReady){
        this.id = id;
        this.isReady=isReady;
    }
}
