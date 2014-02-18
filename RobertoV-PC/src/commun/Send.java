package commun;


public class Send
{
	
	
	public static void main(String[] args) throws Exception
	{
		int msg = System.in.read();
		Robot roberto;
		roberto = new Robot ("Robot 1","00:16:53:0A:6A:65");
		
		roberto.envoie(msg); // roberto PC
		
	}
	
}