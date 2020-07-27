package ru.dragonestia.store.database;

import org.sql2o.Sql2o;

public class DataBase {

    public final Sql2o driver;

    public DataBase(String host, String user, String password, String base, int port){
        driver = new Sql2o("jdbc:mysql://" + host + ":" + port + "/" + base + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", user, password);
    }

}
