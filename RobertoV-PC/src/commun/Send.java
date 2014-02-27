package commun;
import java.util.Scanner;


public class Send
{
	
	
	public static void main(String[] args) throws Exception
	{
		Robot roberto;
		roberto = new Robot ("Robot 1","00:16:53:0A:6A:65");
		int msg = 1;
		int rec = 1; 
		roberto.open();
		Scanner sc =new Scanner(System.in);
		while(msg!=99){
		rec = 1;
		System.out.print("j'attends vos ordres, ô maître:");
		msg=sc.nextInt();
		System.out.println("j'envoie test dans la boucle :" +msg);
		roberto.envoie(msg); // roberto PC
		while (rec!=0){
			 rec =roberto.recoie();
		}
		}
		roberto.close();
	}
	
}