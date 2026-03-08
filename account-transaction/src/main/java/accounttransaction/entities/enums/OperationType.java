package accounttransaction.entities.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OperationType {
    DEPOSITED("DEPOSITED"),
    NEUTRAL("NEUTRAL"),
    WITHDRAWAL("WITHDRAWAL");

    private String operationType;

    OperationType(String value) {
        this.operationType = value;
    }

    // get description
    @Override
    @JsonValue
    public String toString() {
        return operationType;
    }

    @JsonCreator
    public static OperationType fromValue(String value) {
        for (OperationType accountType : OperationType.values()) {
            if (accountType.operationType.equalsIgnoreCase(value)) {
                return accountType;
            }
        }
        return null;
    }
}