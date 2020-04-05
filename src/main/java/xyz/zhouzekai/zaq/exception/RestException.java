package xyz.zhouzekai.zaq.exception;


public class RestException extends Exception {
    private CODE code;
    public RestException(CODE code, String Message){
        super(Message);
        this.code = code;
    }
    public int getCode() {
        return code.getValue();
    }
}
