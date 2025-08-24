package co.com.crediya.model.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {
        FIRSTNAME_REQUIRED("First name is required"),
        LASTNAME_REQUIRED("Last name is required"),
        EMAIL_REQUIRED("Email is required"),
        INVALID_FORMAT("Invalid email format"),
        INVALID_SALARY("Salary must be between 0 and 15,000,000"),
        EMAIL_ALREADY_REGISTERED("Email is already registered"),
        BIRTHDATE_REQUIRED("Birth date is required");

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
