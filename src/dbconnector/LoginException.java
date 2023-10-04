package dbconnector;

public class LoginException extends Exception {

	public LoginException()
    {
        super();
    }

    public LoginException(final String argMessage, final Throwable argCause)
    {
        super(argMessage, argCause);
    }

    public LoginException(final String argMessage)
    {
        super(argMessage);
    }

    public LoginException(final Throwable argCause)
    {
        super(argCause);
    }
}
