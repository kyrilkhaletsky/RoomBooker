
public class Room {
    
    int reference;
    String roomType;
    String startDate;
    String endDate;
    int minGuests;
    int maxGuests;
    int price;
    String priceModel;

    public Room(int reference, String roomType, 
                String startDate, String endDate, 
                int minGuests, int maxGuests, 
                int price, String priceModel) {
        this.reference = reference;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.price = price;
        this.priceModel = priceModel;
        }
    
    public int GetReference(){
        return reference;
    }

    public String GetRoomType(){
        return roomType;
    }

    public String GetStartDate(){
        return startDate;
    }

    public String GetEndDate(){
        return endDate;
    }

    public int GetMinGuests(){
        return minGuests;
    }

    public int GetMaxGuests(){
        return maxGuests;
    }

    public int GetPrice(){
        return price;
    }

    public String GetPriceModel(){
        return priceModel;
    }

    public void SetReference(int reference){
        this.reference = reference;
    }

    public void SetRoomType(String roomType){
        this.roomType = roomType;
    }

    public void SetStartDate(String startDate){
        this.startDate = startDate;
    }

    public void SetEndDate(String endDate){
        this.endDate = endDate;
    }

    public void SetMinGuests(int minGuests){
        this.minGuests = minGuests;
    }

    public void SetMaxGuests(int maxGuests){
        this.maxGuests = maxGuests;
    }

    public void SetPrice(int price){
        this.price = price;
    }

    public void SetPriceModel(String priceModel){
        this.priceModel = priceModel;
    }

}