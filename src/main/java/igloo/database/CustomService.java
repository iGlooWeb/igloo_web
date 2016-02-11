package igloo.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Yikai Gong
 */

public class CustomService {
    public MysqlDataSource ds =null;

    public CustomService(){
        // read the parameter from .properties
        ds = new MysqlDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/igloo");
        ds.setUser("darcular");
        ds.setPassword("");

    }

    public JsonArray getOperationHourPerDayStartat(String chipId, String startDateStr){
        JsonArray jsonArray = new JsonArray();
        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        try{
            connection = ds.getConnection();
            stm = connection.createStatement();
//            System.out.println("select * from (select cast(timestamp as DATE) as date, sum(HOO) as count from state_record where chip_id="+ chipId +" group by date order by date desc limit 7) as t where date>="+startDateStr);
            rs = stm.executeQuery("select * from (select cast(timestamp as DATE) as date, sum(HOO) as count from state_record where chip_id=\""+ chipId +"\" group by date order by date desc limit 7) as t where date>="+startDateStr);
            while(rs.next()){
                String date = rs.getString("date");
                Double hours = rs.getDouble("count");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("date", date);
                jsonObject.addProperty("hours", hours);
                jsonArray.add(jsonObject);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public JsonArray getOperationHourPerMonthAtYear(String serial_num, String year){
        JsonArray jsonArray = new JsonArray();
        try{
            Connection connection = ds.getConnection();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("select monthname(timestamp),sum(HOO) from state_record where year(timestamp)="+ year +" group by month(timestamp)");
            while(rs.next()){
                String date = rs.getString("year");
                Double hours = rs.getDouble("count");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("year", date);
                jsonObject.addProperty("hours", hours);
                jsonArray.add(jsonObject);
            }
            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }
}
