package xyz.zhouzekai.zaq.exception;

public enum CODE {
    OK(200),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    InternalServerError(500);

    private final int value;
    private CODE(int value){
        this.value  = value;
    }

    public int getValue() {
        return value;
    }
}
