package fourpetals.com.dto.response.customers;

public record CustomerStatsResponse(
        long total,
        long newMonth,
        long male,
        long female
) {}
