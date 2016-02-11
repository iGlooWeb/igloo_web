package igloo.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author Yikai Gong
 */

@Entity
@Table(name="state_record")
public class RecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="record_id")
    private Integer id;
    private String serial_number;
    private String zone_id;
    private String chip_id;
    private String model;
    private Double latitude;
    private Double longitude;
    private String suburb;
    private String language;
    private String country;
    private String timestamp;
    private Integer PWO;
    private Float TEM;
    private Float MIN;
    private Float MAX;
    private Integer THM;
    private Float FLM;
    private Float FAN;
    private Float AU1;
    private Float AU2;
    private Integer CLD;
    private Double HOO;
    private String TML;
    private String heater_name;
    private String dealer;
    private String installed_date;
    private String name1;
    private String name2;

    public String getChip_id() {
        return chip_id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setChip_id(String chip_id) {
        this.chip_id = chip_id;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getInstalled_date() {
        return installed_date;
    }

    public void setInstalled_date(String installed_date) {
        this.installed_date = installed_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPWO() {
        return PWO;
    }

    public void setPWO(Integer PWO) {
        this.PWO = PWO;
    }

    public Float getTEM() {
        return TEM;
    }

    public void setTEM(Float TEM) {
        this.TEM = TEM;
    }

    public Float getMIN() {
        return MIN;
    }

    public void setMIN(Float MIN) {
        this.MIN = MIN;
    }

    public Float getMAX() {
        return MAX;
    }

    public void setMAX(Float MAX) {
        this.MAX = MAX;
    }

    public Integer getTHM() {
        return THM;
    }

    public void setTHM(Integer THM) {
        this.THM = THM;
    }

    public Float getFLM() {
        return FLM;
    }

    public void setFLM(Float FLM) {
        this.FLM = FLM;
    }

    public Float getFAN() {
        return FAN;
    }

    public void setFAN(Float FAN) {
        this.FAN = FAN;
    }

    public Float getAU1() {
        return AU1;
    }

    public void setAU1(Float AU1) {
        this.AU1 = AU1;
    }

    public Float getAU2() {
        return AU2;
    }

    public void setAU2(Float AU2) {
        this.AU2 = AU2;
    }

    public Integer getCLD() {
        return CLD;
    }

    public void setCLD(Integer CLD) {
        this.CLD = CLD;
    }

    public Double getHOO() {
        return HOO;
    }

    public void setHOO(Double HOO) {
        this.HOO = HOO;
    }

    public String getTML() {
        return TML;
    }

    public void setTML(String TML) {
        this.TML = TML;
    }

    public String getHeater_name() {
        return heater_name;
    }

    public void setHeater_name(String heater_name) {
        this.heater_name = heater_name;
    }

    //            | serial_number | varchar(45) | NO   | MUL | NULL    |       |
//            | latitude      | double      | NO   |     | NULL    |       |
//            | longitude     | double      | NO   |     | NULL    |       |
//            | suburb        | varchar(45) | NO   |     | NULL    |       |
//            | language      | varchar(45) | NO   |     | NULL    |       |
//            | country       | varchar(45) | NO   |     | NULL    |       |
//            | timestamp     | datetime    | NO   | MUL | NULL    |       |
//            | PWO           | tinyint(4)  | NO   |     | NULL    |       |
//            | TEM           | float       | NO   |     | NULL    |       |
//            | MIN           | float       | NO   |     | NULL    |       |
//            | MAX           | float       | NO   |     | NULL    |       |
//            | THM           | tinyint(4)  | NO   |     | NULL    |       |
//            | FLM           | float       | NO   |     | NULL    |       |
//            | FAN           | float       | NO   |     | NULL    |       |
//            | AU1           | float       | NO   |     | NULL    |       |
//            | AU2           | float       | NO   |     | NULL    |       |
//            | CLD           | tinyint(4)  | NO   |     | NULL    |       |
//            | HOO           | double      | NO   |     | NULL    |       |
//            | TML           | varchar(45)
}
