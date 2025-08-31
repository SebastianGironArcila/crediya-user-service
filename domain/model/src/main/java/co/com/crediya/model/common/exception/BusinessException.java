package co.com.crediya.model.common.exception;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {
        EMAIL_ALREADY_REGISTERED("Email is already registered"),
        ROLE_NOT_FOUND("Role not found"),
        USER_NOT_FOUND("User not found");


        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build() {
            return new BusinessException(this);
        }

        public Supplier<Throwable> defer() {
            return () -> new BusinessException(this);
        }

        Type(String message) {
            this.message = message;
        }
    }

    private final Type type;

    public BusinessException(Type type){
        super(type.getMessage());
        this.type = type;
    }

    @Override
    public String getCode(){
        return type.name();
    }
}
