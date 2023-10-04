package dbconnector;

import java.sql.SQLException;
import java.sql.Statement;

public class Program {

	public static void main(String[] args) {
		Statement stmt=null;
		try {
		EnterDatabase.lgn = EnterDatabase.login();	//restituisce, dopo l'autenticazione la classe Connection
			try {	
				stmt=EnterDatabase.lgn.createStatement(); //genero lo statement utile per effettuare tutte le query
			}
			catch(SQLException e)
			{
				System.out.println(e.toString());
			}
		}
		catch(LoginException e)
		{
			System.out.println(e.toString());
			System.exit(0);				//in caso avvenga la LoginException (numero massimo di tentativi raggiunti) il programma si arresta
		}
		while(true)
			EnterDatabase.chooseQuery(0, 0, 0, stmt);	//permette all'utente di scegliere le query
	}
	

}
