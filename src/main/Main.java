package main;

import main.Database.Database;
import main.Panels.UserPanel;

import javax.xml.crypto.Data;

public class Main
{
    public static void main(String[] args)
    {
        Database db = new Database();
        Application.appMain(db);

    }
}