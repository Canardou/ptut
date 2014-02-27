package commun;


public class Message
{
	/**
	 * Creer, compresse, decompresse les messages envoy�s entre robot et ordinateur
	 */
	private char sender;
	private char receiver;
	private char typeMessage;
	private char ordre;
	private int data;
	
	public Message (long msg)
	{
		char [] msgchar= new char[8];
		
		msgchar=Message.LongToChar(msg);
		
		this.sender=msgchar[0];
		this.receiver=msgchar[1];
		this.typeMessage=msgchar[2];
		this.ordre=msgchar[3];
		
		char [] temp= new char[4];
		temp[0]=msgchar[4];
		temp[1]=msgchar[5];
		temp[2]=msgchar[6];
		temp[3]=msgchar[7];
		this.data=Message.charToInt(temp);
		
	}
	
	public Message(char sender, char receiver ,OrdreDeplacement ordre, int params)
	{
		char [] result= new char[4];
		this.sender=sender;
		this.receiver=receiver;
		this.typeMessage=MessageType.MESSAGE_DEPLACEMENT.getValue();
		this.ordre=ordre.getValue();
		this.data=params;
	}
	
	public Message(char sender, char receiver ,OrdreData ordre,int params)
	{
		char [] result= new char[4];
		this.sender=sender;
		this.receiver=receiver;
		this.typeMessage=MessageType.MESSAGE_DATA.getValue();
		this.ordre=ordre.getValue();
		this.data= params;

	}
	
	public Message(char sender, char receiver ,OrdreRole ordre)
	{
		this.sender=sender;
		this.receiver=receiver;
		this.typeMessage=MessageType.MESSAGE_ROLE.getValue();
		this.ordre=ordre.getValue();
		this.data=0;
	}
	
	public static char[] intToChar(int nombre)
	{
		char[] result = new char[4];
		result[0] = (char)(nombre & 255);
		result[1] = (char)((nombre >> 8) & 255);
		result[2] = (char)((nombre >> 16) & 255);
		result[3] = (char)((nombre >> 24) & 255);
		
		return result;
	}
	
	public static int charToInt(char[] tableau)
	{
		return tableau[0] + ((int)(tableau[1]<<8)) + ((int)(tableau[2])<<16) + ((int)(tableau[3])<<24);
	}
	
	/** 
	 * Compresse le message en type long (8 octets)
	 * @return
	 */
	public long ToLong()
	{
		char [] tab = new char[8];
		char [] temp= new char [4];
		tab[0]=this.sender;
		tab[1]=this.receiver;
		tab[2]=this.typeMessage;
		tab[3]=this.ordre;
		
		temp=Message.intToChar(this.data);
		tab[4]=temp[0];
		tab[5]=temp[1];
		tab[6]=temp[2];
		tab[7]=temp[3];
		
		
		return Message.charToLong(tab);
		
	}
	
	/**
	 * Convertit un tableau de char en long(8 octets)
	 * @param tableau
	 * @return
	 */
	public static long charToLong(char[] tableau)
	{
		long result=0;
		for(int i =0; i<8; i++)
		{
			result+=((long)(tableau[i])<<(8*i));
		}
		return result;
	}
	
	/**
	 * Decompresse un long en un tableau de char
	 * @param nombre
	 * @return
	 */
	public static char[] LongToChar(long nombre)
	{
		char[] result = new char[8];
		for (int i=0; i<8;i++)
		{
		result[i] = (char)((nombre >> (8*i)) & 255);
		}
		return result;
	}
	
	/**
	 * Permet un affichage du message brut
	 */
	public String toString()
	{
		String result = "";
		result += this.sender;
		result += this.receiver;
		result += this.typeMessage;
		result += this.ordre;
		result += this.data;
		
		return result;
	}
	
	
	public char getSender ()
	{
		return this.sender;
	}
	
	public char getReceiver()
	{
		return this.receiver;
	}
	
	public char getTypeMessage()
	{
		return this.typeMessage;
	}
	
	public char getOrdre()
	{
		return this.ordre;
	}
	
