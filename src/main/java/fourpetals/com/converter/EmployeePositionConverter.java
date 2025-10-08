package fourpetals.com.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import fourpetals.com.enums.EmployeePosition;

@Converter(autoApply = false) // bật = true nếu muốn áp toàn cục
public class EmployeePositionConverter implements AttributeConverter<EmployeePosition, String> {

    @Override
    public String convertToDatabaseColumn(EmployeePosition attr) {
        if (attr == null) return null;
        return switch (attr) {
            case MANAGER -> "MANAGER";
            case SALES_EMPLOYEE -> "SALES";         // MAP ĐÚNG THEO CHECK DB
            case INVENTORY_EMPLOYEE -> "INVENTORY";
            case SHIPPER -> "SHIPPER";
        };
    }

    @Override
    public EmployeePosition convertToEntityAttribute(String db) {
        if (db == null) return null;
        String v = db.trim().toUpperCase();        // đề phòng space/khác hoa thường
        return switch (v) {
            case "MANAGER" -> EmployeePosition.MANAGER;
            case "SALES" -> EmployeePosition.SALES_EMPLOYEE;
            case "INVENTORY" -> EmployeePosition.INVENTORY_EMPLOYEE;
            case "SHIPPER" -> EmployeePosition.SHIPPER;
            default -> throw new IllegalArgumentException("Unknown chuc_vu: " + db);
        };
    }
}
