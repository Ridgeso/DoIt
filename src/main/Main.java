package main;

import main.Database.Database;
import main.Panels.UserPanel;

public class Main
{
    public static void main(String[] args)
    {
        Database d = new Database();
        UserPanel userPanel=new UserPanel(1);
        Application.appMain();
    }
}