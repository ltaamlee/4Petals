package fourpetals.com.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import fourpetals.com.enums.EmployeePosition;

@Component
public class StringToEmployeePositionConverter implements Converter<String, EmployeePosition> {
    @Override
    public EmployeePosition convert(String source) {
        if (source == null || source.isBlank()) return null;
        return EmployeePosition.valueOf(source);
    }
}
