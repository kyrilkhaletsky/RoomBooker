import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class Prog {

    // 2-Tuple Class
    public static class Tuple<S, T>
    {
        private S a;
        private T b;

        public Tuple(S a, T b) {
            this.a = a;
            this.b = b;
        }

        public S getA() {
            return a;
        }

        public void setA(S a) {
            this.a = a;
        }

        public T getB() {
            return b;
        }

        public void setB(T b) {
            this.b = b;
        }
    }

    public static void main(String [] args){

        //directory of the CSV, please change this to your directory if testing on another machine.
        String csvFile = "C:/Users/zarre/Desktop/Home/src/book.csv";


        //TESTS ON THE FULL DATABASE
        //TEST1
        String result = GetRooms(7, "01/04/2019", "08/04/2019", csvFile, true);
        String expected = "[120, TWINTRIPLE, 3 guests, 210] [119, TWINTRIPLE, 2 guests, 140] [118, TWINTRIPLE, 2 guests, 140] = 490\n" +
                "[117, TWINTRIPLE, 3 guests, 210] [116, TWINTRIPLE, 2 guests, 140] [115, TWINTRIPLE, 2 guests, 140] = 490\n" +
                "[114, TWINTRIPLE, 3 guests, 210] [113, TWINTRIPLE, 2 guests, 140] [112, TWINTRIPLE, 2 guests, 140] = 490\n";
        assertEquals(result, expected);

        //TEST2
        result = GetRooms(1, "01/04/2019", "08/04/2019", csvFile, true);
        expected = "[111, TWIN, 1 guests, 95] = 95\n" +
                "[110, TWIN, 1 guests, 95] = 95\n" +
                "[109, TWIN, 1 guests, 95] = 95\n";
        assertEquals(result, expected);

        //TEST3
        result = GetRooms(0, "01/04/2019", "08/04/2019", csvFile, true);
        expected = "= 0\n" +
                "= 0\n" +
                "= 0\n";
        assertEquals(result, expected);



        //TESTS ON A SMALLER DATABASE TO SIMULATE OTHER ROOM COMBINATIONS
        //TEST4
        result = GetRooms(7, "01/04/2019", "08/04/2019", csvFile, false);
        expected = "[73, TWIN, 2 guests, 190] [72, TWINTRIPLE, 3 guests, 210] [71, TWIN, 2 guests, 190] = 590\n" +
                "[75, FAMILY, 4 guests, 400] [74, FAMILY, 3 guests, 400] = 800\n" +
                "= 0\n";
        assertEquals(result, expected);


        //TEST5
        result = GetRooms(3, "01/04/2019", "08/04/2019", csvFile, false);
        expected = "[72, TWINTRIPLE, 3 guests, 210] = 210\n" +
                "[73, TWIN, 2 guests, 190] [71, TWIN, 1 guests, 95] = 285\n" +
                "[75, FAMILY, 3 guests, 400] = 400\n";
        assertEquals(result, expected);
    }


    public static String GetRooms(int customerNumber, String startDate, String endDate, String csvFile, boolean populateBool){
        List<Room> roomList = CSVtoList(csvFile);

        if(populateBool){
            roomList = Populate(roomList);

        }

        List<List<String>> cheapestRooms = new ArrayList<>();
        List<Integer> total = new ArrayList<>();

        //Gets the first 3 cheapest combinations
        for (int i = 0; i < 3; ++i) {
            List<Tuple<Room, Integer>> currSolution = CalculateCheapest(roomList,customerNumber, startDate, endDate);
            Tuple<List<String>, Integer> formattedSolution = getSolutionStrings(currSolution);
            cheapestRooms.add(formattedSolution.getA());
            total.add(formattedSolution.getB());
        }

        String finalResult = "";
        for (int i = 0; i < cheapestRooms.size(); ++i) {
            for (int j = 0; j < cheapestRooms.get(i).size(); ++j) {
                finalResult = finalResult + cheapestRooms.get(i).get(j) + " ";

            }
            finalResult = finalResult + "= " + total.get(i) + "\n";

        }
        //Print and return result for testing in Main
        System.out.println(finalResult);
        return finalResult;
    }

    //Returns a String of data so it can be used to print to terminal, this step can be skipped if we need to use the data otherwise
    public static Tuple<List<String>, Integer> getSolutionStrings(List<Tuple<Room, Integer>> solution) {
        List<String> list = new ArrayList<>();
        int total = 0;

        for (Tuple<Room, Integer> p : solution) {
            Room r = p.getA();
            Integer g = p.getB();

            int price = r.GetPriceModel().equals("pp") ? g * r.GetPrice() : r.GetPrice();
            total += price;

            list.add("[" + r.GetReference() + ", " + r.GetRoomType() + ", " + g + " guests, " + price + "]");
        }

        return new Tuple<>(list, total);
    }

    // Edits roomlist (removes one room) as a side-effect
    public static ArrayList<Tuple<Room, Integer>> CalculateCheapest(List<Room> roomList, int customerNumber, String startDate, String endDate) {

        //Skip all the incompatible dates
        ArrayList<Room> list = roomList.stream()
                .filter(room -> room.GetStartDate().equals(startDate) && room.GetEndDate().equals(endDate))
                .collect(Collectors.toCollection(ArrayList::new));

        final int INF = 99999999;
        int[][] dp = new int[customerNumber + 1][list.size() + 1];
        int[][][] par = new int[customerNumber + 1][list.size() + 1][2];

        for (int i = 0; i <= list.size(); ++i) {
            par[0][i][0] = -1;
            par[0][i][1] = -1;
        }

        for (int i = 0; i <= customerNumber; ++i) {
            par[i][0][0] = -1;
            par[i][0][1] = -1;
        }

        // Fill dp with INF
        for (int i = 0; i <= customerNumber; ++i) {
            Arrays.fill(dp[i], INF);
        }

        // Base Cases
        // For 0 rooms, the min price is always INF (impossible to accommodate)
        // For 0 customers, the min price is always 0
        for (int i = 0; i <= list.size(); ++i) {
            dp[0][i] = 0;
        }

        // Dynamic Programming, Bottom Up Solution
        for (int i = 1; i <= customerNumber; ++i) {
            for (int j = 1; j <= list.size(); ++j) {
                Room currRoom = list.get(j - 1);

                dp[i][j] = dp[i][j - 1];

                par[i][j][0] = i;
                par[i][j][1] = j - 1;
                for (int k = currRoom.GetMinGuests(); k <= currRoom.GetMaxGuests(); ++k) {
                    if (i - k < 0) break;

                    //Get price per person
                    int currPrice = currRoom.GetPriceModel().equals("pp") ? k * currRoom.GetPrice() : currRoom.GetPrice();

                    if (dp[i - k][j - 1] + currPrice <= dp[i][j]) {
                        dp[i][j] = dp[i - k][j - 1] + currPrice;

                        par[i][j][0] = i - k;
                        par[i][j][1] = j - 1;
                    }
                }
            }
        }

        // Rebuild Solution from DP Table
        ArrayList<Tuple<Room, Integer>> chosen = new ArrayList<>();

        int y = customerNumber;
        int x = list.size();

        while (y > 0 && x > 0 && par[y][x][0] != -1) {

            if (y != par[y][x][0]) {
                chosen.add(new Tuple<>(list.get(x - 1), y - par[y][x][0]));
                roomList.remove(list.get(x - 1));
            }

            y = par[y][x][0];
            x = par[y][x][1];
        }

        return chosen;
    }

    //populates the rest of the Room class with 55 other rooms as per spec
    public static List<Room> Populate(List<Room> roomList){
        Room myRoom;
        int nextRef = roomList.get(roomList.size()-1).GetReference() + 1;

        int doubleCount = 0;
        int twinCount = 0;
        int twintripleCount = 0;
        int familyCount = 0;

        for(Room rooms : roomList){
            String type = rooms.GetRoomType();

            if(type.equals("DOUBLE")) {
                doubleCount++;
            } else if(type.equals("TWIN")) {
                twinCount++;
            } else if(type.equals("TWINTRIPLE")) {
                twintripleCount++;
            } else if(type.equals("FAMILY")) {
                familyCount++;
            }
        }

        while(doubleCount < 20){
            myRoom = new Room(nextRef, "DOUBLE", "01/04/2019", "08/04/2019", 2, 2, 120, "pp");
            roomList.add(myRoom);
            nextRef++;
            doubleCount++;
        }

        while(twinCount < 20){
            myRoom = new Room(nextRef, "TWIN", "01/04/2019", "08/04/2019", 1, 2, 95, "pp");
            roomList.add(myRoom);
            nextRef++;
            twinCount++;
        }

        while(twintripleCount < 10){
            myRoom = new Room(nextRef, "TWINTRIPLE", "01/04/2019", "08/04/2019", 2, 3, 70, "pp");
            roomList.add(myRoom);
            nextRef++;
            twintripleCount++;
        }

        while(familyCount < 5){
            myRoom = new Room(nextRef, "FAMILY", "01/04/2019", "08/04/2019", 2, 4, 400, "pu");
            roomList.add(myRoom);
            nextRef++;
            familyCount++;
        }

        return roomList;
    }

    //converts CSV data to an object
    public static List<Room> CSVtoList(String csvFile){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int lineNum = 0;

        Room myRoom;
        List<Room> roomList = new ArrayList<Room>();

        //skips the first line and parses the CSV based on comma separator
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                if (lineNum != 0){
                    String[] room = line.split(cvsSplitBy);
                    myRoom = new Room(Integer.parseInt(room[0]),room[1],room[2],room[3],Integer.parseInt(room[4]),Integer.parseInt(room[5]),Integer.parseInt(room[6]),room[7]);
                    roomList.add(myRoom);
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return roomList;
    }
}


