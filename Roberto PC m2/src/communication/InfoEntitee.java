package communication;

public class InfoEntitee {
	
	
	public EntiteeBT PCmarion;
	public EntiteeBT PCkiwor;
	public EntiteeBT PCthomas;
	
	public EntiteeBT robotH;
	public EntiteeBT robotJ;
	public EntiteeBT robotI;
	public EntiteeBT robotF;
	
	public InfoEntitee() {
		this.PCmarion= new EntiteeBT("marion","00:0C:78:33:EB:0C");
		this.PCkiwor= new EntiteeBT("kiwor-0", "00:15:83:0C:BF:EB");
		this.PCthomas= new EntiteeBT("Thomas", "26:0A:64:62:8D:29");
		
		this.robotH= new EntiteeBT("Robot H",(byte)1,"00:16:53:06:DA:CF");
		this.robotJ= new EntiteeBT("Robot J",(byte)2,"00:16:53:06:F5:30");
		this.robotF= new EntiteeBT("Robot F",(byte)0,"00:16:53:06:DE:F8");
	}

}
