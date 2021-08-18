package data.clickhouse.exception;


public class ParameterException extends RuntimeException {
    private String message;
    public ParameterException(){
        super();
    }
    public ParameterException(String message){
        super(message);
        this.message=message;
    }
}
