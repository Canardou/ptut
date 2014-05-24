package robot.taches;

import robot.actionneurs.*;
import robot.capteurs.*;
import robot.environnement.*;
import robot.evenements.*;


/**
 * Classe principale du code embarqué sur le robot. Elle contient tout les
 * éléments qui permettent de représenter le robot.
 * 
 * @author Thomas
 */
public class TachePrincipale extends Thread {
	 	    
    // ------------------------------------- ATTRIBUTS --------------------------------------------

	/**
	 * Attribut représentant les mouvements du robot.
	 */
	private Mouvement mouv;

	/**
	 * Attribut représentant l'environnement du robot.
	 */
	private Environnement env;
	
	/**
	 * Attribut représentant les capteurs.
	 */
	private Capteurs capteurs;
	
	/**
	 * Attribut représentant la gestion des ordres.
	 */
	private Ordre ordre;
	
	// ------------------------------------- CONSTRUCTEUR -----------------------------------------

	/**
	 * Constructeur de Robot
	 */
	public TachePrincipale() {
		this.mouv = new Mouvement(this);
		this.env = new Environnement(this);
		this.capteurs = new Capteurs(this);
		this.ordre = new Ordre(this);
		this.setPriority(5);
	}
	
	// ------------------------------------- TACHE ------------------------------------------------

	/**
	 * Tache principale du robot
	 */
	public void run() {
		while (true) {
			this.ordre.choisirOrdreExploration();
			this.ordre.executerOrdre();
		}
	} 
	
	// ------------------------------------- GETTERS ----------------------------------------------

	/**
	 * @return la valeur de l'attribut mov.
	 */
	public Mouvement getMouv() {
		return this.mouv;
	}

	/**
	 * @return la valeur de l'attribut env.
	 */
	public Environnement getEnv() {
		return this.env;
	}

	/**
	 * @return l'attribut représentant les capteurs.
	 */
	public Capteurs getCapteurs() {
		return this.capteurs;
	}
	
	/**
	 * @return l'objet ordre du robot
	 */
	public Ordre getOrdre() {
		return this.ordre;
	}
}