	public int getData()
	{
		return this.data;
	}
	
	/**
	 * Permet d'afficher de maniere intelligible un message
	 */
	public void afficher()
	{
		String result="";
		
		result=result+"sender: \t "+this.sender+"\n";
		result=result+"receiver: \t "+this.receiver+"\n";
		result=result+"Type d'ordre: \t";
		
		if (this.typeMessage==MessageType.MESSAGE_DATA.getValue())
		{
			result+="DATA \n";
			result+="Ordre: \t";
			if (this.ordre==OrdreData.DATA_ANGLE.getValue())
			{
				result+=" envoi angle \n";
				result=result+"Angle: \t"+this.getData()+"\n";
			}
			else if (this.ordre==OrdreData.DATA_CIBLE_IS_NOT_VISIBLE.getValue())
			{
				result+="La cible n'est pas visible\n";
			}
			else if(this.ordre==OrdreData.DATA_X.getValue())
			{
				result+=" envoi des coordonn�es\n";
				result=result+"X: \t"+this.getData()+"\n";
			}
			else if(this.ordre==OrdreData.DATA_Y.getValue())
			{
				result+=" envoi des coordonn�es\n";
				result=result+"Y: \t"+this.getData()+"\n";
			}
			else if(this.ordre==OrdreData.DATA_DISTANCE.getValue())
			{
				result+=" envoi de la distance\n";
				result=result+"Distance: \t"+this.getData()+"\n";
			}
			else
				result+="probleme ordre\n";
			
		}
		else if(this.typeMessage==MessageType.MESSAGE_DEPLACEMENT.getValue())
		{
			result+="DEPLACEMENT \n";
			result+="Ordre: \t";
			if (this.ordre==OrdreDeplacement.DEP_GO_BACKWARD.getValue())
			{
				result+="recule \n";
			}
			else if (this.ordre==OrdreDeplacement.DEP_GO_FORWARD.getValue())
			{
				result+="avance \n";
			}
			else if(this.ordre==OrdreDeplacement.DEP_SET_TRAVEL_SPEED.getValue())
			{
				result+=" change la vitesse\n";
				result=result+"Vitesse: \t"+this.getData()+"\n";
			}
			else if(this.ordre==OrdreDeplacement.DEP_STOP.getValue())
			{
				result+=" arrete\n";
			}
			else if (this.ordre==OrdreDeplacement.DEP_TURN_ANGLE.getValue())
			{
				result+=" tourne de angle \n";
				result=result+"Angle: \t"+this.getData()+"\n";
			}
			else if (this.ordre==OrdreDeplacement.DEP_TURN_LEFT.getValue())
			{
				result+=" tourne a gauche \n";
			}
			else if(this.ordre==OrdreDeplacement.DEP_TURN_RIGHT.getValue())
			{
				result+=" tourne a droite\n";
			}
			else if(this.ordre==OrdreDeplacement.DEP_ARC_BACKWARD.getValue())
			{
				result+=" tourne fait un arc avant de rayon "+this.getData()+" \n";
				
			}
			else if(this.ordre==OrdreDeplacement.DEP_ARC_FORWARD.getValue())
			{
				result+=" tourne fait un arc arriere de rayon"+this.getData()+" \n";
				
			}
			else
				result+="probleme ordre\n";
		}
		else if(this.typeMessage==MessageType.MESSAGE_ROLE.getValue())
		{
			result+="ROLE \n";
			result+="Ordre: \t";
			
			if (this.ordre==OrdreRole.ROLE_IS_LEADER.getValue())
			{
				result+=" le robot est leader \n";
			}
			else if (this.ordre==OrdreRole.ROLE_IS_SLAVE.getValue())
			{
				result+="La cible n'est pas visible";
			}
			else if (this.ordre==OrdreRole.ROLE_SHUTDOWN.getValue()){
				result+="Demande d'arret du programme";
			}
			else
				result+="probleme ordre";
		
		}
		else 
		result+="Probleme \n";
		
		System.out.print(result);
	}
	
	
}


