package team3.database.DBSearch;

public class SearchContext {
    private DBSearch dbSearch;

    public void setSearchStrat(DBSearch searchStrat){
        this.dbSearch = searchStrat;
    }

    public void executeSearch(){
        dbSearch.dbSearch();
    }
}
