package fourpetals.com.dto.response.dashboard;

import java.util.List;

public class ChartDataResponse {
    private String type;             // revenue | employees | customers | orders
    private List<String> labels;     // trục X (ngày/tháng)
    private List<Number> values;     // trục Y
    // getters/setters
    public String getType(){ return type; }
    public void setType(String t){ this.type = t; }
    public List<String> getLabels(){ return labels; }
    public void setLabels(List<String> l){ this.labels = l; }
    public List<Number> getValues(){ return values; }
    public void setValues(List<Number> v){ this.values = v; }
}
