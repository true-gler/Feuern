package se2.groupa.feuern.network.classes;

import java.io.Serializable;

/**
 * Created by Lukas on 19.04.15.
 *
 * Object for sending within Socket-Communication
 */
public class NetworkMessage implements Serializable {

    private Status status;
    private String message;
    private Object parameter;

    public NetworkMessage(Status status, String message, Object parameter)
    {
        setStatus(status);
        setMessage(message);
        setParameter(parameter);
    }

    @Override
    public String toString()
    {
        return "[" + getStatus().toString() + "] " + getMessage();
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Status getStatus()
    {
        return this.status;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setParameter(Object parameter)
    {
        this.parameter = parameter;
    }

    public Object getParameter()
    {
        return this.parameter;
    }

    public enum Status
    {
        OK,
        ERROR,
        INFO,
        REQUEST
    }
}
