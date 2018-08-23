/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzukisqllite;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author moyashi
 */
public class Person {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty address;
    private StringProperty mail;
    private StringProperty tell;
    private StringProperty memo;
    
    public Person(int anId, String aName, String anAddress, String aMail, String aTell, String aMemo){
        this.id = new SimpleIntegerProperty(anId);
        this.name = new SimpleStringProperty(aName);
        this.address = new SimpleStringProperty(anAddress);
        this.mail = new SimpleStringProperty(aMail);
        this.tell = new SimpleStringProperty(aTell);
        this.memo = new SimpleStringProperty(aMemo);
    }
    
    
    //ゲッター
    public IntegerProperty idProperty(){
        return this.id;
    }
    public void setId(int id){
        this.id.set(id);
    }
    
    public StringProperty nameProperty(){
        return name;
    }
    public void setName(String name){
        this.name.set(name);
    }

    public StringProperty addressProperty(){
        return address;
    }
    public void setAddress(String address){
        this.address.set(address);
    }

    public StringProperty mailProperty(){
        return mail;
    }
    public void setMail(String mail){
        this.mail.set(mail);
    }
    
    public StringProperty tellProperty(){
        return tell;
    }
    public void  setTell(String tell){
        this.tell.set(tell);
    }
    
    public StringProperty memoProperty(){
        return memo;
    }
    public void setMemo(String memo){
        this.memo.set(memo);
    }
    
}
