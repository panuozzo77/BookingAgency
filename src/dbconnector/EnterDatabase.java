package dbconnector; 
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
	
public class EnterDatabase {

static Scanner scan;
static Connection lgn;
	
public static Connection login() throws LoginException {
	  int attempts = 3;
	  Connection con = null;
	  scan = new Scanner(System.in);
	  while(attempts>0) {
		  System.out.print("Enter name: ");
		  String name = scan.nextLine();
		  System.out.print("Enter password: ");
		  String password = scan.nextLine();
		  try {
		  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProgettoMP", name, password);
		  }
		  catch (SQLException e) {
			  System.out.println(e.toString());
			  attempts--;
			  System.out.println(" Error. " +attempts+ " Attempts remaining. Retry");
			  if(attempts==0) {
				  throw new LoginException("Maximum number of attempts reached. Closing...");
			  }
		  }
	      if(con!=null) {
	      System.out.println("Welcome " + name + "!");
	      
	      return con;
	      }
	  }
	  
	  return con;
}

//una chiamata nel Main lasciando la scelta all'utente sarà così strutturata: chooseQuery(0, 0, 0)
public static void chooseQuery(int MainMenu, int SubLevel1, int SubLevel2, Statement stmt)
{
	  if(MainMenu==0) {
	  scan = new Scanner(System.in);
	  System.out.println("\nScegliere la tipologia di Query da effettuare: ");
	  System.out.println("1. Registrazione");
	  System.out.println("2. Visualizzazione");
	  System.out.println("3. Uscita\n");
	  MainMenu = scan.nextInt();
	  }
	  switch(MainMenu) {
  		case 1: //Registrazioni
			  if(SubLevel1==0) {
			  scan = new Scanner(System.in);
			  System.out.println("\n1. Inserimento nuovo Cliente");
			  System.out.println("2. Inserimento Struttura Ricettiva");
			  System.out.println("3. Inserimento nuova Prenotazione");
			  System.out.println("4. Inserimento Tessera Premium");
			  System.out.println("5. INDIETRO\n");
			  SubLevel1 = scan.nextInt();
			  }
			  switch(SubLevel1) {
			  	case 1: 
			  		queriesOnly.newClient(stmt); //fatto
			  		break;
			  	case 2:
			  		queriesOnly.newStructure(stmt);
			  		break;
			  	case 3:
			  		queriesOnly.newReservation(stmt);
			  		break;
			  	case 4: 
			  		queriesOnly.newPremium(stmt);
			  	case 5:
			  		chooseQuery(0, 0, 0, stmt); //ritornare al MainMenu
			  	default:
			  		chooseQuery(1, 0, 0, stmt); //Ti riporta a MainMenu>Registrazioni
			  }			  
		break;
		
  		case 2: //Visualizzazioni
			  if(SubLevel1==0) {
				  scan = new Scanner(System.in);
				  System.out.println("\n1. Strutture");
				  System.out.println("2. Prenotazioni");	//FATTO
				  System.out.println("3. Clienti"); 		//FATTO
				  System.out.println("4. Agenzie"); 		//FATTO
				  System.out.println("5. INDIETRO\n");
				  SubLevel1 = scan.nextInt();
			  }
			  switch(SubLevel1) {
			  	case 1:
			  		queriesOnStructures(SubLevel2, stmt);
			  		chooseQuery(0, 0, 0, stmt); //ritornare al MainMenu
			  		break;
			  	case 2:
			  		queriesOnReservations(SubLevel2, stmt);   
			  		chooseQuery(0, 0, 0, stmt); //ritornare al MainMenu
			  		break;
			  	case 3:
			  		queriesOnClients(SubLevel2, stmt);
			  		chooseQuery(0, 0, 0, stmt); //ritornare al MainMenu
			  		break;
			  	case 4:
			  		queriesOnAgencies(SubLevel2, stmt);
			  		chooseQuery(0, 0, 0, stmt); //ritornare al MainMenu
			  		break;
			  	case 5:
			  		chooseQuery(0, 0, 0, stmt); //ritornare a MainMenu
			  		break;
			  	default:
			  		chooseQuery(2, 0, 0, stmt); //Ti Riporta a MainMenu>Visualizzazioni
			  }
		break;
		
  		case 3: //Uscita dal Programma
  			System.out.println("Uscita dal Programma in corso...");
  			try {
  				lgn.close();
  			}
  			catch (SQLException e)
  			{
  				System.out.println(e.toString());
  			}
  			System.exit(0);
  		break;
  	
  		default:
  			System.out.println("  !Riprovare!"); //errore nel MainMenu
  			chooseQuery(0, 0, 0, stmt); //ritorna a MainMenu
	  }
}

private static void queriesOnStructures(int SubLevel2, Statement stmt) { 		//(2, 1, 0)
	if(SubLevel2==0) {
		  scan = new Scanner(System.in);
		  System.out.println("\n1. Strutture ricettive di una città");
		  System.out.println("2. Tutte le Strutture Ricettive disponibili in un periodo di tempo");
		  System.out.println("3. Tutte le Strutture Ricettive disponibili in un periodo di tempo e con prezzo a notte inferiore ai 50 euro");
		  System.out.println("4. Ostelli per i quali non vi sono prenotazioni superiori ai 7 giorni");
		  System.out.println("5. Strutture con distanza inferiore ai 10km da uno specifico punto d'interesse");
		  System.out.println("6. Strutture che si trovano in una specifica città ed hanno meno di 3 prenotazioni");
		  System.out.println("7. INDIETRO\n");
		  SubLevel2 = scan.nextInt();
	}
	switch(SubLevel2) {
		case 1:
			queriesOnly.StructureByCity(stmt);					//FATTO
			break;
		case 2:
			queriesOnly.StructureByAvailability(stmt);			//FATTO
			break;
		case 3:
			queriesOnly.StructureByAvailabilityAndPrice(stmt);	//FATTO
			break;
		case 4:
			queriesOnly.OstelliLessSevenDay(stmt);				//FATTO
			break;
		case 5:
			queriesOnly.StructureLess10Km(stmt); 				//FATTO
			break;
		case 6:
			queriesOnly.StructureByCityLess3Booking(stmt); 		//FATTO
			break;
		case 7:
		default:
			chooseQuery(2, 0, 0, stmt); //ritorna a MainMenu>Visualizzazioni
	}
	
}

private static void queriesOnReservations(int SubLevel2, Statement stmt) {	//(2, 3, 0)
	if(SubLevel2==0) {
		  scan = new Scanner(System.in);
		  System.out.println("\n1. Prenotazioni effettuate gli ultimi 30 giorni"); //FATTO
		  System.out.println("2. Somma degli incassi per struttura ricettiva"); 
		  System.out.println("3. Prenotazioni non ancora completate");
		  System.out.println("4. INDIETRO\n");
		  SubLevel2 = scan.nextInt();
	}
	switch(SubLevel2) {
	case 1:
		queriesOnly.ReservationsLastMonth(stmt); 
		break;
	case 2:
		queriesOnly.EarningsForStructure(stmt);
		break;
	case 3:
		queriesOnly.NotCompletedReservations(stmt);
	case 4:
	default:
		chooseQuery(2, 0, 0, stmt); //ritorna a MainMenu>Visualizzazioni
	}
}

static void queriesOnClients(int SubLevel2, Statement stmt) {		//(2, 2, 0)
	if(SubLevel2==0) {
		  scan = new Scanner(System.in);
		  System.out.println("\n1. 10 migliori clienti Premium");
		  System.out.println("2. Possiedono prenotazioni solo con Appartamenti e Ostelli");
		  System.out.println("3. INDIETRO\n");
		  SubLevel2 = scan.nextInt();
	}
	switch(SubLevel2) {
	case 1:
		queriesOnly.bestPremiumClients(stmt); //FATTO
		break;
	case 2:
		queriesOnly.onlyReservationWith(stmt); //FATTO
		break;
	case 3:
	default:
		chooseQuery(2, 0, 0, stmt); //ritorna a MainMenu>Visualizzazioni
	}
}

private static void queriesOnAgencies(int SubLevel2, Statement stmt) {	//(2, 4, 0) 
	if(SubLevel2==0) {
		  scan = new Scanner(System.in);
		  System.out.println("1. Numero Totale di prenotazioni effettuate per agenzia");
		  System.out.println("2. INDIETRO");
		  SubLevel2 = scan.nextInt();
	}
	switch(SubLevel2) {
	case 1:
		queriesOnly.countReservationsByAgency(stmt);
		break;
	case 2:
	default:
		chooseQuery(2, 0, 0, stmt); //ritorna a MainMenu>Visualizzazioni
	}
		
}
}
  								