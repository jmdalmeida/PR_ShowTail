package Utils.Data;

public class SearchFilter {
    
    private final String filterUsed, filterValue;

    public SearchFilter(String filterUsed, String filterValue) {
        this.filterUsed = filterUsed;
        this.filterValue = filterValue;
    }

    public String getFilterUsed() {
        return filterUsed;
    }

    public String getFilterValue() {
        return filterValue;
    }
    
}
