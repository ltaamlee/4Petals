package fourpetals.com.dto.response.stats;

public record CustomerStatsResponse(
	    long total,
	    long male,
	    long female,
	    long rankThuong,
	    long rankBac,
	    long rankVang,
	    long rankKimCuong
	) {}