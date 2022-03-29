package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("accountexpired")
        @Expose
        private Boolean accountexpired;
        @SerializedName("credentialsexpired")
        @Expose
        private Boolean credentialsexpired;
        @SerializedName("accountlocked")
        @Expose
        private Boolean accountlocked;
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;
        @SerializedName("mobilenumber")
        @Expose
        private String mobilenumber;
        @SerializedName("transactionpin")
        @Expose
        private String transactionpin;
        @SerializedName("roles")
        @Expose
        private List<Role> roles = null;
        @SerializedName("customerdetails")
        @Expose
        private Customerdetails customerdetails;
        @SerializedName("phonecode")
        @Expose
        private Integer phonecode;
        @SerializedName("securityanswer")
        @Expose
        private String securityanswer;
        @SerializedName("usertype")
        @Expose
        private String usertype;
        @SerializedName("storename")
        @Expose
        private Object storename;
        @SerializedName("latitude")
        @Expose
        private Float latitude;
        @SerializedName("longitude")
        @Expose
        private Float longitude;
        @SerializedName("screenlockpin")
        @Expose
        private Object screenlockpin;
        @SerializedName("question1")
        @Expose
        private Object question1;
        @SerializedName("answer1")
        @Expose
        private Object answer1;
        @SerializedName("question2")
        @Expose
        private Object question2;
        @SerializedName("answer2")
        @Expose
        private Object answer2;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getAccountexpired() {
            return accountexpired;
        }

        public void setAccountexpired(Boolean accountexpired) {
            this.accountexpired = accountexpired;
        }

        public Boolean getCredentialsexpired() {
            return credentialsexpired;
        }

        public void setCredentialsexpired(Boolean credentialsexpired) {
            this.credentialsexpired = credentialsexpired;
        }

        public Boolean getAccountlocked() {
            return accountlocked;
        }

        public void setAccountlocked(Boolean accountlocked) {
            this.accountlocked = accountlocked;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getTransactionpin() {
            return transactionpin;
        }

        public void setTransactionpin(String transactionpin) {
            this.transactionpin = transactionpin;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        public Customerdetails getCustomerdetails() {
            return customerdetails;
        }

        public void setCustomerdetails(Customerdetails customerdetails) {
            this.customerdetails = customerdetails;
        }

        public Integer getPhonecode() {
            return phonecode;
        }

        public void setPhonecode(Integer phonecode) {
            this.phonecode = phonecode;
        }

        public String getSecurityanswer() {
            return securityanswer;
        }

        public void setSecurityanswer(String securityanswer) {
            this.securityanswer = securityanswer;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public Object getStorename() {
            return storename;
        }

        public void setStorename(Object storename) {
            this.storename = storename;
        }

        public Float getLatitude() {
            return latitude;
        }

        public void setLatitude(Float latitude) {
            this.latitude = latitude;
        }

        public Float getLongitude() {
            return longitude;
        }

        public void setLongitude(Float longitude) {
            this.longitude = longitude;
        }

        public Object getScreenlockpin() {
            return screenlockpin;
        }

        public void setScreenlockpin(Object screenlockpin) {
            this.screenlockpin = screenlockpin;
        }

        public Object getQuestion1() {
            return question1;
        }

        public void setQuestion1(Object question1) {
            this.question1 = question1;
        }

        public Object getAnswer1() {
            return answer1;
        }

        public void setAnswer1(Object answer1) {
            this.answer1 = answer1;
        }

        public Object getQuestion2() {
            return question2;
        }

        public void setQuestion2(Object question2) {
            this.question2 = question2;
        }

        public Object getAnswer2() {
            return answer2;
        }

        public void setAnswer2(Object answer2) {
            this.answer2 = answer2;
        }

    }


    public class Role {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("createdOn")
        @Expose
        private Object createdOn;
        @SerializedName("modifiedOn")
        @Expose
        private Object modifiedOn;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Object createdOn) {
            this.createdOn = createdOn;
        }

        public Object getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(Object modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

    }


    public class Customerdetails {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("middlename")
        @Expose
        private Object middlename;
        @SerializedName("cardtoken")
        @Expose
        private Object cardtoken;
        @SerializedName("countryid")
        @Expose
        private Integer countryid;
        @SerializedName("stateid")
        @Expose
        private Integer stateid;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("cityname")
        @Expose
        private String cityname;
        @SerializedName("zipCode")
        @Expose
        private Object zipCode;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("currencyid")
        @Expose
        private Integer currencyid;
        @SerializedName("monthlyIncome")
        @Expose
        private Object monthlyIncome;
        @SerializedName("passportNumber")
        @Expose
        private Object passportNumber;
        @SerializedName("expiryDate")
        @Expose
        private Object expiryDate;
        @SerializedName("bankaccount")
        @Expose
        private Object bankaccount;
        @SerializedName("imageurl")
        @Expose
        private Object imageurl;
        @SerializedName("bankusername")
        @Expose
        private Object bankusername;
        @SerializedName("merchantqrcode")
        @Expose
        private Object merchantqrcode;
        @SerializedName("isdeal")
        @Expose
        private Object isdeal;
        @SerializedName("currencysymbol")
        @Expose
        private String currencysymbol;
        @SerializedName("customeraccount")
        @Expose
        private List<Customeraccount> customeraccount = null;
        @SerializedName("idPlastico")
        @Expose
        private Object idPlastico;
        @SerializedName("idAsociado")
        @Expose
        private Object idAsociado;
        @SerializedName("idCuenta")
        @Expose
        private Object idCuenta;
        @SerializedName("sourceOfIncome")
        @Expose
        private Object sourceOfIncome;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Object getMiddlename() {
            return middlename;
        }

        public void setMiddlename(Object middlename) {
            this.middlename = middlename;
        }

        public Object getCardtoken() {
            return cardtoken;
        }

        public void setCardtoken(Object cardtoken) {
            this.cardtoken = cardtoken;
        }

        public Integer getCountryid() {
            return countryid;
        }

        public void setCountryid(Integer countryid) {
            this.countryid = countryid;
        }

        public Integer getStateid() {
            return stateid;
        }

        public void setStateid(Integer stateid) {
            this.stateid = stateid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public Object getZipCode() {
            return zipCode;
        }

        public void setZipCode(Object zipCode) {
            this.zipCode = zipCode;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getCurrencyid() {
            return currencyid;
        }

        public void setCurrencyid(Integer currencyid) {
            this.currencyid = currencyid;
        }

        public Object getMonthlyIncome() {
            return monthlyIncome;
        }

        public void setMonthlyIncome(Object monthlyIncome) {
            this.monthlyIncome = monthlyIncome;
        }

        public Object getPassportNumber() {
            return passportNumber;
        }

        public void setPassportNumber(Object passportNumber) {
            this.passportNumber = passportNumber;
        }

        public Object getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Object expiryDate) {
            this.expiryDate = expiryDate;
        }

        public Object getBankaccount() {
            return bankaccount;
        }

        public void setBankaccount(Object bankaccount) {
            this.bankaccount = bankaccount;
        }

        public Object getImageurl() {
            return imageurl;
        }

        public void setImageurl(Object imageurl) {
            this.imageurl = imageurl;
        }

        public Object getBankusername() {
            return bankusername;
        }

        public void setBankusername(Object bankusername) {
            this.bankusername = bankusername;
        }

        public Object getMerchantqrcode() {
            return merchantqrcode;
        }

        public void setMerchantqrcode(Object merchantqrcode) {
            this.merchantqrcode = merchantqrcode;
        }

        public Object getIsdeal() {
            return isdeal;
        }

        public void setIsdeal(Object isdeal) {
            this.isdeal = isdeal;
        }

        public String getCurrencysymbol() {
            return currencysymbol;
        }

        public void setCurrencysymbol(String currencysymbol) {
            this.currencysymbol = currencysymbol;
        }

        public List<Customeraccount> getCustomeraccount() {
            return customeraccount;
        }

        public void setCustomeraccount(List<Customeraccount> customeraccount) {
            this.customeraccount = customeraccount;
        }

        public Object getIdPlastico() {
            return idPlastico;
        }

        public void setIdPlastico(Object idPlastico) {
            this.idPlastico = idPlastico;
        }

        public Object getIdAsociado() {
            return idAsociado;
        }

        public void setIdAsociado(Object idAsociado) {
            this.idAsociado = idAsociado;
        }

        public Object getIdCuenta() {
            return idCuenta;
        }

        public void setIdCuenta(Object idCuenta) {
            this.idCuenta = idCuenta;
        }

        public Object getSourceOfIncome() {
            return sourceOfIncome;
        }

        public void setSourceOfIncome(Object sourceOfIncome) {
            this.sourceOfIncome = sourceOfIncome;
        }

    }




    public class Customeraccount {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("accountnumber")
        @Expose
        private String accountnumber;
        @SerializedName("currentbalance")
        @Expose
        private Float currentbalance;
        @SerializedName("accountstatus")
        @Expose
        private String accountstatus;
        @SerializedName("reserveamount")
        @Expose
        private Float reserveamount;
        @SerializedName("currencyid")
        @Expose
        private Integer currencyid;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAccountnumber() {
            return accountnumber;
        }

        public void setAccountnumber(String accountnumber) {
            this.accountnumber = accountnumber;
        }

        public Float getCurrentbalance() {
            return currentbalance;
        }

        public void setCurrentbalance(Float currentbalance) {
            this.currentbalance = currentbalance;
        }

        public String getAccountstatus() {
            return accountstatus;
        }

        public void setAccountstatus(String accountstatus) {
            this.accountstatus = accountstatus;
        }

        public Float getReserveamount() {
            return reserveamount;
        }

        public void setReserveamount(Float reserveamount) {
            this.reserveamount = reserveamount;
        }

        public Integer getCurrencyid() {
            return currencyid;
        }

        public void setCurrencyid(Integer currencyid) {
            this.currencyid = currencyid;
        }

    }


}


/*
public class LoginResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("accountexpired")
        @Expose
        private Boolean accountexpired;
        @SerializedName("credentialsexpired")
        @Expose
        private Boolean credentialsexpired;
        @SerializedName("accountlocked")
        @Expose
        private Boolean accountlocked;
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;
        @SerializedName("mobilenumber")
        @Expose
        private String mobilenumber;
        @SerializedName("transactionpin")
        @Expose
        private String transactionpin;
        @SerializedName("roles")
        @Expose
        private List<Role> roles = null;
        @SerializedName("customerdetails")
        @Expose
        private Customerdetails customerdetails;
        @SerializedName("phonecode")
        @Expose
        private String phonecode;
        @SerializedName("securityanswer")
        @Expose
        private String securityanswer;
        @SerializedName("usertype")
        @Expose
        private String usertype;
        @SerializedName("storename")
        @Expose
        private String storename;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("screenlockpin")
        @Expose
        private String screenlockpin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getAccountexpired() {
            return accountexpired;
        }

        public void setAccountexpired(Boolean accountexpired) {
            this.accountexpired = accountexpired;
        }

        public Boolean getCredentialsexpired() {
            return credentialsexpired;
        }

        public void setCredentialsexpired(Boolean credentialsexpired) {
            this.credentialsexpired = credentialsexpired;
        }

        public Boolean getAccountlocked() {
            return accountlocked;
        }

        public void setAccountlocked(Boolean accountlocked) {
            this.accountlocked = accountlocked;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getTransactionpin() {
            return transactionpin;
        }

        public void setTransactionpin(String transactionpin) {
            this.transactionpin = transactionpin;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        public Customerdetails getCustomerdetails() {
            return customerdetails;
        }

        public void setCustomerdetails(Customerdetails customerdetails) {
            this.customerdetails = customerdetails;
        }

        public String getPhonecode() {
            return phonecode;
        }

        public void setPhonecode(String phonecode) {
            this.phonecode = phonecode;
        }

        public String getSecurityanswer() {
            return securityanswer;
        }

        public void setSecurityanswer(String securityanswer) {
            this.securityanswer = securityanswer;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getStorename() {
            return storename;
        }

        public void setStorename(String storename) {
            this.storename = storename;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getScreenlockpin() {
            return screenlockpin;
        }

        public void setScreenlockpin(String screenlockpin) {
            this.screenlockpin = screenlockpin;
        }

    }


    public class Role {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("createdOn")
        @Expose
        private String createdOn;
        @SerializedName("modifiedOn")
        @Expose
        private String modifiedOn;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

    }


    public class Customeraccount {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("accountnumber")
        @Expose
        private String accountnumber;
        @SerializedName("currentbalance")
        @Expose
        private Float currentbalance;
        @SerializedName("accountstatus")
        @Expose
        private String accountstatus;
        @SerializedName("reserveamount")
        @Expose
        private Integer reserveamount;
        @SerializedName("currencyid")
        @Expose
        private Integer currencyid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountnumber() {
            return accountnumber;
        }

        public void setAccountnumber(String accountnumber) {
            this.accountnumber = accountnumber;
        }

        public Float getCurrentbalance() {
            return currentbalance;
        }

        public void setCurrentbalance(Float currentbalance) {
            this.currentbalance = currentbalance;
        }

        public String getAccountstatus() {
            return accountstatus;
        }

        public void setAccountstatus(String accountstatus) {
            this.accountstatus = accountstatus;
        }

        public Integer getReserveamount() {
            return reserveamount;
        }

        public void setReserveamount(Integer reserveamount) {
            this.reserveamount = reserveamount;
        }

        public Integer getCurrencyid() {
            return currencyid;
        }

        public void setCurrencyid(Integer currencyid) {
            this.currencyid = currencyid;
        }

    }

    public class Customerdetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("middlename")
        @Expose
        private String middlename;
        @SerializedName("cardtoken")
        @Expose
        private String cardtoken;
        @SerializedName("countryid")
        @Expose
        private String countryid;
        @SerializedName("stateid")
        @Expose
        private String stateid;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("cityname")
        @Expose
        private String cityname;
        @SerializedName("zipCode")
        @Expose
        private String zipCode;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("currencyid")
        @Expose
        private String currencyid;
        @SerializedName("bankaccount")
        @Expose
        private String bankaccount;
        @SerializedName("imageurl")
        @Expose
        private String imageurl;
        @SerializedName("bankusername")
        @Expose
        private String bankusername;
        @SerializedName("merchantqrcode")
        @Expose
        private String merchantqrcode;
        @SerializedName("isdeal")
        @Expose
        private String isdeal;
        @SerializedName("currencysymbol")
        @Expose
        private String currencysymbol;
        @SerializedName("customeraccount")
        @Expose
        private List<Customeraccount> customeraccount = null;
        @SerializedName("idCuenta")
        @Expose
        private String idCuenta;
        @SerializedName("idAsociado")
        @Expose
        private String idAsociado;
        @SerializedName("idPlastico")
        @Expose
        private String idPlastico;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddlename() {
            return middlename;
        }

        public void setMiddlename(String middlename) {
            this.middlename = middlename;
        }

        public String getCardtoken() {
            return cardtoken;
        }

        public void setCardtoken(String cardtoken) {
            this.cardtoken = cardtoken;
        }

        public String getCountryid() {
            return countryid;
        }

        public void setCountryid(String countryid) {
            this.countryid = countryid;
        }

        public String getStateid() {
            return stateid;
        }

        public void setStateid(String stateid) {
            this.stateid = stateid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getCurrencyid() {
            return currencyid;
        }

        public void setCurrencyid(String currencyid) {
            this.currencyid = currencyid;
        }

        public String getBankaccount() {
            return bankaccount;
        }

        public void setBankaccount(String bankaccount) {
            this.bankaccount = bankaccount;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getBankusername() {
            return bankusername;
        }

        public void setBankusername(String bankusername) {
            this.bankusername = bankusername;
        }

        public String getMerchantqrcode() {
            return merchantqrcode;
        }

        public void setMerchantqrcode(String merchantqrcode) {
            this.merchantqrcode = merchantqrcode;
        }

        public String getIsdeal() {
            return isdeal;
        }

        public void setIsdeal(String isdeal) {
            this.isdeal = isdeal;
        }

        public String getCurrencysymbol() {
            return currencysymbol;
        }

        public void setCurrencysymbol(String currencysymbol) {
            this.currencysymbol = currencysymbol;
        }

        public List<Customeraccount> getCustomeraccount() {
            return customeraccount;
        }

        public void setCustomeraccount(List<Customeraccount> customeraccount) {
            this.customeraccount = customeraccount;
        }

        public String getIdCuenta() {
            return idCuenta;
        }

        public void setIdCuenta(String idCuenta) {
            this.idCuenta = idCuenta;
        }

        public String getIdAsociado() {
            return idAsociado;
        }

        public void setIdAsociado(String idAsociado) {
            this.idAsociado = idAsociado;
        }

        public String getIdPlastico() {
            return idPlastico;
        }

        public void setIdPlastico(String idPlastico) {
            this.idPlastico = idPlastico;
        }

    }


}

*/
