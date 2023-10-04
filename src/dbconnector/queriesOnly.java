package dbconnector;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public final class queriesOnly {
	static Scanner scan;
	//registrazioni
	static void newClient(Statement stmt) {
		int riuscito=1;
		scan = new Scanner(System.in);
		System.out.print("Inserisci email: ");
	    String email = scan.nextLine();
	    System.out.print("Inserisci numero di telefono: ");
	    String phoneNumber = scan.nextLine();
	    System.out.print("Inserisci data di nascita (YYYY-MM-DD): ");
	    String dateOfBirth = scan.nextLine();
	    Date date = Date.valueOf(dateOfBirth);
	    System.out.print("Inserisci Codice Fiscale: ");
	    String cf = scan.nextLine();
	    System.out.print("Inserisci Cognome: ");
	    String surname = scan.nextLine();
	    System.out.print("Inserisci Nome: ");
	    String name = scan.nextLine();
	    System.out.print("Inserisci CAP: ");
	    String cap = scan.nextLine();
	    System.out.print("Inserisci Indirizzo: ");
	    String address = scan.nextLine();
		
	    try {
	    	stmt.execute("INSERT INTO Cliente (email, Telefono, Data_di_nascita, CF, Cognome, Nome, CAP, VIA) \n"
	    		+ "VALUES('"+ email +"' ,"+ phoneNumber +",  '"+ date +"', '"+ cf +"', '"+ surname +"', '"+ name +"', "+ cap +", '"+ address +"')");
	    	stmt.execute("INSERT INTO Tessera (ID_Tessera, Cliente, Sconto, Data_Inizio, Data_Fine, Tipologia) VALUES"
	    			+ "(1, (SELECT ID_Cliente FROM Cliente WHERE Nome = '"+name+"' AND Cognome ='"+surname+"' ORDER BY ID_Cliente DESC LIMIT 1), 0, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'Standard');");
	    	 
	    }
	    catch (SQLException e)
	    {
	    	riuscito=0;
	    	System.out.println(e.toString());
	    }
	    if(riuscito==1) {
	    	System.out.println("Inserimento cliente riuscito!");
	    }
	   
	}
	
	static void newStructure(Statement stmt) {
		int riuscito=1;
		int choice=-1;
		String servizi = null;
		int anno=0;
		String nome = null;
		float prezzo=-1;
		float latitudine;
		float longitudine;
		int cap=0;
		String via;
		int vani=-1;
		float mq=-1;
		int camere=-1;
		
		String search = null;
		ResultSet rs = null;
		String number = null;
		int nextNumber=0;
		scan = new Scanner(System.in);
		while(!(choice>0 && choice <4))
		{
			System.out.println("Selezionare il tipo di struttura da inserire:\n1. Hotel\n2. Appartamento\n3. Ostello");
			choice = scan.nextInt();
		}
		scan.nextLine();
		System.out.println("Inserire i servizi");
		servizi = scan.nextLine();
		while(!(anno>=1990 && anno <=2023)) {
		System.out.println("Inserire l'anno di iscrizione");
		anno = scan.nextInt();
		}
		scan.nextLine();
		System.out.println("Inserire il nome");
		nome = scan.nextLine();
		while(prezzo<0) {
		System.out.println("Inserire il prezzo per notte");
		prezzo = scan.nextFloat();
		}
		System.out.println("Inserire la latitudine");
		latitudine = scan.nextFloat();
		System.out.println("Inserire la longitudine");
		longitudine = scan.nextFloat();
		while(!(cap>0 && cap <98101)) {
		System.out.println("Inserire il CAP");
		cap = scan.nextInt();
		}
		scan.nextLine();
		System.out.println("Inserire la Via");	//funziona tutto fino a qui
		via = scan.nextLine();
		if(choice==1)
			search="HOT_%";
		if(choice==2)
			search="APP_%";
		if(choice==3)
			search="OST_%";
		try {
			rs=stmt.executeQuery("SELECT ID_Struttura FROM Struttura WHERE ID_Struttura LIKE '"+search+"'\n"
					+ "ORDER BY ID_Struttura DESC\n"
					+ "LIMIT 1;");
			rs.next();
			search=rs.getString("ID_Struttura");
			System.out.println("L'ultimo ID_Struttura è: "+search);
			number=search.replaceAll("[^0-9]", "");
			nextNumber=Integer.parseInt(number);
			nextNumber++;
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		if(nextNumber<10)
			number="000"+nextNumber;
		if(nextNumber<100 && nextNumber>9)
			number="00"+nextNumber;
		if(nextNumber<1000 && nextNumber>99)
			number="0"+nextNumber;
		System.out.println("Il nuovo Numero è: "+number);
		switch(choice)
		{
		case 1:
			while(!(camere>0 && camere<3)) {
			System.out.println("Vuoi inserire delle camere per l'albergo?\n1. Si\n2. No");
			camere = scan.nextInt();
			try {
				stmt.execute("INSERT INTO Struttura\n"
						+ "VALUES ('HOT_"+number+"','"+servizi+"','"+anno+"','"+nome+"', "+prezzo+", "+latitudine+", "+longitudine+", null, null, "+cap+", '"+via+"');");
				}
				catch(SQLException e)
				{
					riuscito=0;
					System.out.println(e.toString());
				}
			if(camere==1)
				newRoom(stmt, "HOT_"+number);
			}
			break;
		case 2:
			while(vani <0) {
			System.out.println("Inserire Numero Vani");	//funziona
			vani = scan.nextInt();
			}
			while(mq<0) {
			System.out.println("Inserire Metri Quadri");	//funziona
			mq = scan.nextInt();
			}
			try {
			stmt.execute("INSERT INTO Struttura\n"
					+ "VALUES ('APP_"+number+"','"+servizi+"','"+anno+"','"+nome+"', "+prezzo+", "+latitudine+", "+longitudine+","+vani+", "+mq+", "+cap+", '"+via+"');"); 
			}
			catch(SQLException e)
			{
				riuscito=0;
				System.out.println(e.toString());
			}
			break;
		case 3:
			try {
			stmt.execute("INSERT INTO Struttura\n"
					+ "VALUES ('OST_"+number+"','"+servizi+"','"+anno+"','"+nome+"', "+prezzo+", "+latitudine+", "+longitudine+", null, null, "+cap+", '"+via+"');");
			}
			catch(SQLException e)
			{
				riuscito=0;
				System.out.println(e.toString());
			}
			break;
			default:
		}
		if(riuscito==1) {
			System.out.println("Inserimento Struttura riuscito!");
		}
	}

	static void newRoom(Statement stmt, String albergo) {
		int number = 1;
		int addAnotherRoom = 1;
		ResultSet rs = null;
		try {
		rs = stmt.executeQuery("select Numero from Stanza where Stanza.Struttura = '"+albergo+"' ORDER BY Numero DESC LIMIT 1;");
		rs.next();
		number+=rs.getInt("Numero");
		}
		catch (SQLException e)
		{
			System.out.println(e.toString());
		}
		while(addAnotherRoom==1) {
			int posti = 0;
			float prezzo = 0;
			String tipologia = null;
			int disponibile = 0;
			String disp = null;
		System.out.println("Stanza "+number+"\n");
			while(posti<=0) {
			System.out.println("Inserire Numero Posti Letto");
			posti = scan.nextInt();
			}
			while(prezzo<=0) {
			System.out.println("Inserire Prezzo a Notte");
			prezzo = scan.nextFloat();
			}
			scan.nextLine();
			System.out.println("Inserire la Tipologia");
			tipologia = scan.nextLine();
			while(!(disponibile>0 && disponibile<3)) {
				System.out.println("Inserire la Disponibilità\n1. Disponibile\n2. Occupata");
				disponibile = scan.nextInt();
				if(disponibile==1) disp="Libera";
				else disp="Occupata";
			}
			try {
			stmt.execute("INSERT INTO Stanza VALUES( "+number+",'"+albergo+"',"+posti+","+prezzo+",'"+disp+"','"+tipologia+"');");
			}
			catch(SQLException e)
			{
				System.out.println(e.toString());
			}
			disponibile=0;
			while(!(disponibile>0 && disponibile<3))
			{
				System.out.println("Vuoi inserire una nuova camera?\n1. Si\n2. No");
				disponibile = scan.nextInt();
				scan.nextLine();
				if(disponibile==2)
					addAnotherRoom++; //si esce dal ciclo
				else number++;	//inserisco il numero successivo della camera e riparte il ciclo
			}
		}
	}
	
	static void newReservation(Statement stmt) {
	int riuscito=1;
	int i=0;
	int scelta=0;
	int ID_Cliente=0;
	String ID_Struttura=null;
	int ID_Stanza=0;
	String Agenzia=null;
	String Checkin=null;
	String Checkout=null;
	int ospiti=0;
	String note=null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	ResultSet rs4 = null;
	ResultSet rs5 = null;
	ResultSet rs6 = null;
	
	scan = new Scanner(System.in);
	
	
	System.out.println("Come vuoi procedere? (1/2)\n"
			+ "1)Visualizza tutti i clienti\n"
			+ "2)Inserisci nome e cognome del cliente che ha effettuato la prenotazione");
	scelta=scan.nextInt();
	if(scelta!=1 || scelta !=2)
	{
		while(scelta!=1 && scelta !=2) {
			System.out.println("Valore inserito errato!");
			System.out.println("Come vuoi procedere? (1/2)\n"
					+ "1)Visualizza tutti i clienti\n"
					+ "2)Inserisci nome e cognome del cliente che ha effettuato la prenotazione");
			scelta=scan.nextInt();
		}
		
	}
	scan.nextLine();
	
	if(scelta==1) {
		try {
			rs = stmt.executeQuery("SELECT ID_Cliente, Nome, Cognome, Data_di_nascita FROM Cliente;");
			System.out.println("Sono presenti i seguenti Utenti nel database:");
			while(rs.next())
			{
				i++;
				int ID = rs.getInt("ID_Cliente");
				String nome = rs.getString("Nome");
				String cognome = rs.getString("Cognome");
				String data = rs.getString("Data_di_nascita");
				System.out.println(i+") ID: ["+ID+"], Nome: ["+nome+"], Cognome: ["+cognome+"], Data di Nascita: ["+data+"]");
			}
			rs.close();
			i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Cliente=scan.nextInt();
		scan.nextLine();
	}
	else {
		System.out.println("Inserire il nome del Cliente:");
		String nome=scan.nextLine();
		System.out.println("Inserire il cognome del Cliente:");
		String cognome=scan.nextLine();
		try {
			rs=stmt.executeQuery("SELECT ID_Cliente, Nome, Cognome, Data_di_nascita\n"
					+ "FROM Cliente\n"
					+ "WHERE Nome='"+nome+"' AND Cognome = '"+cognome+"'");
			System.out.println("Sono presenti i seguenti Utenti nel database con questo nome e cognome:");
			while(rs.next())
			{
				i++;
				int ID = rs.getInt("ID_Cliente");
				String q_nome = rs.getString("Nome");
				String q_cognome = rs.getString("Cognome");
				String data = rs.getString("Data_di_nascita");
				System.out.println(i+") ID: ["+ID+"], Nome: ["+q_nome+"], Cognome: ["+q_cognome+"], Data di Nascita: ["+data+"]");
			}
			rs.close();
			i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Cliente=scan.nextInt();
		scan.nextLine();
	}
	scelta=0;
	System.out.println("Come vuoi procedere? (1-4)\n"
			+ "1)Visualizza tutti gli Hotel\n"
			+ "2)Visualizza tutti gli Ostelli\n"
			+ "3)Visualizza tutti gli Appartamenti\n"
			+ "4)Inserisci il nome della struttura");
	scelta=scan.nextInt();
	if(scelta!=1 || scelta !=2 || scelta!=3 || scelta!=4)
	{
		while(scelta!=1 && scelta !=2 && scelta!=3 && scelta!=4) {
			System.out.println("Valore inserito errato!");
			System.out.println("Come vuoi procedere? (1-4)\n"
					+ "1)Visualizza tutti gli Hotel\n"
					+ "2)Visualizza tutti gli Ostelli\n"
					+ "3)Visualizza tutti gli Appartamenti\n"
					+ "4)Inserisci il nome della struttura");
			scelta=scan.nextInt();
		}
		
	}
	scan.nextLine();
	
	if(scelta==1) {
		try {
			rs2=stmt.executeQuery("SELECT ID_Struttura, Nome, Via, Cap, Prezzo "
					+ "FROM Struttura "
					+ "WHERE ID_Struttura LIKE 'HOT_%';");
			while(rs2.next())
	        {
	            i++;
	            String id = rs2.getString("ID_Struttura");
	            String nome = rs2.getString("Nome");
	            float prezzo = rs2.getFloat("Prezzo");
	            int cap = rs2.getInt("CAP");
	            String via = rs2.getString("Via");
	            System.out.println(i + ") ID:["+id+"], Nome: ["+ nome + "], Prezzo: [" + prezzo + "], Via: [" + via + "], CAP: [" + cap + "]");
	        }
	    rs2.close();
	    i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Struttura=scan.nextLine();
		
		
		try {
	         rs3=stmt.executeQuery("SELECT Numero, Prezzo, Tipologia, Disponibilità "
	         		+ "FROM Stanza "
	         		+ "WHERE Struttura = UPPER('"+ID_Struttura+"');");
	         while(rs3.next())
	         {
	            i++;
	            int numero = rs3.getInt("Numero");
	            float prezzo = rs3.getFloat("Prezzo");
	            String tipo = rs3.getString("Tipologia");
	            String disp = rs3.getString("Disponibilità");
	            System.out.println(i + ")Numero: ["+ numero + "], Prezzo: [" + prezzo + "], Tipologia: [" + tipo + "], Disponibilità: ["+ disp +"]");
	         }
	         System.out.println("Inserire il Numero della camera presente nella lista:");
	         ID_Stanza = scan.nextInt();
	         scan.nextLine();
		}
	    catch(SQLException e)
	    {
	        System.out.println(e.toString());
	    }
	}
	
	else if(scelta==2) {
		try {
			rs2=stmt.executeQuery("SELECT ID_Struttura, Nome, Via, Cap, Prezzo "
					+ "FROM Struttura "
					+ "WHERE ID_Struttura LIKE 'OST_%';");
			while(rs2.next())
	        {
	            i++;
	            String id = rs2.getString("ID_Struttura");
	            String nome = rs2.getString("Nome");
	            float prezzo = rs2.getFloat("Prezzo");
	            int cap = rs2.getInt("CAP");
	            String via = rs2.getString("Via");
	            System.out.println(i + ") ID:["+id+"], Nome: ["+ nome + "], Prezzo: [" + prezzo + "], Via: [" + via + "], CAP: [" + cap + "]");
	        }
	    rs2.close();
	    i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Struttura=scan.nextLine();
	}
	
	else if(scelta==3) {
		try {
			rs2=stmt.executeQuery("SELECT ID_Struttura, Nome, Via, Cap, Prezzo, Vani, MQ "
					+ "FROM Struttura "
					+ "WHERE ID_Struttura LIKE 'APP_%';");
			while(rs2.next())
	        {
	            i++;
	            String id = rs2.getString("ID_Struttura");
	            String nome = rs2.getString("Nome");
	            String via = rs2.getString("Via");
	            int cap = rs2.getInt("CAP");
	            float prezzo = rs2.getFloat("Prezzo");
	            int vani = rs2.getInt("Vani");
	            float mq = rs2.getFloat("MQ");
	            System.out.println(i + ") ID:["+id+"], Nome: ["+ nome + "], Prezzo: [" + prezzo + "], Vani:["+vani+"], Metri Quadri:["+mq+"], "
	            		+ "Via: [" + via + "], CAP: [" + cap + "]");
	        }
	    rs2.close();
	    i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Struttura=scan.nextLine();
	}
	
	else if(scelta==4) {
		System.out.println("Inserire il nome della struttura:");
		String nomeStr=scan.nextLine();
		
		try {
			rs4=stmt.executeQuery("SELECT ID_Struttura, Nome, Via, Cap, Prezzo "
					+ "FROM Struttura "
					+ "WHERE UPPER(Nome) LIKE UPPER('%"+nomeStr+"%');");
			while(rs4.next())
	        {
	            i++;
	            String id = rs4.getString("ID_Struttura");
	            String nome = rs4.getString("Nome");
	            float prezzo = rs4.getFloat("Prezzo");
	            int cap = rs4.getInt("CAP");
	            String via = rs4.getString("Via");
	            System.out.println(i + ") ID: ["+id+"], Nome: ["+ nome + "], Prezzo: [" + prezzo + "], Via: [" + via + "], CAP: [" + cap + "]");
	        }
	    rs4.close();
	    i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		ID_Struttura=scan.nextLine();
		if(ID_Struttura.contains("HOT") || ID_Struttura.contains("hot")){
			try {
		         rs5=stmt.executeQuery("SELECT Numero, Prezzo, Tipologia, Disponibilità "
			         		+ "FROM Stanza "
			         		+ "WHERE Struttura = UPPER('"+ID_Struttura+"');");
		         while(rs5.next())
		         {
		            i++;
		            int numero = rs5.getInt("Numero");
		            float prezzo = rs5.getFloat("Prezzo");
		            String tipo = rs5.getString("Tipologia");
		            String disp = rs5.getString("Disponibilità");
		            System.out.println(i + ")Numero: ["+ numero + "], Prezzo: [" + prezzo + "], Tipologia: [" + tipo + "], Disponibilità: ["+disp+"]");
		         }
		         System.out.println("Inserire il Numero della camera presente nella lista:");
		         ID_Stanza = scan.nextInt();
		         scan.nextLine();
			}
		    catch(SQLException e)
		    {
		        System.out.println(e.toString());
		    }

		}
	}
	int sentinella=0;
	int k=0;
	while(sentinella==0)
	try {
		rs6=stmt.executeQuery("Select Nome From Agenzia");
		while(rs6.next()){
			k++;
			String nome=rs6.getString("Nome");
			System.out.println(k + ") Nome: ["+ nome + "]");
		}
		rs6.close();
		k=0;
		
		System.out.println("Inserire il nome dell'Agenzia:");
		Agenzia=scan.nextLine();
		sentinella=1;
		rs5=stmt.executeQuery("SELECT COUNT(Nome) as n "
				+ "FROM Agenzia "
				+ "Where UPPER(Nome) LIKE UPPER('"+Agenzia+"');");
		rs5.next();
		if(rs5.getInt("n")!=1) {
			System.out.println("Agenzia non presente! Reinserire il nome dell'Agenzia:");
			Agenzia=scan.nextLine();
			sentinella=0;
		}
		else {
			rs5=stmt.executeQuery("SELECT Nome "
					+ "FROM Agenzia "
					+ "Where UPPER(Nome) LIKE UPPER('"+Agenzia+"');");
			rs5.next();
			Agenzia=rs5.getString("Nome");
		}
		rs5.close();
	}
	catch(SQLException e)
    {
        System.out.println(e.toString());
    }
	
	
	System.out.println("Inserire le note (se presenti):");
	note=scan.nextLine();
	System.out.println("Inserire la data di check-in (YYYY-MM-DD):");
	Checkin=scan.nextLine();
	System.out.println("Inserire la data di check-out (YYYY-MM-DD):");
	Checkout=scan.nextLine();
	
	sentinella=0;
	while(sentinella==0) {
		sentinella=1;
		System.out.println("Inserire il numero di ospiti:");
		ospiti=scan.nextInt();
		if(ospiti<1) {
			System.out.println("Hai inserito un valore negativo! Reinserire il numero di ospiti:");
			ospiti=scan.nextInt();
			sentinella=0;
		}
		
	}
	scan.nextLine();
	
	Date cin = Date.valueOf(Checkin);
	Date cout = Date.valueOf(Checkout);
	
	if(!cin.before(cout)) {
		System.out.println("Il check-in è dopo il check-out!");
		System.out.println("Reinserire la data di check-in (YYYY-MM-DD):");
		Checkin=scan.nextLine();
		System.out.println("Reinserire la data di check-out (YYYY-MM-DD):");
		Checkout=scan.nextLine();
		cin = Date.valueOf(Checkin);
		cout = Date.valueOf(Checkout);
	}
	ID_Struttura=ID_Struttura.toUpperCase();
	
	if(!ID_Struttura.contains("HOT")) {
		try {
			stmt.execute("INSERT INTO Prenotazione (ID_Prenotazione, ID_Cliente, ID_Struttura, Agenzia, Check_in, Check_Out, Data_Inserimento, Stato, Costo, Ospiti, Note)"
					+ "SELECT"
					+ "  NULL,"
					+ "  "+ID_Cliente+","
					+ "  '"+ID_Struttura+"',"
					+ "  '"+Agenzia+"',"
					+ "  '"+cin+"',"
					+ "  '"+cout+"',"
					+ "  CURDATE(),"
					+ "  'Inviata',"
					+ "  (DATEDIFF('"+cout+"', '"+cin+"') * (s.prezzo) * (100 - t.sconto) / 100),"
					+ "  "+ospiti+","
					+ "  '"+note+"'"
					+ "FROM Struttura s"
					+ "JOIN Tessera t ON (t.Cliente = "+ID_Cliente+" AND t.Tipologia != \"Scaduta\")"
					+ "WHERE s.ID_Struttura = UPPER('"+ID_Struttura+"')	;");
		}
		catch(SQLException e)
	    {
			riuscito=0;
	        System.out.println(e.toString());
	    }
	}
	
	else {
		
		try{
			stmt.execute("INSERT INTO Prenotazione (ID_Prenotazione, ID_Cliente, ID_Struttura, ID_Stanza, Agenzia, Check_in, Check_Out, Data_Inserimento, Stato, Costo, Ospiti, Note)"
					+ "Values "
					+ "(NULL,"
					+ " "+ID_Cliente+","
					+ " '"+ID_Struttura+"',"
					+ " "+ID_Stanza+","
					+ " '"+Agenzia+"',"
					+ " '"+cin+"',"
					+ "'"+cout+"',"
					+ " CURDATE(),"
					+ " 'Inviata',"
					+ " ((DATEDIFF('"+cout+"','"+cin+"'))*"
							+ "((SELECT Prezzo from Stanza where Numero="+ID_Stanza+" AND ID_Struttura='"+ID_Struttura+"')"
									+ "+(Select Prezzo from Struttura where ID_Struttura='"+ID_Struttura+"'))"
											+ "*(100-(select Sconto from Tessera where Cliente="+ID_Cliente+" AND Tipologia!='Scaduta')))/100, "+ospiti+",'"+note+"');");
		}
		catch(SQLException e)
	    {
			riuscito=0;
	        System.out.println(e.toString());
	    }
	}	
	if(riuscito==1) {
		System.out.println("Inserimento Prenotazione riuscito!");
	}
}

	static void newPremium(Statement stmt) {
		int riuscito=1;
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		try {
			rs = stmt.executeQuery("SELECT ID_Cliente, Nome, Cognome, Data_di_nascita FROM Cliente;");
			System.out.println("Sono presenti i seguenti Utenti nel database:");
			while(rs.next())
			{
				i++;
				int ID = rs.getInt("ID_Cliente");
				String nome = rs.getString("Nome");
				String cognome = rs.getString("Cognome");
				String data = rs.getString("Data_di_nascita");
				System.out.println(i+") ID: ["+ID+"], Nome: ["+nome+"], Cognome: ["+cognome+"], Data di Nascita: ["+data+"]");
			}
			rs.close();
			i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println("Inserire l'ID desiderato presente nella lista:");
		scan = new Scanner(System.in);
		int id = scan.nextInt();
		System.out.println("Inserire la percentuale di sconto da attribuire:");
		int sconto = scan.nextInt();
		System.out.println("Inserire la data di scadenza della tessera premium nel formato (YYYY-MM-DD):");
		scan.nextLine();
		String scadenza = scan.nextLine();
		Date date = Date.valueOf(scadenza);
		int id_tessera=0;
		try {
			rs2=stmt.executeQuery("SELECT MAX(ID_Tessera) as ID_Tessera FROM Tessera WHERE Cliente ="+ id +";");
			rs2.next();
			id_tessera=1+rs2.getInt("ID_Tessera");
			stmt.execute("INSERT INTO Tessera (ID_Tessera, Cliente, Sconto, Data_Inizio, Data_Fine, Tipologia)\n"
					+ "VALUES ("+id_tessera+","+ id + "," + sconto + ",CURDATE(),'"+date+"','Premium');");
			id_tessera--;
			stmt.execute("UPDATE Tessera SET Tipologia ='Scaduta' WHERE ID_Tessera="+id_tessera+" AND Cliente ="+id+";");
		}
		catch(SQLException e)
		{
			riuscito=0;
			System.out.println(e.toString());
		}
		if(riuscito==1) {
			System.out.println("Inserimento Premium riuscito!");
		}
	}

	//queriesOnStructures
	static void StructureByCity(Statement stmt) {
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		try {
			rs = stmt.executeQuery("SELECT CAP FROM Struttura;");
			System.out.println("Sono presenti i seguenti CAP nel databse:");
			while(rs.next())
			{
				i++;
				int cap = rs.getInt("CAP");
				System.out.println(i + ") "+ cap);
			}
			rs.close();
			i=0;
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println("Inserire il CAP desiderato presente nella lista:");
		scan = new Scanner(System.in);
		int citta = scan.nextInt();
		try {
			rs2 = stmt.executeQuery("SELECT ID_Struttura, Nome, Via, CAP\n"
				+ "FROM Struttura\n"
				+ "WHERE CAP = "+citta+";");
		
			while(rs2.next())
			{
				i++;
				String struttura = rs2.getString("ID_Struttura");
				String nome = rs2.getString("Nome");
				String via = rs2.getString("Via");
				int cap = rs2.getInt("CAP");
				System.out.println(i + ")ID: ["+ struttura + "], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "]");
			}
			rs2.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
	}

	static void StructureByAvailability(Statement stmt) {
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		System.out.println("Inserire le date nel formato YYYY-MM-DD");
		scan = new Scanner(System.in);
		System.out.println("Inserire Check-In");
		String checkin = scan.nextLine();
		Date IN = Date.valueOf(checkin);
		System.out.println("Inserire Check-Out");
		String checkout = scan.nextLine();
		Date OUT = Date.valueOf(checkout);
		try {
		rs = stmt.executeQuery("SELECT s.ID_Struttura, s.Nome, s.Via, s.CAP, s.Prezzo\n"	//TROVA SOLO APPARTAMENTI ED OSTELLI
				+ "FROM Struttura s\n"
				+ "WHERE s.ID_Struttura NOT LIKE \"HOT_%\" AND NOT EXISTS (\n"
				+ "  SELECT *\n"
				+ "  FROM Prenotazione p\n"
				+ "  WHERE p.ID_Struttura = s.ID_Struttura AND p.Stato = \"Confermata\" AND (\n"
				+ "    (p.Check_in <='"+IN+"' AND p.Check_out >='"+IN+"') OR\n"
				+ "		(p.Check_in >='"+IN+"' AND p.Check_out <='"+OUT+"') OR\n"
				+ "		(p.Check_in >='"+OUT+"' AND p.Check_out >='"+OUT+"') OR\n"
				+ "		(p.Check_in <='"+IN+"' AND p.Check_out >='"+OUT+"'))\n"
				+ ");");
				
			while(rs.next())
			{
				i++;
				String struttura = rs.getString("ID_Struttura");
				String nome = rs.getString("Nome");
				String via = rs.getString("Via");
				int cap = rs.getInt("CAP");
				float prezzo = rs.getFloat("Prezzo");
				System.out.println(i + ") ID: ["+ struttura + "], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "], Prezzo: [" + prezzo+ "]");
			}
			rs.close();
			
		rs2 = stmt.executeQuery("SELECT DISTINCT s.Struttura, h.Nome, h.Via, h.CAP, COUNT(s.Numero) AS Numero_Stanze_Libere\n"
				+ "FROM Struttura h\n"
				+ "JOIN Stanza s ON s.Struttura = h.ID_Struttura\n"
				+ "WHERE s.Disponibilità = 'Libera'\n"
				+ "  AND NOT EXISTS (\n"
				+ "    SELECT * FROM Prenotazione p \n"
				+ "    WHERE p.ID_Struttura = s.Struttura AND p.ID_Stanza = s.Numero AND p.stato=\"Confermata\"\n"
				+ "      AND (p.Check_in <= '"+IN+"' AND p.Check_out >= '"+IN+"') OR\n"
				+ "		(p.Check_in >= '"+IN+"' AND p.Check_out <= '"+OUT+"') OR\n"
				+ "		(p.Check_in >= '"+OUT+"' AND p.Check_out >= '"+OUT+"') OR\n"
				+ "		(p.Check_in <= '"+IN+"' AND p.Check_out >= '"+OUT+"'))\n"
				+ " GROUP BY s.Struttura;");
			while(rs2.next())
			{
				i++;
				String struttura = rs2.getString("Struttura");
				String nome = rs2.getString("Nome");
				String via = rs2.getString("Via");
				String cap = rs2.getString("CAP");
				int stanze = rs2.getInt("Numero_Stanze_Libere");
				System.out.println(i + ") ID: ["+ struttura +"], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "], Stanze Libere: [" + stanze+"]");
			}
			rs2.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
	}

	static void StructureByAvailabilityAndPrice(Statement stmt) {
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		System.out.println("Inserire le date nel formato YYYY-MM-DD");
		scan = new Scanner(System.in);
		System.out.println("Inserire Check-In");
		String checkin = scan.nextLine();
		Date IN = Date.valueOf(checkin);
		System.out.println("Inserire Check-Out");
		String checkout = scan.nextLine();
		Date OUT = Date.valueOf(checkout);
		try {
		rs = stmt.executeQuery("SELECT s.ID_Struttura, s.Nome, s.Via, s.CAP, s.Prezzo\n"	//TROVA SOLO APPARTAMENTI ED OSTELLI
				+ "FROM Struttura s\n"
				+ "WHERE s.Prezzo <= 50 AND s.ID_Struttura NOT LIKE \"HOT_%\" AND NOT EXISTS (\n"
				+ "  SELECT *\n"
				+ "  FROM Prenotazione p\n"
				+ "  WHERE p.ID_Struttura = s.ID_Struttura AND p.Stato = \"Confermata\" AND (\n"
				+ "    (p.Check_in <='"+IN+"' AND p.Check_out >='"+IN+"') OR\n"
				+ "		(p.Check_in >='"+IN+"' AND p.Check_out <='"+OUT+"') OR\n"
				+ "		(p.Check_in >='"+OUT+"' AND p.Check_out >='"+OUT+"') OR\n"
				+ "		(p.Check_in <='"+IN+"' AND p.Check_out >='"+OUT+"'))\n"
				+ ");");
				
			while(rs.next())
			{
				i++;
				String struttura = rs.getString("ID_Struttura");
				String nome = rs.getString("Nome");
				String via = rs.getString("Via");
				int cap = rs.getInt("CAP");
				float prezzo = rs.getFloat("Prezzo");
				System.out.println(i + ") ID: ["+ struttura + "], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "], Prezzo: [" + prezzo+ "]");
			}
			rs.close();
			
		rs2 = stmt.executeQuery("SELECT DISTINCT s.Struttura, h.Nome, h.Via, h.CAP, COUNT(s.Numero) AS Numero_Stanze_Libere\n"
				+ "FROM Struttura h\n"
				+ "JOIN Stanza s ON s.Struttura = h.ID_Struttura\n"
				+ "WHERE s.Disponibilità = 'Libera' AND (s.prezzo+h.prezzo)<=50\n"
				+ "  AND NOT EXISTS (\n"
				+ "    SELECT * FROM Prenotazione p \n"
				+ "    WHERE p.ID_Struttura = s.Struttura AND p.ID_Stanza = s.Numero AND p.stato=\"Confermata\"\n"
				+ "      AND (p.Check_in <= '"+IN+"' AND p.Check_out >= '"+IN+"') OR\n"
				+ "		(p.Check_in >= '"+IN+"' AND p.Check_out <= '"+OUT+"') OR\n"
				+ "		(p.Check_in >= '"+OUT+"' AND p.Check_out >= '"+OUT+"') OR\n"
				+ "		(p.Check_in <= '"+IN+"' AND p.Check_out >= '"+OUT+"'))\n"
				+ " GROUP BY s.Struttura;");
			while(rs2.next())
			{
				i++;
				String struttura = rs2.getString("Struttura");
				String nome = rs2.getString("Nome");
				String via = rs2.getString("Via");
				int cap = rs2.getInt("CAP");
				int stanze = rs2.getInt("Numero_Stanze_Libere");
				System.out.println(i + ") ID: ["+ struttura +"], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "], Stanze Libere: [" + stanze+"]");
			}
			rs2.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	static void OstelliLessSevenDay(Statement stmt) {
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT s.ID_Struttura, s.Nome, s.Via, s.CAP\n"
				+ "FROM Struttura s\n"
				+ "WHERE s.ID_Struttura LIKE 'OST_%' AND NOT EXISTS (\n"
				+ "  SELECT *\n"
				+ "  FROM Prenotazione p\n"
				+ "  WHERE p.ID_Struttura = s.ID_Struttura AND DATEDIFF(p.Check_Out, p.Check_In) > 7\n"
				+ ");");
			while(rs.next())
			{
				i++;
				String struttura = rs.getString("ID_Struttura");
				String nome = rs.getString("Nome");
				String via = rs.getString("Via");
				int cap = rs.getInt("CAP");
				System.out.println(i + ") ID: ["+ struttura + "], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap+ "]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	static void StructureLess10Km(Statement stmt) {
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		try {
			rs=stmt.executeQuery("SELECT Nome FROM Punto_Interesse;");
			System.out.println("Sono le seguenti Attrazioni nel databse:");
			while(rs.next())
			{
				i++;
				String attrazione = rs.getString("Nome");
				System.out.println(i + ") "+ attrazione);
			}
			i=0;
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println("Inserire Nome dell'Attrazione");
		scan = new Scanner(System.in);
		String attrazione = scan.nextLine();
		try {
		rs2 = stmt.executeQuery("SELECT s.ID_Struttura, d.Distanza, s.Nome, s.Via, s.CAP \n"
				+ "FROM Struttura s\n"
				+ "JOIN Dista d ON s.ID_Struttura = d.Struttura\n"
				+ "WHERE d.Distanza<10 AND d.Attrazione ='"+ attrazione + "';");
				
			while(rs2.next())
			{
				i++;
				String struttura = rs2.getString("ID_Struttura");
				float distanza = rs2.getFloat("Distanza");
				String nome = rs2.getString("Nome");
				String via = rs2.getString("Via");
				int cap = rs2.getInt("CAP");
				System.out.println(i + ") ID: ["+ struttura + "], Distanza (KM): [" + distanza + "], Nome struttura:[" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap+"]");
			}
			rs2.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}
	
	static void StructureByCityLess3Booking(Statement stmt) {
		ResultSet rs = null;
		ResultSet rs2 = null;
		int i=0;
		
		try {
			rs=stmt.executeQuery("SELECT CAP FROM Struttura;");
			System.out.println("Sono presenti i seguenti CAP nel databse:");
			while(rs.next())
			{
				i++;
				int cap = rs.getInt("CAP");
				System.out.println(i + ") "+ cap);
			}
			i=0;
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println("Inserire il CAP desiderato presente nella lista:");
		scan = new Scanner(System.in);
		int citta = scan.nextInt();
		try {
		rs2 = stmt.executeQuery("SELECT s.ID_Struttura, s.Nome, s.Via, s.CAP, COUNT(p.ID_Prenotazione) AS Numero_Prenotazioni\n"
				+ "FROM Struttura s\n"
				+ "LEFT JOIN Prenotazione p ON s.ID_Struttura = p.ID_Struttura\n"
				+ "WHERE s.CAP ="+ citta + "\n"
				+ "GROUP BY s.ID_Struttura, s.Nome, s.Via, s.CAP\n"
				+ "HAVING COUNT(p.ID_Prenotazione) < 3;");
			
			while(rs2.next())
			{
				i++;
				String struttura = rs2.getString("ID_Struttura");
				String nome = rs2.getString("Nome");
				String via = rs2.getString("Via");
				int cap = rs2.getInt("CAP");
				int n_pren = rs2.getInt("Numero_Prenotazioni");
				System.out.println(i + ")ID: ["+ struttura + "], Nome: [" + nome + "], Indirizzo: [" + via + "], CAP: [" + cap + "], N.Prenotazioni: [" + n_pren+"]");
			}
			rs2.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	//queriesOnReservation
	static void ReservationsLastMonth(Statement stmt) {
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT * FROM Prenotazione p\n"
				+ "WHERE Data_Inserimento>=DATE_SUB(CURDATE(), interval 30 day)\n"
				+ "ORDER BY p.Data_Inserimento ASC;");
			while(rs.next())
			{
				i++;
				int prenotazione = rs.getInt("ID_Prenotazione");
				int cliente = rs.getInt("ID_Cliente");
				String struttura = rs.getString("ID_Struttura");
				int stanza = rs.getInt("ID_Stanza");
				String agenzia = rs.getString("Agenzia");
				String C_IN = rs.getString("Check_In");
				String C_OUT = rs.getString("Check_Out");
				String D_ins = rs.getString("Data_Inserimento");
				String stato = rs.getString("Stato");
				float costo = rs.getFloat("Costo");
				int n_osp = rs.getInt("Ospiti");
				String note = rs.getString("Note");
				System.out.println(i + ") ID Prenotazione: ["+ prenotazione + "], ID Cliente[" + cliente + "], ID Struttura: [" + struttura + "], "
						+"ID Stanza: [" + stanza + "], Agenzia: [" + agenzia + "], Check-in: [" + C_IN  + "], Check-out: [" + C_OUT + "], "
						+"Data-Inserimento: [" + D_ins + "], Stato: [" + stato + "], Costo: [" + costo + "], N.Ospiti: [" + n_osp + "], Note: [" + note +"]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	static void EarningsForStructure(Statement stmt) {
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT s.ID_Struttura, s.Nome, SUM(p.Costo) AS Incasso_Totale\n"
				+ "FROM Struttura s\n"
				+ "JOIN Prenotazione p ON s.ID_Struttura = p.ID_Struttura\n"
				+ "GROUP BY s.ID_Struttura, s.Nome\n"
				+ "ORDER BY Incasso_Totale DESC;");
			while(rs.next())
			{
				i++;
				String struttura = rs.getString("ID_Struttura");
				String nome = rs.getString("Nome");
				float totale = rs.getFloat("Incasso_Totale");
				System.out.println(i + ") ID: ["+ struttura + "], Nome: [" + nome + "], Incasso Totale: [" + totale+"]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	static void NotCompletedReservations(Statement stmt) { //Escluse soddisfatta eliminata e rifiutata
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT * \n"
				+ "FROM Prenotazione p\n"
				+ "WHERE p.Stato IN ('Inviata', 'Confermata')\n"
				+ "ORDER BY p.Check_In ASC;");
			while(rs.next())
			{
				i++;
				int prenotazione = rs.getInt("ID_Prenotazione");
				int cliente = rs.getInt("ID_Cliente");
				String struttura = rs.getString("ID_Struttura");
				int stanza = rs.getInt("ID_Stanza");
				String agenzia = rs.getString("Agenzia");
				String C_IN = rs.getString("Check_In");
				String C_OUT = rs.getString("Check_Out");
				String D_ins = rs.getString("Data_Inserimento");
				String stato = rs.getString("Stato");
				float costo = rs.getFloat("Costo");
				int n_osp = rs.getInt("Ospiti");
				String note = rs.getString("Note");
				System.out.println(i + ") ID Prenotazione: ["+ prenotazione + "], ID Cliente[" + cliente + "], ID Struttura: [" + struttura + "], "
						+"ID Stanza: [" + stanza + "], Agenzia: [" + agenzia + "], Check-in: [" + C_IN  + "], Check-out: [" + C_OUT + "], "
						+"Data-Inserimento: [" + D_ins + "], Stato: [" + stato + "], Costo: [" + costo + "], N.Ospiti: [" + n_osp + "], Note: [" + note +"]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	//queriesOnClients
	static void bestPremiumClients(Statement stmt) {
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT c.ID_Cliente, c.Nome, c.Cognome, COUNT(p.ID_Prenotazione) AS Numero_Prenotazioni"
				+ "FROM Cliente c JOIN Tessera t ON c.ID_Cliente = t.Cliente JOIN Prenotazione p ON c.ID_Cliente = p.ID_Cliente"
				+ "WHERE t.Tipologia = 'Premium'"
				+ "GROUP BY c.ID_Cliente"
				+ "ORDER BY Numero_Prenotazioni DESC LIMIT 10;");
			while(rs.next())
			{
				i++;
				int ID = rs.getInt("ID_Cliente");
				String name = rs.getString("Nome");
				String surname = rs.getString("Cognome");
				int np = rs.getInt("Numero_Prenotazioni");
				System.out.println(i + ") ID: ["+ ID + "], Nome: [" + name + "], Cognome: [" + surname +"], N.Prenotazioni: [" +np+"]");
			}
			rs.close();
	}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	static void onlyReservationWith(Statement stmt) {
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT c.ID_Cliente, c.Nome, c.Cognome\n"
				+ "FROM Cliente c\n"
				+ "WHERE NOT EXISTS (\n"
				+ "  SELECT *\n"
				+ "  FROM Prenotazione p\n"
				+ "  WHERE p.ID_Cliente = c.ID_Cliente AND p.ID_Struttura LIKE 'HOT_%' \n"
				+ ")\n"
				+ "AND EXISTS ( SELECT * FROM Prenotazione p\n"
				+ "  WHERE p.ID_Cliente = c.ID_Cliente \n"
				+ ");");
			while(rs.next())
			{
				i++;
				int ID = rs.getInt("ID_Cliente");
				String name = rs.getString("Nome");
				String surname = rs.getString("Cognome");
				System.out.println(i + ") ID: ["+ ID + "], Nome: [" + name + "], Cognome: [" + surname+"]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	//queriesOnAgencies
	static void countReservationsByAgency(Statement stmt) { //FATTO
		ResultSet rs = null;
		int i=0;
		try {
		rs = stmt.executeQuery("SELECT a.Nome AS Agenzia, COUNT(p.ID_Prenotazione) AS Numero_Prenotazioni\n"
				+ "FROM Agenzia a\n"
				+ "JOIN Prenotazione p ON p.Agenzia = a.Nome \n"
				+ "GROUP BY a.Nome\n"
				+ "ORDER BY Numero_Prenotazioni DESC;");
			while(rs.next())
			{
				i++;
				String name = rs.getString("Agenzia");
				int np = rs.getInt("Numero_Prenotazioni");
				System.out.println(i + ") Agenzia: ["+ name + "], Prenotazioni: [" + np+"]");
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

}