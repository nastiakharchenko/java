package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import exception.BlankFieldException;
import exception.FieldParsingException;
import exception.MyCustomException;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static config.Constants.*;

@Log4j
public abstract class Organization implements Comparable<Organization> {
    @ToString.Exclude final private static String[] entityProperties = {NAME, ADDRESS, EMPLOYEES_COUNT, CREATION_DATE, MFO, SITE, COMMERCIALLY};
    @ToString.Exclude private static int[] indexProperties;

    private String name;
    private String address;
    private Integer employeesCount;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    private Integer mfo;
    private String site;
    private Boolean commercially;

    public Organization(final String properties) {
        if (indexProperties == null || indexProperties.length != PROPERTIES_COUNT) {
            throw new MyCustomException(WRONG_FILE_HEADER);
        }
        final String[] splitProps = properties.replaceAll(SPACE, EMPTY_LINE).split(COMMA);
        if (splitProps.length == PROPERTIES_COUNT) {
            this.setName(splitProps[indexProperties[0]]);
            this.setAddress(splitProps[indexProperties[1]]);
            this.setEmployeesCount(splitProps[indexProperties[2]]);
            this.setCreationDate(splitProps[indexProperties[3]]);
            this.setMfo(splitProps[indexProperties[4]]);
            this.setSite(splitProps[indexProperties[5]]);
            this.setCommercially(splitProps[indexProperties[6]]);
        } else {
            throw new MyCustomException(CAN_NOT_PARSE_LINE + properties);
        }
    }

    /**
     *Function filling the array of indexes:
     */
    public static Boolean resolveProperties(final String props) {
        String[] entityPropertiesWrite = props.replaceAll(SPACE, EMPTY_LINE).split(COMMA);
        if(entityPropertiesWrite.length != PROPERTIES_COUNT){
            return false;
        }
        indexProperties = new int[entityProperties.length];
        for(int j = 0; j < entityProperties.length; j++){
            for(int i = 0; i < entityProperties.length; i++){
                if(entityProperties[j].equalsIgnoreCase(entityPropertiesWrite[i])){
                    indexProperties[j] = i;
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public int compareTo(final Organization o) {
        final int i = this.name.compareToIgnoreCase(o.getName());
        return i != 0 ? i : this.address.compareToIgnoreCase(o.getAddress());
    }

    @Override
    public String toString() {
        return "name=" + name + ", address=" + address + ", employeesCount=" + employeesCount
                + ", creationDate=" + creationDate + ", mfo=" + mfo + ", site=" + site + ", commercially=" + commercially;
    }

    public String toJson() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            log.error(CONVERT_TO_JSON_ERROR_MESSAGE + this.toString());
            log.error(e.getMessage());
        }
        return EMPTY_LINE;
    }

//    public String toJson() {
//        return "{" + "\"name\": " + "\"" + name + "\"" + ",\"address\":" + "\"" + address + "\"" + ",\"employeesCount\":" + employeesCount +
//        ",\"creationDate\":" + "[" + getCreationDateStr() + "]" + ",\"mfo\":" + mfo + ",\"site\":" + "\"" + site + "\"" + ",\"commercially\":" + commercially + "}";
//    }

    //-----------------------GETTERS-----------------------

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public Integer getEmployeesCount(){
        return employeesCount;
    }

    public LocalDate getCreationDate(){
        return creationDate;
    }

//    public String getCreationDateStr(){
//        String form = "dd,MM,yyyy";
//        return form.format(String.valueOf(creationDate));
//    }

    public Integer getMfo(){
        return mfo;
    }

    public String getSite(){
        return site;
    }

    public Boolean getCommercially(){
        return commercially;
    }

    //-----------------------SETTERS-----------------------

    public void setName(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new BlankFieldException(NAME);
        }
        this.name = name;
    }

    public void setAddress(final String address) {
        if (StringUtils.isBlank(address)) {
            throw new BlankFieldException(ADDRESS);
        }
        this.address = address;
    }

    public void setEmployeesCount(final Integer employeesCountInt){
        if(employeesCountInt > 0){
            this.employeesCount = employeesCountInt;
        }
    }

    public void setEmployeesCount(final String employeesCount) {
        try {
            setEmployeesCount(Integer.parseInt(employeesCount));
        } catch (final NumberFormatException e) {
            throw new FieldParsingException(EMPLOYEES_COUNT);
        }
    }

    public void setCreationDate(final LocalDate dateLocDate){
        this.creationDate = dateLocDate;
    }

    public void setCreationDate(final String creationDate) {
        try {
            setCreationDate(LocalDate.parse(creationDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (final DateTimeParseException e) {
            throw new FieldParsingException(CREATION_DATE);
        }
    }

    public void setMfo(final Integer mfoInt) {
        if(mfoInt > 0){
            this.mfo = mfoInt;
        }
    }

    public void setMfo(final String mfoStr) {
        try {
            setMfo(Integer.parseInt(mfoStr));
        } catch (final NumberFormatException e) {
            throw new FieldParsingException(MFO);
        }
    }

    public void setSite(final String site) {
        if (StringUtils.isBlank(site)) {
            throw new BlankFieldException(SITE);
        }
        if (!site.matches(DOMAIN_REGEX)) {
            throw new FieldParsingException(SITE);
        }
        this.site = site;
    }

    public void setCommercially(final boolean commerciallyBool){
        this.commercially = commerciallyBool;
    }

    public void setCommercially(final String commercially) {
        if (StringUtils.isBlank(commercially)) {
            throw new BlankFieldException(COMMERCIALLY);
        } else if (commercially.equalsIgnoreCase("true")
                || commercially.equalsIgnoreCase("1")){
            setCommercially(true);
        } else if (commercially.equalsIgnoreCase("false")
                || commercially.equalsIgnoreCase("0")) {
            setCommercially(false);
        } else {
            throw new FieldParsingException(COMMERCIALLY);
        }
    }
}