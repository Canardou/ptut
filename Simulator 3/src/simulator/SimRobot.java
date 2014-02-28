package simulator;

public class SimRobot {

    protected int x, y ;
    protected int vx, vy ;
    private Sprite image ;
    private SimBoard board ;
    private boolean arrete ;
    private int direction ;
	
    public SimRobot (String nomImage, int init_x, int init_y, SimBoard brd) {    	
		this.x = init_x ;
		this.y = init_y ;
		this.board = brd ;
		this.image = brd.addSprite (nomImage, this.x, this.y) ;
		this.arrete = false ;
		this.direction = 0 ;
    }
    
    public void moveForward() {
    	if(this.direction==0) {
    		this.y = this.y-Param.TAILLECASE ;
    	}
    	else if(this.direction==1) {
    		this.x = this.x-Param.TAILLECASE ;
    	}
    	else if(this.direction==2) {
    		this.y = this.y+Param.TAILLECASE ;
    	}
    	else {
    		this.x = this.x+Param.TAILLECASE ;
    	}
    	this.image.moveTo(this.x-this.image.getLarg()/2,this.y-this.image.getHaut()/2);
    }
    
    public void turnLeft() {
    	this.direction = (this.direction+1)%4 ;
    }
    
    public void turnRight() {
    	this.direction = (this.direction+1)%4 ;
    }
}


