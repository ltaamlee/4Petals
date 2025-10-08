package fourpetals.com.model;

public enum CustomerRank {
    THUONG("Thường"), BAC("Bạc"), VANG("Vàng"), KIM_CUONG("Kim Cương");

    public final String display;
    CustomerRank(String d){ this.display = d; }

    public static CustomerRank ofTotalOrders(long total){
        if (total >= 30) return KIM_CUONG;
        if (total >= 15) return VANG;
        if (total >= 5)  return BAC;
        return THUONG;
    }
}
