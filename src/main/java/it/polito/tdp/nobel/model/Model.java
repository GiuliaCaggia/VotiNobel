package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private double bestMedia = 0.0;
	private Set<Esame> bestSoluzione = null;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<>();
		cerca2(parziale, 0, numeroCrediti);
		
		return bestSoluzione;
	}

	//APPROCCIO 1
	/* La complessità è 2^N */
	private void cerca1(Set<Esame> parziale, int L, int m) {
		
		//casi terminali
		
		int crediti = sommaCrediti(parziale);
		
		if(crediti>m)   //1 caso terminale, il più semplice, mi fermo subito se condizione non rispettata
			return;
		
		if(crediti == m){   // potrebbe essere una soluzione interessante
		    double media = calcolaMedia(parziale);
		    if(media > bestMedia) {
		    	bestSoluzione = new HashSet<>(parziale);
		    	bestMedia = media;    //tengo traccia della migliore soluzione che ho incontrato
		    	//alla fine della ricorsione questi due valori conterranno la soluzione ottima da restituire al controller
		    }
		}
		//sicuramente, crediti < m
		if(L == esami.size())
			return;
		
		//generale sottoproblemi
		//esami[L] è da aggiungere o no? provo entrambe le cose
		
		//provo ad aggiungerlo 
		parziale.add(esami.get(L));
		cerca1(parziale, L+1, m);
		parziale.remove(esami.get(L));
		
		//provo anche a non aggiungerlo
		cerca1(parziale, L+1, m);
		
		
	}
	//APPROCCIO 2
	/* la complessità è N! */
	private void cerca2(Set<Esame> parziale, int L, int m) {
		
		//casi terminali
        int crediti = sommaCrediti(parziale);
		
		if(crediti>m)   //1 caso terminale, il più semplice, mi fermo subito se condizione non rispettata
			return;
		
		if(crediti == m){   // potrebbe essere una soluzione interessante
		    double media = calcolaMedia(parziale);
		    if(media > bestMedia) {
		    	bestSoluzione = new HashSet<>(parziale);
		    	bestMedia = media;    //tengo traccia della migliore soluzione che ho incontrato
		    	//alla fine della ricorsione questi due valori conterranno la soluzione ottima da restituire al controller
		    }
		}
		//sicuramente, crediti < m
		if(L == esami.size())
			return;
		
		//generazione sottoproblemi
		
		for(Esame e: esami) {
			if(!parziale.contains(e)) {
			parziale.add(e);
			cerca2(parziale, L +1, m);
			parziale.remove(e);
			}
		}
	}
	

	public double calcolaMedia(Set<Esame> parziale) {
		int somma = 0;
		int crediti = 0;
		for(Esame e: parziale) {
			crediti += e.getCrediti(); 
			somma += e.getVoto()*e.getCrediti();
		}
		
		return somma/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e: parziale) 
			somma += e.getCrediti();
		
		return somma;
	}
}
